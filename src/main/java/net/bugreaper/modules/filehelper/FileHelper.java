package net.bugreaper.modules.filehelper;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.core.exceptions.ConfigException;
import net.bugreaper.core.exceptions.FileReaderException;
import net.bugreaper.modules.filehelper.interfaces.FileHelperInt;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.bugreaper.core.filereaders.ResourcesFileReader.*;
import static net.bugreaper.core.mappers.StringMappers.formatBytes;
import static net.bugreaper.core.mappers.StringMappers.formatMilliseconds;
import static net.bugreaper.core.utils.AwaitUtils.awaitCustom;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class consists methods that operate with files in <b>src/test/resources</b>
 *
 * <p>Supports reading and managing dynamic files created during test execution.</p>
 *
 * <p>Recommended to use one instance:
 * {@code FileHelper fh = FileHelper.getInstance();}
 * </p>
 *
 * <p>Default await timeout for assertions with await is configured by {@link #awaitMs}.
 * It can be changed using {@link #setAwaitMs(int)} or configuration.</p>
 *
 * <p>Maximum file size for operations that limit file size is configured by
 * {@link #maxFileSize}. It can be changed using {@link #setMaxFileSize(long)} or configuration.</p>
 *
 * @author Oleksii Betin "ambu550"
 * @since 1.0.0
 */
@SuppressWarnings("squid:S5960")
public class FileHelper implements FileHelperInt {

    private static FileHelper instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    /**
     * default ms await in tests
     */
    private volatile int awaitMs = 2000;

    /**
     * default max file size in bytes for:
     * <p>{@link #createFileWithSize}, {@link #showDataFromFile}
     */
    private volatile long maxFileSize = 1_024L * 1024; // 1MB

    /**
     * Returns the instance of {@link FileHelper} with config builder {@link #FileHelper()}.
     * <p>
     * This implementation is thread-safe using method-level synchronization.
     *
     * @return the singleton instance of {@link FileHelper}
     * @see #FileHelper() config setup
     */
    public static synchronized FileHelper getInstance() {
        if (instance == null) {
            instance = new FileHelper();
        }

        return instance;
    }

    /**
     * Constructs a FileHelper client using YAML configuration.
     *
     * <p>Loads configuration values from a YAML file.</p>
     *
     * <p><b>Default file:</b> {@code bugreaper.yml}</p>
     * <p><b>Custom file:</b> using {@code -DbugreaperEnv=test} loads {@code bugreaper-test.yml}</p>
     *
     * <pre>
     * modules:
     *   file-helper:
     *     await: 500   # (optional) await timeout in milliseconds
     *     maxFileSize: 2048  # (optional) maximum file size in bytes
     * </pre>
     *
     * <p>Missing optional keys will fall back to predefined defaults.</p>
     *
     * @throws IllegalArgumentException if the configuration contains invalid values
     */
    public FileHelper() {
        loadFromYaml();
    }

    private void loadFromYaml() {

        try {
            //optional config fields
            Object awaitVal = YamlUtils.getValueByPath("modules.file-helper.await", true);
            if (awaitVal instanceof Number number) {
                setAwaitMs(number.intValue());
            }
            Object fileSizeVal = YamlUtils.getValueByPath("modules.file-helper.maxFileSize", true);
            if (fileSizeVal instanceof Number size) {
                setMaxFileSize(size.longValue());
            }
        } catch (ConfigException e) {
            LOGGER.warn("Config file error, but {} is not expected required keys: {}", this.getClass().getSimpleName(), e.getMessage());
        }

    }

    // setters

    @Override
    public FileHelper setAwaitMs(int awaitMs) {
        if (awaitMs < 200) {
            throw new IllegalArgumentException("awaitMs too small (can`t bee less 200ms)");
        }
        this.awaitMs = awaitMs;
        return this;
    }

    @Override
    public FileHelper setMaxFileSize(long maxFileSize) {
        if (maxFileSize < 1) {
            throw new IllegalArgumentException("maxFileSize too small (can`t bee less 1)");
        }
        this.maxFileSize = maxFileSize;
        return this;
    }

    // getters

    @Override
    public String getConfigSummary() {
        String info = String.format("""
                        %s:
                            await=%d
                            maxFileSize=%d%n""",
                this.getClass().getSimpleName(), awaitMs, maxFileSize);

        LOGGER.info(info);
        return info;
    }

    // interactions

    @Override
    @Step("(FILE) Clean {filePath} file")
    public void cleanFile(String filePath) {
        overwriteTextToResourceFile(filePath, "");
    }

    @Override
    @Step("(FILE) Delete {filePath} file")
    public void deleteFile(String filePath) {
        deleteResourceFile(filePath);
    }


    @Override
    @Step("(FILE) Add message: {message} to {filePath} file")
    public void addToFile(String filePath, String message) {
        writeTextToResourceFile(filePath, message);
    }

    @Override
    @Step("(FILE) Create with size: {sizeInBytes}-bytes {filePath} file")
    public void createFileWithSize(String filePath, long sizeInBytes) {
        try {
            Assertions.assertTrue(sizeInBytes <= maxFileSize);
        } catch (AssertionError e) {
            throw new FileReaderException("Provided size %s for file '%s' more then #maxFileSize(%s) (set or configure more if you need)"
                    .formatted(formatBytes(sizeInBytes), filePath, formatBytes(maxFileSize)));

        }
        createResourceFileWithSize(filePath, sizeInBytes);
    }

    @Override
    @Step("(FILE) Show data from {filePath}")
    public void showDataFromFile(String filePath) {
        checkFileSize(filePath);
        Allure.addAttachment(filePath, "text/json", readResourceFile(filePath));
    }

    @Override
    //no step
    public long getFileSize(String filePath) {
        return getResourceFileSize(filePath);
    }

    @Override
    //no step
    public int countMatchesInFile(String filePath, String expectedText, boolean regex) {
        checkFileSize(filePath);

        if (!regex) {
            return StringUtils.countMatches(readResourceFile(filePath), expectedText);
        }
        Pattern pattern = Pattern.compile(expectedText);
        Matcher matcher = pattern.matcher(readResourceFile(filePath));

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }


    // assertions in file

    @Override
    @Step("(FILE)[ASSERT] {filePath} should have regex: <{expectedTextPattern}>")
    public void seeFileContainsRegex(String filePath, String expectedTextPattern) {
        seeFileContainWithAwaitSetup(filePath, expectedTextPattern, true);
    }

    @Override
    @Step("(FILE)[ASSERT] {filePath} should have text: <{expectedText}>")
    public void seeFileContainString(String filePath, String expectedText) {
        seeFileContainWithAwaitSetup(filePath, expectedText, false);
    }

    @Override
    @Step("(FILE)[ASSERT] {filePath} should NOT have text: <{unexpectedText}>")
    public void seeFileDoesNotContainString(String filePath, String unexpectedText) {
        seeFileDoesNotContainSetup(filePath, unexpectedText, false);
    }

    @Override
    @Step("(FILE)[ASSERT] {filePath} size is exactly: {expectedSize} bytes")
    public void seeFileSizeIsExactly(String filePath, long expectedSize) {
        try {
            awaitCustom(awaitMs).untilAsserted(() ->
                    assertEquals(expectedSize, getResourceFileSize(filePath)));
        } catch (ConditionTimeoutException e) {

            throw new AssertionError("File '%s' size expected to be EXACTLY <%s> but got <%s> within %s"
                    .formatted(filePath, formatBytes(expectedSize), formatBytes(getResourceFileSize(filePath)), formatMilliseconds(awaitMs)));
        }
    }

    @Override
    @Step("(FILE)[ASSERT] {filePath} size is greater: {minSize} bytes")
    public void seeFileSizeIsGreaterThan(String filePath, long minSize) {
        try {
            awaitCustom(awaitMs).untilAsserted(() ->
                    Assertions.assertTrue(
                            getResourceFileSize(filePath) > minSize));
        } catch (ConditionTimeoutException e) {

            throw new AssertionError("File '%s' size expected to be GREATER <%s> but got <%s> within %s"
                    .formatted(filePath, formatBytes(minSize), formatBytes(getResourceFileSize(filePath)), formatMilliseconds(awaitMs)));
        }
    }


    @Override
    @Step("(FILE)[ASSERT] {filePath} size is less: {maxSize} bytes")
    public void seeFileSizeIsLessThan(String filePath, long maxSize) {
        try {
            awaitCustom(awaitMs).untilAsserted(() ->
                    Assertions.assertTrue(
                            getResourceFileSize(filePath) < maxSize));
        } catch (ConditionTimeoutException e) {
            throw new AssertionError("File '%s' size expected to be LESS <%s> but got <%s> within %s"
                    .formatted(filePath, formatBytes(maxSize), formatBytes(getResourceFileSize(filePath)), formatMilliseconds(awaitMs)));
        }
    }


    @Override
    public void seeFileHashMd5Equal(String filePath, String expectedHash) {
        seeFileHashAssertSetup(filePath, expectedHash, "MD5");
    }

    @Override
    public void seeFileHashSha1Equal(String filePath, String expectedHash) {
        seeFileHashAssertSetup(filePath, expectedHash, "SHA-1");
    }

    @Override
    public void seeFileHashSha256Equal(String filePath, String expectedHash) {
        seeFileHashAssertSetup(filePath, expectedHash, "SHA-256");
    }

    @Override
    public void seeFileHashSha512Equal(String filePath, String expectedHash) {
        seeFileHashAssertSetup(filePath, expectedHash, "SHA-512");
    }

    private void seeFileContainWithAwaitSetup(String filePath, String expectedText, Boolean regex) {
        try {
            awaitCustom(awaitMs).untilAsserted(() ->
                    assertNotEquals(0,
                            countMatchesInFile(filePath, expectedText, regex)));
        } catch (ConditionTimeoutException e) {
            throw new AssertionError(
                    "%nFile '%s' does not contain expected text <<%s>> within %s".formatted(filePath, expectedText, formatMilliseconds(awaitMs)));
        }
    }

    private void seeFileDoesNotContainSetup(String filePath, String expectedText, Boolean regex) {
        assertEquals(0,
                countMatchesInFile(filePath, expectedText, regex),
                "%nText <<%s>> should not be present in file: %s".formatted(expectedText, filePath));
    }

    @Step("(FILE)[ASSERT] {filePath} equal to {algorithm} hash: <{expectedHash}>")
    protected void seeFileHashAssertSetup(String filePath, String expectedHash, String algorithm) {
        assertEquals(
                expectedHash,
                hashFile(filePath, algorithm),
                MessageFormat.format(
                        "%nHash({0}) <<{1}>> not equal to file: {2}", algorithm, expectedHash, filePath)
        );
    }

    private static String hashFile(String filePath, String algorithm) {

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new FileReaderException("Not supported algorithm: " + algorithm, e);
        }

        String data = readResourceFile(filePath);

        byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        // Convert to hex
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private void checkFileSize(String filePath) {

        try {
            Assertions.assertTrue(getResourceFileSize(filePath) <= maxFileSize);
        } catch (AssertionError e) {
            throw new FileReaderException("File '%s' size(%s) more then provided #maxFileSize(%s) (set or configure more if you need)"
                    .formatted(filePath, formatBytes(getResourceFileSize(filePath)), formatBytes(maxFileSize)));

        }
    }

}

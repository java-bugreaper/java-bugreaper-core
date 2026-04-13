package net.bugreaper.modules.filehelper;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.core.exceptions.FileReaderException;
import net.bugreaper.modules.filehelper.interfaces.FileHelperInt;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import net.bugreaper.core.filereaders.ResourcesFileReader;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.core.ConditionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.bugreaper.core.mappers.StringMappers.formatMilliseconds;
import static net.bugreaper.core.utils.AwaitUtils.awaitCustom;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("squid:S5960")
public class FileHelper implements FileHelperInt {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    /**
     * default ms await in tests
     */
    private int await = 2000;


    /**
     * Constructor with configurations
     * <p> Loads configuration from YAML file.
     * <p>Default: bugreaper.yml
     * <p>Custom: -DbugreaperEnv=test loads bugreaper-test.yml
     */
    public FileHelper() {
        loadFromYaml();
    }

    private void loadFromYaml() {

        //optional config fields
        Object awaitVal = YamlUtils.getValueByPath("modules.file-helper.await", true);
        if (awaitVal instanceof Number number) {
            setAwaitMs(number.intValue());
        }

    }

    // setters

    /**
     * Set await for asserts in MS
     *
     * @param awaitMs await in MS
     * @throws IllegalArgumentException on invalid value (less 200)
     */
    public FileHelper setAwaitMs(int awaitMs) {
        if (awaitMs < 200) {
            throw new IllegalArgumentException("awaitMs too small (can`t bee less 200ms)");
        }
        this.await = awaitMs;
        return this;
    }

    // getters

    public int getAwait() {
        return await;
    }

    @Override
    public String getConfigSummary() {
        String info = String.format("""
                %s:
                    await=%d""", this.getClass().getSimpleName(), await);

        LOGGER.info(info);
        return info;
    }

    // interactions

    @Override
    @Step("(FILE) Clean {filePath} file")
    public void cleanFile(String filePath) {
        ResourcesFileReader.overwriteTextToResourceFile(filePath, "");
    }


    @Override
    @Step("(FILE) Add message: {message} to {filePath} file")
    public void addToFile(String filePath, String message) {
        ResourcesFileReader.writeTextToResourceFile(filePath, message);
    }


    @Override
    @Step("(FILE) Show data from {filePath}")
    public void showDataFromFile(String filePath) {
        Allure.addAttachment(filePath, "text/json", ResourcesFileReader.readResourceFile(filePath));
    }


    @Override
    public int countMatchesInFile(String filePath, String expectedText, boolean regex) {

        if (!regex) {
            return StringUtils.countMatches(ResourcesFileReader.readResourceFile(filePath), expectedText);
        }
        Pattern pattern = Pattern.compile(expectedText);
        Matcher matcher = pattern.matcher(ResourcesFileReader.readResourceFile(filePath));

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }


    // assertions in file

    @Override
    @Step("(FILE)[ASSERT] {filePath} should have regex: <{expectedText}>")
    public void seeFileContainsRegex(String filePath, String expectedText) {
        seeFileContainWithAwaitSetup(filePath, expectedText, true);
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
            awaitCustom(await).untilAsserted(() ->
                assertNotEquals(0,
                        countMatchesInFile(filePath, expectedText, regex)));
        } catch (ConditionTimeoutException e) {
            fail(
                    MessageFormat.format(
                            "\nFAILED: <<{0}>> expected to be present in file within {1}",
                            expectedText, formatMilliseconds(await)));
        }
    }

    private void seeFileDoesNotContainSetup(String filePath, String expectedText, Boolean regex) {
        assertEquals(0,
                countMatchesInFile(filePath, expectedText, regex),
                "\nFAILED: <<" + expectedText + ">> unexpected present in file");
    }

    @Step("(FILE)[ASSERT] {filePath} equal to {algorithm} hash: <{expectedHash}>")
    protected void seeFileHashAssertSetup(String filePath, String expectedHash, String algorithm) {
        assertEquals(
                expectedHash,
                hashFile(filePath, algorithm),
                MessageFormat.format(
                        "\nHash({0}) <<{1}>> not equal to file: {2}", algorithm, expectedHash, filePath)
        );
    }

    private static String hashFile(String filePath, String algorithm) {

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new FileReaderException("Not supported algorithm: " + algorithm, e);
        }

        String data = ResourcesFileReader.readResourceFile(filePath);

        byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        // Convert to hex
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}

package net.bugreaper.modules.filehelper;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.modules.filehelper.interfaces.FileHelperInt;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import net.bugreaper.core.filereaders.ResourcesFileReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.Duration.ofMillis;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


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
            withAwaitMs(number.intValue());
        }

    }

    // setters

    /**
     * Set await for asserts in MS
     * @param awaitMs await in MS
     * @throws IllegalArgumentException on invalid value (less 200)
     */
    public FileHelper withAwaitMs(int awaitMs) {
        if (awaitMs < 200){
            throw new IllegalArgumentException("awaitMs too small (can`t bee less 200ms)");
        }
        this.await = awaitMs;
        return this;
    }

    // getters

    public int getAwait() { return await; }

    public String getConfigSummary() {
        String info = String.format("""
        %s:
            await=%d""", this.getClass().getSimpleName(), await);

        LOGGER.info(info);
        return info;
    }

    // interactions

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

        if(!regex) {
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
    @Step("(FILE)[ASSERT] {filePath} contains regex: <{expectedText}>")
    public void seeFileContainsRegex(String filePath, String expectedText) {
        seeFileContainWithAwaitSetup(filePath, expectedText, true);
    }

    @Override
    @Step("(FILE)[ASSERT] {filePath} contains: <{expectedText}>")
    public void seeFileContainString(String filePath, String expectedText) {
        seeFileContainWithAwaitSetup(filePath, expectedText, false);
    }

    @Override
    @Step("(FILE)[ASSERT] {filePath} does NOT contains: <{unexpectedText}>")
    public void seeFileDoesNotContainString(String filePath, String unexpectedText) {
        seeFileDoesNotContainSetup(filePath, unexpectedText, false);
    }

    private void seeFileContainWithAwaitSetup(String filePath, String expectedText, Boolean regex) {
        await().pollDelay(ofMillis(0)).atMost(ofMillis(await)).untilAsserted(() ->
                assertNotEquals(0,
                        countMatchesInFile(filePath, expectedText, regex),
                        "\nSearch: <<" + expectedText + ">> in file"));
    }

    private void seeFileDoesNotContainSetup(String filePath, String expectedText, Boolean regex) {
        assertEquals(0,
                countMatchesInFile(filePath, expectedText, regex),
                "\nSearch: <<" + expectedText + ">> not exist in file");
    }

}

package io.bugreaper.modules.filehelper;

import io.bugreaper.modules.filehelper.interfaces.FileHelperInt;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.bugreaper.core.filereaders.ResourcesFileReader.*;
import static java.time.Duration.ofMillis;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SuppressWarnings("squid:S5960")
public class FileHelper implements FileHelperInt {

    /**
     * default ms await in tests
     */
    private long awaitMs = 2000;

    public FileHelper withAwaitMs(int awaitMs) {
        if (awaitMs < 200){
            throw new IllegalArgumentException("awaitMs too small (can`t bee less 200ms)");
        }
        this.awaitMs = awaitMs;
        return this;
    }

    @Step("(FILE) Clean {filePath} file")
    public void cleanFile(String filePath) {
        overwriteTextToResourceFile(filePath, "");
    }


    @Override
    @Step("(FILE) Add message: {message} to {filePath} file")
    public void addToFile(String filePath, String message) {
        writeTextToResourceFile(filePath, message);
    }


    @Override
    @Step("(FILE) Show data from {filePath}")
    public void showDataFromFile(String filePath) {
            Allure.addAttachment(filePath, "text/json", readResourceFile(filePath));
    }


    @Override
    public int countMatchesInFile(String filePath, String expectedText, boolean regex) {

        if(!regex) {
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
        await().pollDelay(ofMillis(0)).atMost(ofMillis(awaitMs)).untilAsserted(() ->
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
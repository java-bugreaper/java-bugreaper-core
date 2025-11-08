package io.bugreaper.modules.filehelper;

import io.bugreaper.modules.filehelper.interfaces.LogHelperInt;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.bugreaper.core.filereaders.ResourcesFileReader.*;
import static java.time.Duration.ofMillis;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("squid:S5960")
public class LogHelper implements LogHelperInt {

    /**
     * default ms await in tests
     */
    private long awaitMs = 2000;

    private final String logPath;

    public LogHelper(String logPath) {
        if (logPath == null || logPath.isBlank()) {
            throw new IllegalArgumentException("logPath can`t be empty or null");
        }
        this.logPath = logPath;
    }

    public LogHelper withAwaitMs(int awaitMs) {
        if (awaitMs < 200) {
            throw new IllegalArgumentException("awaitMs too small (can`t bee less 200ms)");
        }
        this.awaitMs = awaitMs;
        return this;
    }

    @Override
    public void cleanLogs() {
        Allure.step(MessageFormat.format("(LOGS) Clean {0}", logPath),
                () -> overwriteTextToResourceFile(logPath, "")
        );

        overwriteTextToResourceFile(logPath, "");
    }


    @Override
    public void addToLogs(String message) {
        Allure.step(MessageFormat.format("(LOGS) Add message: <{0}> to {1}", message, logPath),
                () -> writeTextToResourceFile(logPath, message)
        );
    }


    @Override
    public void showDataFromLogs() {
        Allure.step(MessageFormat.format("(LOGS) Show logs from {0}", logPath),
                () -> Allure.addAttachment(logPath, "text/json", readResourceFile(logPath))
        );
    }


    @Override
    public int countInLogs(String expectedText, boolean regex) {

        if (!regex) {
            return StringUtils.countMatches(readResourceFile(logPath), expectedText);
        }
        Pattern pattern = Pattern.compile(expectedText);
        Matcher matcher = pattern.matcher(readResourceFile(logPath));

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }


    // assertions in logs

    @Override
    public void seeLogsContainsRegex(String expectedText) {
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Search REGEX text in {0}: <{1}>", logPath, expectedText),
                () -> seeLogsContainWithAwaitSetup(expectedText, true)
        );
    }

    @Override
    public void seeLogsContainString(String expectedText) {
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Search EQUAL text in {0}: <{1}>", logPath, expectedText),
                () -> seeLogsContainWithAwaitSetup(expectedText, false)
        );
    }

    @Override
    public void seeLogsDoesNotContainString(String unexpectedText) {
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Search text NOT exist in {0}: <{1}>", logPath, unexpectedText),
                () -> seeLogsDoesNotContainSetup(unexpectedText, false)
        );
    }

    private void seeLogsContainWithAwaitSetup(String expectedText, Boolean regex) {
        await().pollDelay(ofMillis(0)).atMost(ofMillis(awaitMs)).untilAsserted(() ->
                assertNotEquals(0,
                        countInLogs(expectedText, regex),
                        "\nSearch: <<" + expectedText + ">> in logs")
        );
    }

    private void seeLogsDoesNotContainSetup(String expectedText, Boolean regex) {
        assertEquals(0,
                countInLogs(expectedText, regex),
                "\nSearch: <<" + expectedText + ">> not exist in logs");
    }

}
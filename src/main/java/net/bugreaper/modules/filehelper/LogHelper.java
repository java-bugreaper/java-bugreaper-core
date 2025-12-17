package net.bugreaper.modules.filehelper;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.modules.filehelper.interfaces.LogHelperInt;
import io.qameta.allure.Allure;
import net.bugreaper.core.filereaders.ResourcesFileReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static java.time.Duration.ofMillis;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("squid:S5960")
public class LogHelper implements LogHelperInt {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogHelper.class);

    /**
     * default ms await in tests
     */
    private int await = 2000;
    private String logFilePath = "default/server.log";


    /**
     * Constructor with provided params
     * @param logFilePath path to logfile in test resources (example "logs/server/logs.log"
     */
    public LogHelper(String logFilePath) {
        if (logFilePath == null || logFilePath.isBlank()) {
            throw new IllegalArgumentException("logPath can`t be empty or null");
        }
        this.logFilePath = logFilePath;
    }

    /**
     * Constructor with configurations
     * <p> Loads configuration from YAML file.
     * <p>Default: bugreaper.yml
     * <p>Custom: -DbugreaperEnv=test loads bugreaper-test.yml
     */
    public LogHelper() {
        loadFromYaml();
    }

    private void loadFromYaml() {

        //required config fields
        String fileVal = YamlUtils.getStringValueByPath("modules.log-helper.logfile");
        withLogfile(fileVal);

        //optional config fields
        Object awaitVal = YamlUtils.getValueByPath("modules.log-helper.await", true);
        if (awaitVal instanceof Number number) {
            withAwaitMs(number.intValue());
        }

    }

    //setters

    /**
     * Set await for asserts in MS
     * @param awaitMs await in MS
     * @throws IllegalArgumentException on invalid value (less 200)
     */
    public LogHelper withAwaitMs(int awaitMs) {
        if (awaitMs < 200) {
            throw new IllegalArgumentException("awaitMs too small (can`t bee less 200ms)");
        }
        this.await = awaitMs;
        return this;
    }

    /**
     * Set log file for interactions
     * @param logFilePath path to logfile in test resources (example "logs/server/logs.log"
     * @throws IllegalArgumentException on invalid value
     */
    public LogHelper withLogfile(String logFilePath) {
        if (logFilePath == null || logFilePath.isEmpty()) {
            throw new IllegalArgumentException("logfile can`t bee empty or null");
        }
        this.logFilePath = logFilePath;
        return this;
    }

    // getters

    public int getAwait() { return await; }
    public String getLogfilePath() { return logFilePath; }


    public String getConfigSummary() {
        String info = String.format("""
        %s:
            await=%d
            logfile=%s%n""", this.getClass().getSimpleName(), await, logFilePath);

        LOGGER.info(info);
        return info;
    }

    // interactions

    @Override
    public void cleanLogs() {
        Allure.step(MessageFormat.format("(LOGS) Clean {0}", logFilePath),
                () -> ResourcesFileReader.overwriteTextToResourceFile(logFilePath, "")
        );

        ResourcesFileReader.overwriteTextToResourceFile(logFilePath, "");
    }


    @Override
    public void addToLogs(String message) {
        Allure.step(MessageFormat.format("(LOGS) Add message: <{0}> to {1}", message, logFilePath),
                () -> ResourcesFileReader.writeTextToResourceFile(logFilePath, message)
        );
    }


    @Override
    public void showDataFromLogs() {
        Allure.step(MessageFormat.format("(LOGS) Show logs from {0}", logFilePath),
                () -> Allure.addAttachment(logFilePath, "text/json", ResourcesFileReader.readResourceFile(logFilePath))
        );
    }


    @Override
    public int countInLogs(String expectedText, boolean regex) {

        if (!regex) {
            return StringUtils.countMatches(ResourcesFileReader.readResourceFile(logFilePath), expectedText);
        }
        Pattern pattern = Pattern.compile(expectedText);
        Matcher matcher = pattern.matcher(ResourcesFileReader.readResourceFile(logFilePath));

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }


    // assertions in logs

    @Override
    public void seeLogsContainsRegex(String expectedText) {
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Search REGEX text in {0}: <{1}>", logFilePath, expectedText),
                () -> seeLogsContainWithAwaitSetup(expectedText, true)
        );
    }

    @Override
    public void seeLogsContainString(String expectedText) {
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Search EQUAL text in {0}: <{1}>", logFilePath, expectedText),
                () -> seeLogsContainWithAwaitSetup(expectedText, false)
        );
    }

    @Override
    public void seeLogsDoesNotContainString(String unexpectedText) {
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Search text NOT exist in {0}: <{1}>", logFilePath, unexpectedText),
                () -> seeLogsDoesNotContainSetup(unexpectedText, false)
        );
    }

    private void seeLogsContainWithAwaitSetup(String expectedText, Boolean regex) {
        await().pollDelay(ofMillis(0)).atMost(ofMillis(await)).untilAsserted(() ->
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

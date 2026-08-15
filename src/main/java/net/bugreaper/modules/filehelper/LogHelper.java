package net.bugreaper.modules.filehelper;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.core.exceptions.ConfigException;
import net.bugreaper.modules.filehelper.interfaces.LogHelperInt;
import io.qameta.allure.Allure;
import net.bugreaper.core.filereaders.ResourcesFileReader;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.core.ConditionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static net.bugreaper.core.mappers.StringMappers.formatMilliseconds;
import static net.bugreaper.core.utils.AwaitUtils.awaitCustom;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides methods for working with log files in <b>src/test/resources</b>.
 *
 * <p>The log file path is provided during initialization. Log files can be mounted
 * from external services or created and updated during test execution.</p>
 *
 * <p>Supports reading and managing dynamic log files created during test execution.</p>
 *
 * <p>It is recommended to use a single instance:
 * {@code LogHelper lh = LogHelper.getInstance();}
 * </p>
 *
 * <p>Default await timeout for assertions with await is configured by {@link #awaitMs}.
 * It can be changed using {@link #setAwaitMs(int)}.</p>
 *
 * @author Oleksii Betin "ambu550"
 * @since 1.0.0
 */
@SuppressWarnings("squid:S5960")
public class LogHelper implements LogHelperInt {

    private static LogHelper instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogHelper.class);

    /**
     * Default await timeout for tests, in milliseconds.
     */
    private volatile int awaitMs = 2000;

    /**
     * Default await polling interval in milliseconds for tests.
     */
    private volatile int awaitPollInterval = 100;

    private volatile String logFilePath;


    public LogHelper(String logFilePath) {
        setLogFile(logFilePath);
    }

    /**
     * Returns the instance of {@link LogHelper} with config builder {@link #LogHelper()}.
     * <p>
     * This implementation is thread-safe using method-level synchronization.
     *
     * @return the shared instance of {@link LogHelper}
     * @throws ConfigException if the configuration contains invalid values
     * @see #LogHelper() config setup
     */
    public static synchronized LogHelper getInstance() {
        if (instance == null) {
            instance = new LogHelper();
        }

        return instance;
    }

    /**
     * Constructs a LogHelper client using YAML configuration.
     *
     * <p>Loads configuration values from a YAML file.</p>
     *
     * <p><b>Default file:</b> {@code bugreaper.yml}</p>
     * <p><b>Custom file:</b> using {@code -DbugreaperEnv=test} loads {@code bugreaper-test.yml}</p>
     *
     * <pre>
     * modules:
     *   log-helper:
     *     logfile: 'logs/server/logs.log'  # path in src/test/resources
     *     await: 500  # (optional)
     * </pre>
     *
     * <p>Missing required keys will result in configuration errors.
     * Missing optional keys will fall back to predefined defaults.</p>
     *
     * @throws ConfigException if the configuration contains invalid values
     */
    public LogHelper() {
        loadFromYaml();
    }

    private void loadFromYaml() {

        //required config fields
        String fileVal = YamlUtils.getStringValueByPath("modules.log-helper.logfile");
        setLogFile(fileVal);

        //optional config fields
        Object awaitVal = YamlUtils.getValueByPath("modules.log-helper.await", true);
        if (awaitVal instanceof Number number) {
            setAwaitMs(number.intValue());
        }
        Object awaitPollIntervalVal = YamlUtils.getValueByPath("modules.log-helper.await-poll-interval", true);
        if (awaitPollIntervalVal instanceof Number number) {
            this.awaitPollInterval = number.intValue();
        }
    }

    //setters

    @Override
    public LogHelper setAwaitMs(int awaitMs) {
        if (awaitMs < 200) {
            throw new IllegalArgumentException("awaitMs too small (can`t bee less 200ms)");
        }
        this.awaitMs = awaitMs;
        return this;
    }

    @Override
    public LogHelper setLogFile(String logFilePath) {
        if (logFilePath == null || logFilePath.isEmpty()) {
            throw new IllegalArgumentException("Log file path must not be null or empty");
        }
        this.logFilePath = logFilePath;
        return this;
    }

    // getters

    @Override
    public String getConfigSummary() {
        String info = String.format("""
                %s:
                    await=%d
                    await-poll-interval=%d
                    logfile=%s%n""", this.getClass().getSimpleName(), awaitMs, awaitPollInterval, logFilePath);

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
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Should have REGEX text in {0}: <{1}>", logFilePath, expectedText),
                () -> seeLogsContainWithAwaitSetup(expectedText, true)
        );
    }

    @Override
    public void seeLogsContainString(String expectedText) {
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Should have text in {0}: <{1}>", logFilePath, expectedText),
                () -> seeLogsContainWithAwaitSetup(expectedText, false)
        );
    }

    @Override
    public void seeLogsDoesNotContainString(String unexpectedText) {
        Allure.step(MessageFormat.format("(LOGS)[ASSERT] Should NOT have text in {0}: <{1}>", logFilePath, unexpectedText),
                () -> seeLogsDoesNotContainSetup(unexpectedText, false)
        );
    }

    private void seeLogsContainWithAwaitSetup(String expectedText, Boolean regex) {
        try {
            awaitCustom(awaitMs, awaitPollInterval).untilAsserted(() ->
                    assertNotEquals(0,
                            countInLogs(expectedText, regex)));
        } catch (ConditionTimeoutException e) {
            throw new AssertionError(
                    "%nLog file '%s' does not contain expected text%s <<%s>> within %s".formatted(logFilePath, regText(regex), expectedText, formatMilliseconds(awaitMs)));
        }
    }

    private void seeLogsDoesNotContainSetup(String expectedText, Boolean regex) {
        assertEquals(0,
                countInLogs(expectedText, regex),
                "%nText%s <<%s>> should not be present in log file: %s".formatted(regText(regex), expectedText, logFilePath));
    }

    private String regText(boolean regex){
        if (regex) {
            return "(regex)";
        }
        return "";
    }

}

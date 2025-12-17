package logs;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.core.exceptions.ConfigException;
import net.bugreaper.modules.filehelper.LogHelper;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class LogHelperConfigTests {

    public static final String LOG_FILE_CONF = "logs/byyml/config.log";
    public static final String LOG_FILE_TEST = "logs/byyml/config-test.log";
    public static final String MESSAGE = "some message2";
    public static final String PROPERTY = "bugreaperEnv";
    

    @BeforeEach
    void copyConfig() {
        System.clearProperty(PROPERTY);
        YamlUtils.clearCache();
    }

    @Test
    void testLogMessageAddAndCountByConfig() {
        LogHelper logs = new LogHelper();

        logs.cleanLogs();
        logs.addToLogs(MESSAGE);
        var ar =logs.countInLogs(MESSAGE, false);

        assertEquals(1, ar, "Log message add and counted");

        String retest = readResourceFile(LOG_FILE_CONF);
        assertEquals(MESSAGE, retest);


        MatcherAssert.assertThat(
                "Info summary",
                logs.getConfigSummary(),
                StringContains.containsString("""
                        LogHelper:
                            await=600
                            logfile=logs/byyml/config.log"""));

    }

    @Test
    void testLogAwaitByConfig() {

        LogHelper logs = new LogHelper();

        logs.cleanLogs();
        logs.getConfigSummary();

        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                logs.seeLogsContainString(MESSAGE));

        MatcherAssert.assertThat(
                "Await for assert from yml",
                exception.getMessage(),
                StringContains.containsString("Search: <<" + MESSAGE + ">> in logs ==> expected: not equal but was: <0> within 600 milliseconds."));
    }

    @Test
    void testNoOptionalFieldInConfig() {

        System.setProperty(PROPERTY, "nooptional");

        LogHelper logs = new LogHelper();

        assertEquals(2000, logs.getAwait(), "Default await used");
        assertEquals("logs/byyml/noop.log", logs.getLogfilePath(), "Required key used");
    }

    @Test
    void testLogMessageAddAndCountByTestConfig() {

        System.setProperty(PROPERTY, "test");

        LogHelper logs = new LogHelper();

        logs.cleanLogs();
        logs.addToLogs(MESSAGE);
        var ar =logs.countInLogs(MESSAGE, false);

        assertEquals(1, ar, "Log message add and counted");

        String retest = readResourceFile(LOG_FILE_TEST);
        assertEquals(MESSAGE, retest);
    }

    @Test
    void testLogAwaitByTestConfig() {

        System.setProperty(PROPERTY, "test");

        LogHelper logs = new LogHelper();

        logs.cleanLogs();

        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                logs.seeLogsContainString(MESSAGE));

        MatcherAssert.assertThat(
                "Await for assert from yml",
                exception.getMessage(),
                StringContains.containsString("Search: <<" + MESSAGE + ">> in logs ==> expected: not equal but was: <0> within 200 milliseconds."));
    }

    @Test
    void testConfigNotExists() {

        System.setProperty(PROPERTY, "not_exists");

        Throwable exception = assertThrows(ConfigException.class, LogHelper::new);

        MatcherAssert.assertThat(
                "No config found",
                exception.getMessage(),
                StringContains.containsString("""
                        Failed to load YAML: bugreaper-not_exists.yml
                        Config file not found: bugreaper-not_exists.yml"""));
    }

    @Test
    void testConfigMissingRequiredField() {

        System.setProperty(PROPERTY, "nopath");

        Throwable exception = assertThrows(ConfigException.class, LogHelper::new);

        MatcherAssert.assertThat(
                "No required field",
                exception.getMessage(),
                StringContains.containsString("""
                        Missing required config field: modules.log-helper.logfile"""));
    }

    @Test
    void testConfigNullRequiredField() {

        System.setProperty(PROPERTY, "nullpath");

        Throwable exception = assertThrows(ConfigException.class, LogHelper::new);

        MatcherAssert.assertThat(
                "Null required field",
                exception.getMessage(),
                StringContains.containsString("""
                        Config key 'modules.log-helper.logfile' is present but null. Null is not allowed"""));
    }

    @Test
    void testConfigBrokenPathToRequiredField() {

        System.setProperty(PROPERTY, "brpath");

        Throwable exception = assertThrows(ConfigException.class, LogHelper::new);

        MatcherAssert.assertThat(
                "Broken required field",
                exception.getMessage(),
                StringContains.containsString("""
                       Path segment 'logfile' does not lead to a map (path: modules.log-helper.logfile)"""));
    }
}

package filehelpers;

import net.bugreaper.modules.filehelper.LogHelper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogHelperTest {

    LogHelper logReaderV2 = new LogHelper(LOG_FILE).setAwaitMs(500);
    LogHelper logReaderWrong = new LogHelper(WRONG_FILE).setAwaitMs(500);

    public static final String WRONG_FILE = "logs/test/wrong.log";
    public static final String LOG_FILE = "logs/test/test.log";
    public static final String MESSAGE = "some message";


    @Test
    void testExistLogsFailedSetAwaitMsSetTime() {
        logReaderV2.cleanLogs();
        Throwable exception = assertThrows(AssertionError.class, () ->
                logReaderV2.seeLogsContainsRegex(MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("Log file 'logs/test/test.log' does not contain expected text <<some message>> within 500 milliseconds"));
    }

    @Test
    void testGetLogsNotExist() {

        Throwable exception = assertThrows(Exception.class, () ->
                logReaderWrong.showDataFromLogs());

        MatcherAssert.assertThat(
                "Logs not found message",
                exception.getMessage(),
                stringContainsInOrder("Failed to read file:", "/src/test/resources/logs/test/wrong.log"));
    }

    @Test
    void configLogHelperAwaitMsTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                logReaderV2.setAwaitMs(50));

        assertEquals(
                "awaitMs too small (can`t bee less 200ms)",
                exception.getMessage(),
                "Error on config .setAwaitMs validation");
    }

    @Test
    void configLogHelperLogfileEmptyTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                logReaderV2.setLogfile(""));

        assertEquals(
                "logfile can`t bee empty or null",
                exception.getMessage());
    }

    @Test
    void configLogHelperLogfileNullTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                logReaderV2.setLogfile(null));

        assertEquals(
                "logfile can`t bee empty or null",
                exception.getMessage());
    }

}

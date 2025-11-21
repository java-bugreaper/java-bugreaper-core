package logs;

import io.bugreaper.modules.filehelper.LogHelper;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogHelperTest {

    LogHelper logReaderV2 = new LogHelper(LOG_FILE).withAwaitMs(500);
    LogHelper logReaderWrong = new LogHelper(WRONG_FILE).withAwaitMs(500);

    public static final String WRONG_FILE = "logs/test/wrong.log";
    public static final String LOG_FILE = "logs/test/test.log";
    public static final String MESSAGE = "some message";


    @Test
    void testExistLogsFailedWithAwaitMsSetTime() {
        logReaderV2.cleanLogs();
        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                logReaderV2.seeLogsContainsRegex(MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("Search: <<" + MESSAGE + ">> in logs ==> expected: not equal but was: <0> within 500 milliseconds."));
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

}

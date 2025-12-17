package logs;

import net.bugreaper.modules.filehelper.LogHelper;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogHelperExtendTest extends LogHelper {

    public static final String LOG_FILE = "logs/test/test.log";

    public static final String MESSAGE = "some message";

    public LogHelperExtendTest() {
        super(LOG_FILE);
    }

    LogHelper logTime = new LogHelper(LOG_FILE).withAwaitMs(400);


    @Test
    void testLogMessageAddAndCount() {
        cleanLogs();
        addToLogs(MESSAGE);
        var ar = countInLogs(MESSAGE, false);
        assertEquals(1, ar, "Log message add and counted");
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testLogMessageRegular() {
        cleanLogs();
        addToLogs("some[1]");
        seeLogsContainsRegex("some.1.");
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testLogMessageExactly() {
        cleanLogs();
        addToLogs("some[*1]");
        seeLogsContainString("some[*1]");
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testGetLogs() {
        cleanLogs();
        showDataFromLogs();
    }


    @Test
    void testLogMessageCleanedAndCount() {
        addToLogs(MESSAGE);
        cleanLogs();
        var ar = countInLogs(MESSAGE, true);
        assertEquals(0, ar, "Log message cleaned and counted");
    }

    @Test
    void testExistLogsFailedWithAwaitMs() {
        cleanLogs();
        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                seeLogsContainsRegex(MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("Search: <<" + MESSAGE + ">> in logs ==> expected: not equal but was: <0> within 2 seconds."));
    }

    @Test
    void testExistLogsFailedWithSpecificAwait() {
        cleanLogs();
        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                logTime.seeLogsContainsRegex(MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("Search: <<" + MESSAGE + ">> in logs ==> expected: not equal but was: <0> within 400 milliseconds."));
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testExistLogsSuccessMoreThenOne() {
        cleanLogs();
        addToLogs(MESSAGE);
        addToLogs(MESSAGE);

        seeLogsContainsRegex(MESSAGE);
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testSeeFileDoesNotContainStringSuccess() {
        cleanLogs();
        addToLogs(MESSAGE);

        seeLogsDoesNotContainString(MESSAGE + "_tail");
    }

    @Test
    void testSeeFileDoesNotContainStringFailed() {
        cleanLogs();
        addToLogs(MESSAGE);

        Throwable exception = assertThrows(AssertionError.class, () ->
                seeLogsDoesNotContainString(MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message exist in logs",
                exception.getMessage(),
                StringContains.containsString("\nSearch: <<" + MESSAGE + ">> not exist in logs"));
    }


    @Test
    void testAddLogsWithNewLine() {

        String text = "test\nthis";

        cleanLogs();
        addToLogs(text);

        var ar = readResourceFile(LOG_FILE);
        assertEquals(text, ar, "Logs are written with new lines");

    }

    @Test
    void testAddLogWithQuotesLine() {

        String text = "test'this";

        cleanLogs();
        addToLogs(text);

        var ar = readResourceFile(LOG_FILE);
        assertEquals(text, ar, "Logs are written with quotes");

    }

}

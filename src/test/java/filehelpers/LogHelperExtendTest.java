package filehelpers;

import com.fasterxml.jackson.databind.JsonNode;
import net.bugreaper.core.utils.AllureAssert;
import net.bugreaper.core.utils.AllureResultLoader;
import net.bugreaper.modules.filehelper.LogHelper;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("java:S2699")
class LogHelperExtendTest extends LogHelper {

    public static final String LOG_FILE = "logs/test/test.log";

    public static final String MESSAGE = "some message";

    public LogHelperExtendTest() {
        super(LOG_FILE);
    }

    LogHelper logTime = new LogHelper(LOG_FILE).setAwaitMs(400);


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
    @Order(1)
    void allureStepsCheck() {
        cleanLogs();
        addToLogs(MESSAGE);
        showDataFromLogs();
    }

    @Test
    @Order(2)
    void allureStepsCheck2() {

        JsonNode result = AllureResultLoader.loadByTestName("allureStepsCheck");

        AllureAssert.assertThat(result)
                .hasStep("(LOGS) Clean logs/test/test.log")
                .hasStep("(LOGS) Add message: <some message> to logs/test/test.log")
                .hasStep("(LOGS) Show logs from logs/test/test.log")
                .hasAttachment("logs/test/test.log")
                .hasAttachment("logs/test/test.log", "some message");
    }


    @Test
    void testExistLogsFailedSetAwaitMs() {
        cleanLogs();
        Throwable exception = assertThrows(AssertionError.class, () ->
                seeLogsContainsRegex(MESSAGE));

        assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("FAILED: <<" + MESSAGE + ">> expected to be present in logs within 2 seconds"));
    }

    @Test
    void testExistLogsFailedWithSpecificAwait() {
        cleanLogs();
        Throwable exception = assertThrows(AssertionError.class, () ->
                logTime.seeLogsContainsRegex(MESSAGE));

        assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("FAILED: <<" + MESSAGE + ">> expected to be present in logs within 400 milliseconds"));
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

        assertThat(
                "Failed message when message exist in logs",
                exception.getMessage(),
                StringContains.containsString("FAILED: <<" + MESSAGE + ">> unexpected present in logs"));
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

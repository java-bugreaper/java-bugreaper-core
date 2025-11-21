package logs;

import io.bugreaper.modules.filehelper.FileHelper;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static io.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.*;

class FileHelperExtendTest extends FileHelper {

    public static final String LOG_FILE = "logs/test/test.log";
    public static final String WRONG_FILE = "logs/test/wrong.log";
    public static final String MESSAGE = "some message";

    FileHelper fileTime = new FileHelper().withAwaitMs(400);

    @Test
    void testLogMessageAddAndCount() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, MESSAGE);
        var result = countMatchesInFile(LOG_FILE, MESSAGE, false);
        assertEquals(1, result, "Log message add and counted");
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testLogMessageRegular() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, "some[1]");
        seeFileContainsRegex(LOG_FILE, "some.1.");
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testLogMessageExactly() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, "some[*1]");
        seeFileContainString(LOG_FILE, "some[*1]");
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testGetLogs() {
        cleanFile(LOG_FILE);
        showDataFromFile(LOG_FILE);
    }

    @Test
    void testGetLogsNotExist() {

        Throwable exception = assertThrows(Exception.class, () ->
                showDataFromFile(WRONG_FILE));

        MatcherAssert.assertThat(
                "Logs not found message",
                exception.getMessage(),
                stringContainsInOrder("Failed to read file:", "/src/test/resources/logs/test/wrong.log"));
    }

    @Test
    void testLogMessageCleanedAndCount() {
        addToFile(LOG_FILE, MESSAGE);
        cleanFile(LOG_FILE);
        var result = countMatchesInFile(LOG_FILE, MESSAGE, true);
        assertEquals(0, result, "Log message cleaned and counted");
    }

    @Test
    void testExistLogsFailedWithAwaitMs() {
        cleanFile(LOG_FILE);
        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                fileTime.seeFileContainsRegex(LOG_FILE, MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in file",
                exception.getMessage(),
                StringContains.containsString("Search: <<" + MESSAGE + ">> in file ==> expected: not equal but was: <0> within 400 milliseconds."));
    }

    @Test
    void testExistLogsFailedWithAwaitMsSetTime() {
        cleanFile(LOG_FILE);
        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                fileTime.seeFileContainsRegex(LOG_FILE, MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("Search: <<" + MESSAGE + ">> in file ==> expected: not equal but was: <0> within 400 milliseconds."));
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testExistLogsSuccessMoreThenOne() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, MESSAGE);
        addToFile(LOG_FILE, MESSAGE);

        seeFileContainsRegex(LOG_FILE, MESSAGE);
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testSeeFileDoesNotContainStringSuccess() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, MESSAGE);

        seeFileDoesNotContainString(LOG_FILE, MESSAGE + "_tail");
    }

    @Test
    void testSeeFileDoesNotContainStringFailed() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, MESSAGE);

        Throwable exception = assertThrows(AssertionError.class, () ->
                seeFileDoesNotContainString(LOG_FILE, MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message exist in logs",
                exception.getMessage(),
                StringContains.containsString("\nSearch: <<" + MESSAGE + ">> not exist in file"));
    }


    @Test
    void testAddLogsWithNewLine() {

        String text = "test\nthis";

        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, text);

        var result = readResourceFile(LOG_FILE);
        assertEquals(text, result, "Logs are written with new lines");

    }

    @Test
    void testAddLogWithQuotesLine() {

        String text = "test'this";

        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, text);

        var result = readResourceFile(LOG_FILE);
        assertEquals(text, result, "Logs are written with quotes");

    }

}

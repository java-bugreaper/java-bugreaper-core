package logs;

import io.bugreaper.modules.filehelper.FileHelper;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileHelperTest {

    FileHelper fileHelper = new FileHelper().withAwaitMs(500);

    public static final String LOG_FILE = "logs/test/test.log";
    public static final String MESSAGE = "some message";


    @Test
    void testExistLogsFailedWithAwaitMsSetTime() {
        fileHelper.cleanFile(LOG_FILE);
        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                fileHelper.seeFileContainsRegex(LOG_FILE, MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("Search: <<" + MESSAGE + ">> in file ==> expected: not equal but was: <0> within 500 milliseconds."));
    }

}

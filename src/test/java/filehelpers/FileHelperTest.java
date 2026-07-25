package filehelpers;

import net.bugreaper.modules.filehelper.FileHelper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileHelperTest {

    FileHelper fileHelper = new FileHelper().setAwaitMs(500);

    public static final String LOG_FILE = "logs/test/test.log";
    public static final String MESSAGE = "some message";


    @Test
    void testExistLogsFailedWithAwaitMsSetTime() {
        fileHelper.cleanFile(LOG_FILE);
        Throwable exception = assertThrows(AssertionError.class, () ->
                fileHelper.seeFileContainsRegex(LOG_FILE, MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("FAILED: <<" + MESSAGE + ">> expected to be present in file <logs/test/test.log> within 500 milliseconds"));
    }

}

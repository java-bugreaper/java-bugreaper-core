package filehelpers;

import net.bugreaper.core.exceptions.FileReaderException;
import net.bugreaper.modules.filehelper.FileHelper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("squid:S2699")
class FileHelperExtendTest extends FileHelper {

    public static final String LOG_FILE = "logs/test/test.log";
    public static final String WRONG_FILE = "logs/test/wrong.log";
    public static final String HASH_FILE = "files/test_hash.txt";
    public static final String MESSAGE = "some message";

    FileHelper fileTime = new FileHelper().setAwaitMs(400).setMaxFileSize(99999999);

    @Test
    void testNoOptionalAwaitConfig() {

        MatcherAssert.assertThat(
                "Info summary",
                fileTime.getConfigSummary(),
                StringContains.containsString("""
                        FileHelper:
                            await=400
                            maxFileSize=99999999"""));
    }


    @Test
    void testLogMessageAddAndCount() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, MESSAGE);
        var result = countMatchesInFile(LOG_FILE, MESSAGE, false);
        assertEquals(1, result, "Log message add and counted");
    }

    @Test
    void testLogMessageRegular() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, "some[1]");
        seeFileContainsRegex(LOG_FILE, "some.1.");
    }

    @Test
    void testLogMessageExactly() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, "some[*1]");
        seeFileContainString(LOG_FILE, "some[*1]");
    }

    @Test
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
        Throwable exception = assertThrows(AssertionError.class, () ->
                fileTime.seeFileContainsRegex(LOG_FILE, MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in file",
                exception.getMessage(),
                StringContains.containsString("FAILED: <<" + MESSAGE + ">> expected to be present in file <logs/test/test.log> within 400 milliseconds"));
    }

    @Test
    void testExistLogsFailedWithAwaitMsSetTime() {
        cleanFile(LOG_FILE);
        Throwable exception = assertThrows(AssertionError.class, () ->
                fileTime.seeFileContainsRegex(LOG_FILE, MESSAGE));

        MatcherAssert.assertThat(
                "Failed message when message not exist in logs",
                exception.getMessage(),
                StringContains.containsString("FAILED: <<" + MESSAGE + ">> expected to be present in file <logs/test/test.log> within 400 milliseconds"));
    }

    @Test
    void testExistLogsSuccessMoreThenOne() {
        cleanFile(LOG_FILE);
        addToFile(LOG_FILE, MESSAGE);
        addToFile(LOG_FILE, MESSAGE);

        seeFileContainsRegex(LOG_FILE, MESSAGE);
    }

    @Test
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
                StringContains.containsString("FAILED: <<" + MESSAGE + ">> unexpected present in file"));
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

    @Test
    void testHashMd5Pass() {
        seeFileHashMd5Equal(HASH_FILE, "b156152dfc1637561d6bab4e6bd1aee3");
    }

    @Test
    void testHashMd5Failed() {
        Throwable exception = assertThrows(AssertionError.class, () ->
                seeFileHashMd5Equal(HASH_FILE, "8888152dfc1637561d6bab4e6bd18888"));

        MatcherAssert.assertThat(

                exception.getMessage(),
                StringContains.containsString("""
                        Hash(MD5) <<8888152dfc1637561d6bab4e6bd18888>> not equal to file: files/test_hash.txt"""));
    }

    @Test
    void testHashSha1Pass() {
        seeFileHashSha1Equal(HASH_FILE, "19fcae40ccca465227672dd2fb748bced923f3ca");
    }

    @Test
    void testHashSha256Pass() {
        seeFileHashSha256Equal(HASH_FILE, "fc123f3d794f32cb31cf63808fcd70131211bac27afaba12a47fa48981175d24");
    }

    @Test
    void testHashSha512Pass() {
        seeFileHashSha512Equal(HASH_FILE, "c9849ce0cc3ffaf7266da108480792a54c3bc7e9e1893cf1641ffe4990797ad82033ea34b8df2de3be5eb1f5f962f72093215fb343d8989952dd1612048a6e35");
    }

    @Test
    void testHashNotSupported() {
        Throwable exception = assertThrows(FileReaderException.class, () ->
                seeFileHashAssertSetup(HASH_FILE, "hash123", "MY-ALG"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Not supported algorithm: MY-ALG"));
    }

    @Test
    void testFileSizeCheckPass() {
        String filePath = "temp/sizeCheckPass.txt";
        fileTime.deleteFile(filePath);

        fileTime.createFileWithSize(filePath, 180);

        fileTime.seeFileSizeIsExactly(filePath, 180);
        fileTime.seeFileSizeIsGreaterThan(filePath, 179);
        fileTime.seeFileSizeIsLessThan(filePath, 181);

       long size = fileTime.getFileSize(filePath);

        assertEquals(180, size);
    }

    @Test
    void testFileSizeCheckCatch() {
        String filePath = "temp/sizeCheck.txt";
        fileTime.deleteFile(filePath);


        fileTime.createFileWithSize(filePath, 180);

        Throwable exception1 = assertThrows(AssertionError.class, () ->
                fileTime.seeFileSizeIsExactly(filePath, 170));

        MatcherAssert.assertThat(
                exception1.getMessage(),
                StringContains.containsString("File <temp/sizeCheck.txt> size expected to be EXACTLY <170 bytes> but got <180 bytes> within 400 milliseconds"));

        Throwable exception2 = assertThrows(AssertionError.class, () ->
                fileTime.seeFileSizeIsGreaterThan(filePath, 181));

        MatcherAssert.assertThat(
                exception2.getMessage(),
                StringContains.containsString("File <temp/sizeCheck.txt> size expected to be GREATER <181 bytes> but got <180 bytes> within 400 milliseconds"));

        Throwable exception3 = assertThrows(AssertionError.class, () ->
                fileTime.seeFileSizeIsLessThan(filePath, 180));

        MatcherAssert.assertThat(
                exception3.getMessage(),
                StringContains.containsString("File <temp/sizeCheck.txt> size  expected to be LESS <180 bytes> but got <180 bytes> within 400 milliseconds"));

    }

    @Test
    void testSeeFileContainStringCatch() {


        String filePath = "temp/check.txt";


        fileTime.createFileWithSize(filePath, 101);


        Throwable exception1 = assertThrows(AssertionError.class, () ->
                fileTime.seeFileContainString(filePath, "not exist"));


        MatcherAssert.assertThat(
                exception1.getMessage(),
                StringContains.containsString("FAILED: <<not exist>> expected to be present in file <temp/check.txt> within 400 milliseconds"));

    }

    @Test
    void testFileSizePreCheckCatch() {
        FileHelper file1 = new FileHelper().setMaxFileSize(100);
        FileHelper file2 = new FileHelper().setMaxFileSize(101);

        String filePath = "temp/sizePreCheck.txt";


        file2.createFileWithSize(filePath, 101);


        Throwable exception1 = assertThrows(FileReaderException.class, () ->
                file1.createFileWithSize(filePath, 101));

        MatcherAssert.assertThat(
                exception1.getMessage(),
                StringContains.containsString("Provided size 101 bytes more then provided #maxFileSize(100 bytes) (set or configure more if you need)"));

        Throwable exception2 = assertThrows(FileReaderException.class, () ->
                file1.showDataFromFile(filePath));

        sizeCatch(exception2);

        Throwable exception3 = assertThrows(FileReaderException.class, () ->
                file1.countMatchesInFile(filePath, MESSAGE, false));

        sizeCatch(exception3);

        Throwable exception4 = assertThrows(FileReaderException.class, () ->
                file1.seeFileContainsRegex(filePath, "some.1."));

        sizeCatch(exception4);

        Throwable exception5 = assertThrows(FileReaderException.class, () ->
                file1.seeFileContainString(filePath, "some"));

        sizeCatch(exception5);

        Throwable exception6 = assertThrows(FileReaderException.class, () ->
                file1.seeFileDoesNotContainString(filePath, "some"));

        sizeCatch(exception6);

    }

    private void sizeCatch(Throwable ex){

        MatcherAssert.assertThat(
                ex.getMessage(),
                StringContains.containsString("File <temp/sizePreCheck.txt> size(101 bytes) more then provided #maxFileSize(100 bytes) (set or configure more if you need)"));

    }

}

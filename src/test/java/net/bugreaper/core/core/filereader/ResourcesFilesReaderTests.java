package net.bugreaper.core.core.filereader;

import net.bugreaper.core.exceptions.FileReaderException;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.text.MessageFormat;

import static net.bugreaper.core.filereaders.ResourcesFileReader.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("squid:S2699")
class ResourcesFilesReaderTests {

    @Test
    void testResourceWriteAndReadText() {
        var file = "temp/write_test.tmp";
        var text = """
                [write to
                file]""";

        overwriteTextToResourceFile(file, text);

        var ar = readResourceFile(file);

        assertEquals(text, ar, "File write and read successful");
    }

    @Test
    void testResourceOverWriteAndReadText() {
        var file = "temp/write_test.tmp";
        var text1 = "text12123131";
        var text2 = "text2";

        overwriteTextToResourceFile(file, text1);
        overwriteTextToResourceFile(file, text2);

        var ar = readResourceFile(file);

        assertEquals(text2, ar, "File overwrite and read successful");
    }

    @Test
    void testResourceWriteMultipleTimes() {
        var file = "temp/write_test2.tmp";
        var text1 = "text1";
        var text2 = "text2";

        writeTextToResourceFile(file, text1);
        deleteResourceFile(file);

        writeTextToResourceFile(file, text1);
        writeTextToResourceFile(file, text2);

        var ar = readResourceFile(file);

        assertEquals(text1 + text2, ar, "File write multiple times and read successful");
    }

    @Test
    void testNoFileExistDeleteNoError() {
        var file = "temp/no-error" + System.currentTimeMillis();

        deleteResourceFile(file);
    }


    @Test
    void testResourceWriteAndReadTextWrongDir() {

        String errorMessage = null;
        var file = "wrong/write_test.tmp";
        var text = "logs/test";


        try {
            overwriteTextToResourceFile(file, text);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertEquals(
                "Failed to write file: " + System.getProperty("user.dir") + "/src/test/resources/" + file,
                errorMessage,
                "File write problem exception message: " + errorMessage);
    }

    @Test
    void testResourceReadTextNotExist() {

        String errorMessage = null;
        final String path = "files/test_not_exist.txt";

        try {
           readResourceFile(path);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertEquals(
                "Failed to read file: " + System.getProperty("user.dir") + "/src/test/resources/" + path,
                errorMessage,
                "File read problem exception message: " + errorMessage);
    }

    @Test
    void testGetFileSizeError() {

        String fileName = "temp2/test_size_2.txt";


        Throwable exception = assertThrows(FileReaderException.class, () ->
                getResourceFileSize(fileName));

        assertEquals(
                "Failed to read file: " + System.getProperty("user.dir") + "/src/test/resources/" + fileName,
                exception.getMessage(),
                "Error on get size from not existing file");
    }

    @Test
    void testCreateResourceFileWithSizeError() {

        String fileName = "temp2/test_size_2.txt";


        Throwable exception = assertThrows(FileReaderException.class, () ->
                createResourceFileWithSize(fileName, 10));

        assertEquals(
                "Failed to create file: " + System.getProperty("user.dir") + "/src/test/resources/" + fileName,
                exception.getMessage(),
                "Error on get size from not existing file");
    }

    @Test
    void testFileWithSizeCreateRecreate() {

        String fileName = "temp/test_size_1.txt";
        long expectedBytes = 177;

        createResourceFileWithSize(fileName, expectedBytes);

        assertEquals(
                expectedBytes,
                getResourceFileSize(fileName),
                "File created with right size");

        long expectedBytesNew = 103;

        createResourceFileWithSize(fileName, expectedBytesNew);

        assertEquals(
                expectedBytesNew,
                getResourceFileSize(fileName),
                "File created with right size");
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testDeleteResourceFile() {
        String fileName = "temp/test_for_delete.txt";
        long expectedBytes = 10;

        createResourceFileWithSize(fileName, expectedBytes);

        seeResourceFileExists(fileName);
        seeResourceFileNotEmpty(fileName);

        deleteResourceFile(fileName);

        seeResourceFileNotExists(fileName);
    }

    @Test
    void testCreateEmptyFileFile() {
        String fileName = "temp/test_empty.txt";

        createResourceFileWithSize(fileName, 0);

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                seeResourceFileNotEmpty(fileName));

        assertEquals(
                MessageFormat.format("File {0} is empty", System.getProperty("user.dir") + "/src/test/resources/" + fileName),
                exception.getMessage(),
                "Error on file not empty check");
    }

    @Test
    void testSeeResourceFileExists() {
        String fileName = "temp/not_exist_file.txt";

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                seeResourceFileExists(fileName));

        assertEquals(
                MessageFormat.format("File {0}{1}{2} not exists",  System.getProperty("user.dir"), "/src/test/resources/", fileName),
                exception.getMessage(),
                "Assert catch on is file exists");
    }

    @Test
    void testSeeResourceFileNotExists() {
        String fileName = "files/test1.json";

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                seeResourceFileNotExists(fileName));

        assertEquals(
                MessageFormat.format("File {0}{1}{2} exists",  System.getProperty("user.dir"), "/src/test/resources/", fileName),
                exception.getMessage(),
                "Assert catch on is file not exists");
    }

    @Test
    void testSeeResourceFileNotEmpty() {
        String fileName = "temp/empty_temp.txt";


        createResourceFileWithSize(fileName, 0);

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                seeResourceFileNotEmpty(fileName));

        assertEquals(
                MessageFormat.format("File {0}{1}{2} is empty",  System.getProperty("user.dir"), "/src/test/resources/", fileName),
                exception.getMessage(),
                "Assert catch on is file not exists");
    }

}

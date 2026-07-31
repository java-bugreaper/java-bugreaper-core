package net.bugreaper.core.filereader;


import ch.qos.logback.classic.Level;
import net.bugreaper.core.exceptions.FileReaderException;
import net.bugreaper.core.utils.LogWatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.filereaders.FileReader.readJsonFromFile;
import static net.bugreaper.core.filereaders.FileReader.readTextFromFile;
import static org.junit.jupiter.api.Assertions.*;

class FileReaderNegativeTests {

    private LogWatcher logWatcher;

    @AfterEach
    void tearDown() {
        logWatcher.detach();
    }

    @BeforeEach
    void clean() {
        logWatcher = new LogWatcher("net.bugreaper.core", Level.DEBUG);
    }


    @Test
    void testReadNotTypeJson() {

        String errorMessage = null;
        final String path = "files/test2.yml";

        try {
            readJsonFromFile(path);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertEquals("File is not a valid JSON file: " + path,
                errorMessage, "File not JSON exception message: " + errorMessage);
    }

    @Test
    void testReadTypeJsonNotExist() {

        String errorMessage = null;
        final String path = "files/not_exist.json";

        try {
            readJsonFromFile(path);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertEquals("File does not exist in resources: " + path,
                errorMessage);

        assertEquals(
                "[[INFO] Full path to project: %s]"
                        .formatted(System.getProperty("user.dir")),
                logWatcher.getLoggedEvents(Level.INFO).toString());
    }

    @Test
    void testReadTypeJsonCantRead() {

        final String path = "files";

        Throwable exception = assertThrows(FileReaderException.class, () ->
                readJsonFromFile(path));

        assertEquals(
                "Failed to read resource(classpath) file: files",
                exception.getMessage());
    }


    @Test
    void testReadNotExistJson() {

        String errorMessage = null;
        final String path = "files/test_not_exist.txt";

        try {
            readJsonFromFile(path);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertEquals("File does not exist in resources: " + path,
                errorMessage, "File absent exception message: " + errorMessage);
    }

    @Test
    void testReadTextNotExist() {

        final String path = "files/test_not_exist.txt";

        Throwable exception = assertThrows(FileReaderException.class, () ->
                readTextFromFile(path));

        assertEquals(
                "File does not exist in resources: files/test_not_exist.txt",
                exception.getMessage());
    }

    @Test
    void testReadTextNotFile() {

        final String path = "files";

        Throwable exception = assertThrows(FileReaderException.class, () ->
                readTextFromFile(path));

        assertEquals(
                "Failed to read resource(classpath) file: files",
                exception.getMessage());
    }

    @Test
    void testReadJsonNotExist() {


        final String path = "files/test_not_exist.txt";

        Throwable exception = assertThrows(FileReaderException.class, () ->
                readJsonFromFile(path));

        assertEquals(
                "File does not exist in resources: files/test_not_exist.txt",
                exception.getMessage());
    }
}

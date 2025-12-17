package net.bugreaper.core.core.filereader;


import net.bugreaper.core.exceptions.FileReaderException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.filereaders.FileReader.readJsonFromFile;
import static net.bugreaper.core.filereaders.FileReader.readTextFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileReaderNegativeTests {


    @Test
    void testReadNotTypeJson() {

        String errorMessage = null;
        final String path = "files/test2.yml";

        try {
            readJsonFromFile(path);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertEquals("File not JSON type: " + path,
                errorMessage, "File not JSON exception message: " + errorMessage);
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

        assertEquals("File not exist in resources: " + path,
                errorMessage, "File absent exception message: " + errorMessage);
    }

    @Test
    void testReadTextNotExist() {

        final String path = "files/test_not_exist.txt";

        Throwable exception = assertThrows(FileReaderException.class, () ->
                readTextFromFile(path));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("File not exist in resources: files/test_not_exist.txt"));

    }

    @Test
    void testReadJsonNotExist() {


        final String path = "files/test_not_exist.txt";

        Throwable exception = assertThrows(FileReaderException.class, () ->
                readJsonFromFile(path));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("File not exist in resources: files/test_not_exist.txt"));

    }
}

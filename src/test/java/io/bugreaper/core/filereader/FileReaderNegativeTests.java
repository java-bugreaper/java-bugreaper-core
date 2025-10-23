package io.bugreaper.core.filereader;


import org.junit.jupiter.api.Test;

import static io.bugreaper.core.filereaders.FileReader.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals("Can't find file in resources: " + path,
                errorMessage, "File absent exception message: " + errorMessage);
    }

    @Test
    void testReadTextNotExist() {

        String errorMessage = null;
        final String path = "files/test_not_exist.txt";

        try {
            readTextFromFile(path);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertEquals("Some problem with: " + path,
                errorMessage, "File problem exception message: " + errorMessage);

    }


}

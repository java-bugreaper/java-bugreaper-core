package io.bugreaper.core.filereader;


import org.junit.jupiter.api.Test;

import static io.bugreaper.core.filereaders.FileReader.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileReaderTests {

    @Test
    void testReadJson() {

        var er = """
                {
                  "id": 1,
                  "text": "test"
                }""";
        var ar = readJsonFromFile("files/test1.json");

        assertEquals(er, ar, "JSON file read successful");
    }

    @Test
    void testReadJsonArray() {

        var er = """
                [{
                  "id": 1,
                  "text": "test"
                }]""";
        var ar = readJsonFromFile("files/testArray.json");

        assertEquals(er, ar, "JSON array file read successful");
    }

    @Test
    void testReadText() {

        var er = "test: 2";
        var ar = readTextFromFile("files/test2.yml");

        assertEquals(er, ar, "File read successful");
    }

}

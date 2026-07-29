package net.bugreaper.core.assertable.list;


import net.bugreaper.core.assertable.AssertableStringList;
import net.bugreaper.core.exceptions.FileReaderException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("squid:S2699")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssertableListNegativeTests {

    AssertableStringList data = new AssertableStringList(new ArrayList<>());

    String brokenJson = "dummy";
    Path notJsonFilePath = Path.of("files/test.txt");
    Path emptyFilePath = Path.of("files/empty.txt");
    Path notExistsFilePath = Path.of("files/not_exists.txt");

    String wrongJsonMessage = """
            Invalid JSON or JSON array format (strict):
            dummy""";

    //  provided data

    @Test
    void seeListAnyContainsJsonSubsetProvideNotJsonTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                data.seeListAnyContainsJsonSubset(brokenJson));

        assertEquals(wrongJsonMessage,
                exception.getMessage());
    }

    @Test
    void seeListAnyContainsExtendedJsonProvideNotJsonTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                data.seeListAnyContainsExtendedJson(brokenJson));

        assertEquals(wrongJsonMessage,
                exception.getMessage());
    }

    @Test
    void seeListAnyContainsJsonProvideNotJsonTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                data.seeListAnyContainsJson(brokenJson));

        assertEquals(wrongJsonMessage,
                exception.getMessage());
    }

    @Test
    void seeListAnyEqualsJsonProvideNotJsonTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                data.seeListAnyEqualsJson(brokenJson));

        assertEquals(wrongJsonMessage,
                exception.getMessage());
    }

    @Test
    void seeListAnyJsonMatchSchemaProvideNotJsonTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                data.seeListAnyJsonMatchSchema(brokenJson));

        assertEquals(wrongJsonMessage,
                exception.getMessage());
    }

    //  data from file

    @Test
    void seeListAnyContainsJsonProvideNotJsonFileTest() {

        Throwable exception = assertThrows(FileReaderException.class, () ->
                data.seeListAnyContainsJson(notJsonFilePath));

        assertEquals("File is not a valid JSON file: files/test.txt",
                exception.getMessage());
    }

    @Test
    void seeListAnyContainsJsonProvideNotExistsTest() {

        Throwable exception = assertThrows(FileReaderException.class, () ->
                data.seeListAnyContainsJson(notExistsFilePath));

        assertEquals("File does not exist in resources: files/not_exists.txt",
                exception.getMessage());
    }


    @Test
    void seeListAnyEqualsJsonProvideNotJsonFileTest() {

        Throwable exception = assertThrows(FileReaderException.class, () ->
                data.seeListAnyEqualsJson(notJsonFilePath));

        assertEquals("File is not a valid JSON file: files/test.txt",
                exception.getMessage());
    }

    @Test
    void seeListAnyEqualsJsonProvideEmptyFileTest() {

        Throwable exception = assertThrows(FileReaderException.class, () ->
                data.seeListAnyEqualsJson(emptyFilePath));

        assertEquals("File is not a valid JSON file: files/empty.txt",
                exception.getMessage());
    }

    @Test
    void seeListAnyEqualsJsonProvideNotExistsTest() {

        Throwable exception = assertThrows(FileReaderException.class, () ->
                data.seeListAnyEqualsJson(notExistsFilePath));

        assertEquals("File does not exist in resources: files/not_exists.txt",
                exception.getMessage());
    }

    @Test
    void seeListAnyJsonMatchSchemaProvideNotJsonFileTest() {

        Throwable exception = assertThrows(FileReaderException.class, () ->
                data.seeListAnyJsonMatchSchema(notJsonFilePath));

        assertEquals("File is not a valid JSON file: files/test.txt",
                exception.getMessage());
    }

}

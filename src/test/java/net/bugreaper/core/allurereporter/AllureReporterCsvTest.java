package net.bugreaper.core.allurereporter;

import org.junit.jupiter.api.Test;

import static net.bugreaper.core.allurereporter.AllureBuilder.reportHtmlCsvCases;
import static net.bugreaper.core.filereaders.FileReader.readTextFromFile;
import static net.bugreaper.core.filereaders.FileReader.readCsvToArray;
import static net.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AllureReporterCsvTest {

    @Test
    void testCsvReaderBasic() {
        var file = "files/test1.csv";

        assertEquals(
                readTextFromFile("testdata/allure/csv_test1.html"),
                reportHtmlCsvCases("csv_tests_1", readCsvToArray(file)),
                "Check csv array from file" );

    }

    @Test
    void testCsvReaderEmptyCellsRowsAndComas() {
        var file = "files/test2.csv";

        assertEquals(
                readResourceFile("testdata/allure/csv_test2.html"),
                reportHtmlCsvCases("csv_tests_2", readCsvToArray(file)),
                "Check csv array from file with hard data" );

    }

    @Test
    void testCsvReaderWithLongText() {
        var file = "files/test3.csv";

        assertEquals(
                readResourceFile("testdata/allure/csv_test3.html"),
                reportHtmlCsvCases("csv_tests_3", readCsvToArray(file)),
                "Check csv array with long text" );

    }

    @Test
    void testCsvReaderWithAccent() {
        var file = "files/test4.csv";

        assertEquals(
                readResourceFile("testdata/allure/csv_test4.html"),
                reportHtmlCsvCases("csv_tests_4", readCsvToArray(file)),
                "Check csv array from file with accent" );

    }

    @Test
    void testCsvReaderWithEmptyColumn() {
        var file = "files/test5.csv";

        assertEquals(
                readTextFromFile("testdata/allure/csv_test5.html"),
                reportHtmlCsvCases("csv_tests_5", readCsvToArray(file)),
                "Check csv array from file" );
    }

    @Test
    void testCsvReaderNotExist() {

        String errorMessage = null;
        var file = "files/csv_not_exist.csv";

        try {
            reportHtmlCsvCases("error", readCsvToArray(file));
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertEquals(
                "File does not exist in resources: " + file,
                errorMessage,
                "File not found error: " + errorMessage);
    }

}

package net.bugreaper.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import net.bugreaper.core.exceptions.AllureValidatorException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.bugreaper.core.allurereporter.AllureReporter.attachFromCsv;
import static net.bugreaper.core.filereaders.FileReader.readTextFromFile;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("java:S2699")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AllureUtilsTests {

    @BeforeAll
    static void cleanAllure(){
        AllureResultLoader.cleanResultsDir();
    }

    @Test
    void utilityAllureStepsValidatorClass() throws NoSuchMethodException {
        Constructor<AllureStepsValidator> constructor = AllureStepsValidator.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assert (cause instanceof IllegalStateException);
        assert ("Utility class".equals(cause.getMessage()));
    }

    @Test
    void utilityAllureResultLoaderClass() throws NoSuchMethodException {
        Constructor<AllureResultLoader> constructor = AllureResultLoader.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assert (cause instanceof IllegalStateException);
        assert ("Utility class".equals(cause.getMessage()));
    }

    @Test
    void resultNotFoundTest() {

        Throwable exception = assertThrows(AllureValidatorException.class, () ->
                AllureResultLoader.loadByTestName("notExistTest"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("No value present"));
    }

    @Test
    void cleanResultsDirThrowsWhenFileCannotBeDeleted(@TempDir Path tempDir) throws IOException {
        Path resultsDir = tempDir.resolve("allure-results");
        Path lockedDir = resultsDir.resolve("locked");
        Files.createDirectories(lockedDir);

        File file = lockedDir.resolve("result.json").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{}");
        }

        File directory = lockedDir.toFile();
        String originalResultsDirectory = System.getProperty("allure.results.directory");

        try {
            System.setProperty("allure.results.directory", resultsDir.toString());
            assertTrue(directory.setWritable(false, false));
            assumeTrue(deleteIsBlocked(lockedDir.resolve("result.json")),
                    "Filesystem allows deleting from a non-writable directory");

            Throwable exception = assertThrows(AllureValidatorException.class,
                    AllureResultLoader::cleanResultsDir);

            MatcherAssert.assertThat(
                    exception.getMessage(),
                    StringContains.containsString("Failed to delete"));
        } finally {
            directory.setWritable(true, false);
            restoreAllureResultsDirectory(originalResultsDirectory);
        }
    }

    private static boolean deleteIsBlocked(Path path) {
        try {
            Files.delete(path);
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    @Test
    void cleanResultsDirThrowsWhenDirectoryCannotBeCreated(@TempDir Path tempDir) throws IOException {
        Path blocker = tempDir.resolve("not-directory");
        try (FileWriter writer = new FileWriter(blocker.toFile())) {
            writer.write("blocker");
        }

        String originalResultsDirectory = System.getProperty("allure.results.directory");

        try {
            System.setProperty("allure.results.directory",
                    blocker.resolve("allure-results").toString());

            Throwable exception = assertThrows(AllureValidatorException.class,
                    AllureResultLoader::cleanResultsDir);

            MatcherAssert.assertThat(
                    exception.getMessage(),
                    StringContains.containsString("Failed to clean Allure results directory"));
        } finally {
            restoreAllureResultsDirectory(originalResultsDirectory);
        }
    }

    private static void restoreAllureResultsDirectory(String originalResultsDirectory) {
        if (originalResultsDirectory == null) {
            System.clearProperty("allure.results.directory");
        } else {
            System.setProperty("allure.results.directory", originalResultsDirectory);
        }
    }

    //

    @Test
    @Order(1)
    void createResultTest() {
        step();
    }

    private void step(){
        Allure.step("Step1");
    }

    //

    @Test
    @Order(2)
    void checkResultTest() {
        JsonNode result = AllureResultLoader.loadByTestName("createResultTest");

        AllureAssert.assertThat(result)
                .hasStep("Step1");

        var test1 =AllureAssert.assertThat(result);
        Throwable exception = assertThrows(AssertionError.class, () ->
                test1.hasStep("Step2"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        Step not found: 'Step2'
                        Available steps:
                        - Step1"""));

        var test2 =AllureAssert.assertThat(result)
                .hasStep("Step1");

        Throwable exception2 = assertThrows(AssertionError.class, () ->
                test2.hasAttachment("test"));

        MatcherAssert.assertThat(
                exception2.getMessage(),
                StringContains.containsString("Attachment not found: test"));

        Throwable exception3 = assertThrows(AssertionError.class, () ->
                test2.hasAttachment("test","some content"));

        MatcherAssert.assertThat(
                exception3.getMessage(),
                StringContains.containsString("Attachment not found: test"));
    }

    //

    @Test
    @Order(3)
    void createResultTest2() {
        stepInStep();
    }

    @Step("Global step")
    private void stepInStep() {

        Allure.addAttachment("test-att", "message");
        Allure.step("SubStep1");
        Allure.step("SubStep1a");
    }

    //

    @Test
    @Order(4)
    void checkResultTest2() {
        JsonNode result = AllureResultLoader.loadByTestName("createResultTest2");

        AllureAssert.assertThat(result)
                .hasStep("Global step")
                .hasAttachment("test-att")
                .hasAttachment("test-att", "message")
                .hasSubStepLeft("SubStep1")
                .hasSubStepLeft("SubStep1a");

        var test1 = AllureAssert.assertThat(result)
                .hasStep("Global step");

        Throwable exception = assertThrows(AssertionError.class, () ->
                test1.hasSubStep("SubStep2"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        Sub-step not found: 'SubStep2'
                        Available sub-steps:
                        - SubStep1"""));

    }

    @Test
    @Order(5)
    void createCsvTest() {
        Allure.step("Step with CSV",
                () -> attachFromCsv("my csv:",  "files/test5.csv")
        );
    }

    @Test
    @Order(6)
    void checkCsvTest() {
        JsonNode result = AllureResultLoader.loadByTestName("createCsvTest");

        AllureAssert.assertThat(result)
                .hasStep("Step with CSV")
                .hasAttachment("my csv:", readTextFromFile("testdata/allure/csv_test5.html"));


    }

}

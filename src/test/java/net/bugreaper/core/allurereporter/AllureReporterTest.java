package net.bugreaper.core.allurereporter;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import net.bugreaper.core.utils.AllureAssert;
import net.bugreaper.core.utils.AllureResultLoader;
import net.bugreaper.core.utils.LogWatcher;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bugreaper.core.allurereporter.AllureReporter.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SuppressWarnings("squid:S2699")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AllureReporterTest {

    @BeforeAll
    static void cleanReport() {
        AllureResultLoader.cleanResultsDir();
    }

    private LogWatcher logWatcher;
    @BeforeEach
    void setup() {
        logWatcher = new LogWatcher("net.bugreaper.core.allurereporter.AllureReporter", Level.DEBUG);
    }

    @AfterEach
    void teardown() {
        logWatcher.detach();
    }


    @Test
    void testReporter() {
        String expectedLog = "=====test=====";

        reporter("test");

        assertThat(
                "Check Actual list log table",
                logWatcher.getLoggedEvents(Level.INFO).toString(),
                StringContains.containsString(expectedLog));
    }

    @Test
    void testBasicAllureWork() {
        String type = "text/plain";
        ArrayList<String> actualList = new ArrayList<>();

        addStepAttachment("text", "some data");

        attachFromFile("description message", "files/test.txt");
        attachFromFile("description message", type, "files/test.txt");
        attachFromFileNoStep("description message", "files/test.txt");

        attachJson("text", "{}");
        attachFromList("name_2", actualList);

        attachCanBeNull("name_1", null);
        attachCanBeNull("name_1", "not_null");

    }

    @Test
    void attachObjectNullTest() {
        runAttachObj(null);
        assertEquals(
                "[[DEBUG] Data <attach_name>: null]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
    }


    @Test
    @Order(1)
    void attachObjectStringTest() {
        runAttachObj("text");
        assertEquals(
                "[[DEBUG] Data <attach_name> type=String: text]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
    }

    @Test
    @Order(2)
    void checkResultTest2() {
        JsonNode result = AllureResultLoader.loadByTestName("attachObjectStringTest");

        AllureAssert.assertThat(result)
                .hasStep("Step1")
                .hasAttachment("attach_name type=String:", "text");
    }

    @Test
    void attachObjectBoolTest() {
        runAttachObj(true);
        assertEquals(
                "[[DEBUG] Data <attach_name> type=Boolean: true]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
    }

    @Test
    void attachObjectDecimalTest() {
        runAttachObj(22.33d);
        assertEquals(
                "[[DEBUG] Data <attach_name> type=Other: 22.33]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
    }

    @Test
    void attachObjectListTest() {
        List<String> list = new ArrayList<>(Arrays.asList("one", "two"));

        runAttachObj(list);
        assertEquals(
                "[[DEBUG] Data <attach_name> type=Array: [one, two]]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
    }

    @Test
    void attachObjectFloatTest() {
        runAttachObj(22.44f);
        assertEquals(
                "[[DEBUG] Data <attach_name> type=Float: 22.44]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
    }

    @Test
    void attachObjectIntTest() {
        runAttachObj(22);
        assertEquals(
                "[[DEBUG] Data <attach_name> type=Integer: 22]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
    }

    @Test
    void attachObjectLongTest() {
        runAttachObj(1000L);
        assertEquals(
                "[[DEBUG] Data <attach_name> type=Long: 1000]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
    }

    private void runAttachObj(Object value) {
        Allure.step("Step1", () -> {
            attachObject("attach_name", value);
        });
    }

}

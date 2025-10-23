package io.bugreaper.core.allurereporter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import helpers.MemoryAppender;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static io.bugreaper.core.allurereporter.AllureReporter.*;
import static org.hamcrest.MatcherAssert.assertThat;


@SuppressWarnings("squid:S2699")
class AllureReporterTest {

    private static final MemoryAppender memoryAppender = new MemoryAppender();
    private static final  String LOGGER_NAME = "io.bugreaper.core.allurereporter.AllureReporter";


    @Test
    void testReporter() {
        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);

        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        memoryAppender.start();
        String expectedLog = "=====test=====";

        reporter("test");

        assertThat(
                "Check Actual list log table",
                memoryAppender.getLoggedEvents().toString(),
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


}

package net.bugreaper.core.utils;

import ch.qos.logback.classic.Level;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoreLoggingTest {

    private static final String LOGGER_NAME = "net.bugreaper.core";

    private LogWatcher logWatcher;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LOGGER_NAME);


    @BeforeEach
    void setup() {
        logWatcher = new LogWatcher(LOGGER_NAME, Level.DEBUG);
    }

    @AfterEach
    void teardown() {
        logWatcher.detach();
    }

    @Test
    void testDifferentLevels() {
        logger.debug("Debug String1");
        logger.info("Info String");
        logger.debug("Debug String2");


        MatcherAssert.assertThat(
                logWatcher.getLoggedEvents(Level.DEBUG).toString(),
                StringContains.containsString("Debug String1"));

        assertEquals(
                "[[DEBUG] Debug String1, [DEBUG] Debug String2]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString(),
                "check debug logs");


        assertEquals(
                "[[INFO] Info String]",
                logWatcher.getLoggedEvents(Level.INFO).toString(),
                "check info logs");

        assertEquals(
                "[[INFO] Info String]",
                logWatcher.getLoggedEvents(Level.INFO).toString(),
                "check info logs");

        assertEquals(
                "[[DEBUG] Debug String1, [INFO] Info String, [DEBUG] Debug String2]",
                logWatcher.getLoggedEvents().toString(),
                "check all logs");

        assertEquals(
                3,
                logWatcher.countAll(),
                "All logs count");

        assertEquals(
                2,
                logWatcher.countByLevel(Level.DEBUG),
                "DEBUG logs count");


        logWatcher.clear();

        assertEquals(
                0,
                logWatcher.countByLevel(Level.DEBUG),
                "DEBUG logs count");
    }

    @Test
    void testEmptyLogs() {

        assertEquals(
                "[]",
                logWatcher.getLoggedEvents(Level.INFO).toString(),
                "check info logs");

        assertEquals(
                0,
                logWatcher.countByLevel(Level.DEBUG),
                "DEBUG logs count");
    }

}

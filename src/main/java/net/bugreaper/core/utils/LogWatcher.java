package net.bugreaper.core.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A test utility class designed to intercept and verify log messages in memory during Unit testing.
 * <p>
 * This class automatically registers itself as an appender for the target logger. It eliminates
 * the need to parse physical log files or console outputs during verification assertions.
 * </p>
 *
 * <p><b>JUnit 5 Example Usage:</b></p>
 * <pre>{@code
 * private LogWatcher logWatcher;
 *
 * @BeforeEach
 * void setup() {
 *     logWatcher = new LogWatcher("net.bugreaper.core", Level.DEBUG);
 * }
 *
 * @AfterEach
 * void teardown() {
 *     logWatcher.detach();
 * }
 *
 * @Test
 * void test() {
 *
 *     // do something to create log
 *     logger.info("Info String");
 *
 *     assertEquals(1, logWatcher.countByLevel(Level.INFO));
 *
 *     assertEquals(
 *             "[[INFO] Info String]",
 *             logWatcher.getLoggedEvents(Level.INFO).toString());
 * }
 * }</pre>
 */
public class LogWatcher extends ListAppender<ILoggingEvent> {

    /**
     * The reference to the Logback Logger instance this watcher is attached to.
     */
    private final Logger testLogger;

    /**
     * Constructs a new {@code LogWatcher}, adjusts the threshold level for the targeted logger,
     * and automatically starts capturing all logging events.
     *
     * @param loggerName the fully qualified name of the logger to monitor (e.g., "net.bugreaper.core")
     * @param level      the minimum log level threshold that the target logger should forward
     *                   to this appender (e.g., {@link Level#DEBUG})
     */
    public LogWatcher(String loggerName, Level level) {
        this.testLogger = (Logger) LoggerFactory.getLogger(loggerName);
        this.testLogger.setLevel(level);

        // Bind the appender to the current Logback context and activate it
        this.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        this.start();

        // Attach this appender instance to the target logger configuration
        this.testLogger.addAppender(this);
    }

    /**
     * Retrieves an unmodifiable, read-only list of all log events captured since the initialization
     * of this watcher or the last list clearance.
     *
     * @return a {@link List} containing {@link ILoggingEvent} objects representing all intercepted logs
     */
    public List<ILoggingEvent> getLoggedEvents() {
        return Collections.unmodifiableList(this.list);
    }

    /**
     * Filters the captured logs and returns only the events matching the specifically provided logging level.
     * <p>
     * This is useful for testing scenarios where you need to independently verify the text presence
     * of INFO statements and the total absence of ERROR events within a single test execution.
     * </p>
     *
     * @param level the specific log level threshold to filter by (e.g., {@link Level#INFO})
     * @return an unmodifiable filtered list of matching logging events
     */
    public List<ILoggingEvent> getLoggedEvents(Level level) {
        return this.list.stream()
                .filter(event -> event.getLevel().equals(level))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    /**
     * Returns the total count of all logging events intercepted by this watcher.
     *
     * @return the total number of captured log events
     */
    public int countAll() {
        return this.list.size();
    }

    /**
     * Counts the number of captured logging events that match the specified logging level.
     * <p>
     * This is useful for asserting that a specific number of errors or warnings
     * occurred during an operation.
     * </p>
     *
     * @param level the log level to match and count (e.g., {@link Level#ERROR})
     * @return the total count of captured log events matching the specified level
     */
    public long countByLevel(Level level) {
        return this.list.stream()
                .filter(event -> event.getLevel().equals(level))
                .count();
    }

    /**
     * Clears all currently accumulated logging events from the internal memory buffer.
     * <p>
     * This method can be used mid-test to reset the log history before starting a new step
     * or phase within the same test execution. Note that the watcher remains attached
     * to the logger and will continue capturing new logs after this call.
     * </p>
     */
    public void clear() {
        this.list.clear();
    }

    /**
     * Detaches this {@code LogWatcher} from the targeted logger configuration and stops processing events.
     * <p>
     * <b>Critical Requirement:</b> This method must be explicitly called at the end of every test execution
     * (e.g., within an {@code @AfterEach} lifecycle block) to avoid test cross-pollution, duplicate logging
     * capture side effects, and application context memory leaks.
     * </p>
     */
    public void detach() {
        if (testLogger != null) {
            testLogger.detachAppender(this);
        }
        this.stop();
    }
}
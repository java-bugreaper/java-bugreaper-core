package net.bugreaper.modules.filehelper.interfaces;


import net.bugreaper.modules.filehelper.LogHelper;

public interface LogHelperInt {

    /**
     * Configures the global await timeout for assertions and operations that use await.
     *
     * @param awaitMs await timeout in milliseconds
     * @return this
     * @throws IllegalArgumentException if the provided timeout is invalid or less than 200 milliseconds
     */
    LogHelper setAwaitMs(int awaitMs);


    /**
     * Creates a helper with the specified log file path.
     *
     * @param logFilePath path to the log file in <b>test</b> resources (example: {@code logs/server/logs.log})
     * @return this
     * @throws IllegalArgumentException if the log file path is null or empty
     */
    LogHelper setLogFile(String logFilePath);

    /**
     * Clears all content from the log file.
     *
     */
    void cleanLogs();

    /**
     * Appends text to the end of the log file.
     *
     * @param message text to append
     */
    void addToLogs(String message);

    /**
     * Adds the log file content as an Allure attachment.
     *
     */
    void showDataFromLogs();

    /**
     * Returns the number of times the text occurs in the log file.
     *
     * @param expectedText expected text or regular expression
     * @param regex        whether the expected text should be treated as a regular expression
     * @return number of occurrences in the log file
     */
    int countInLogs(String expectedText, boolean regex);

    /**
     * Asserts that the log file contains text matching the specified regular expression.
     * <p><b>Uses await.</b></p>
     *
     * @param expectedText expected text with regex
     * @throws AssertionError if the assertion fails
     */
    void seeLogsContainsRegex(String expectedText);

    /**
     * Asserts that the log file contains the specified text (not a regular expression).
     * <p><b>Uses await.</b></p>
     *
     * @param expectedText expected text
     * @throws AssertionError if the assertion fails
     */
    void seeLogsContainString(String expectedText);

    /**
     * Asserts that the file does not contain the specified text (not a regular expression).
     *
     * @param unexpectedText text that must not be present
     * @throws AssertionError if the assertion fails
     */
    void seeLogsDoesNotContainString(String unexpectedText);

    /**
     * Returns and logs (at INFO level) a human-readable summary of all resolved
     * configuration values.
     * <p>
     * The summary includes values loaded from the YAML configuration file as well as
     * any fields overridden programmatically after construction. Optional fields that
     * were not present in the configuration and resolved via default values may also
     * be included.
     *
     * @return String with summary
     */
    String getConfigSummary();

}

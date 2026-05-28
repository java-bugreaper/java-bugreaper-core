package net.bugreaper.modules.filehelper.interfaces;


import net.bugreaper.modules.filehelper.LogHelper;

public interface LogHelperInt {

    /**
     * Configure await in asserts with await
     *
     * @param awaitMs ms await
     * @return this
     * @throws IllegalArgumentException on invalid setup
     */
    LogHelper setAwaitMs(int awaitMs);

    /**
     * Configure log file for interaction
     *
     * @param logFilePath log file in test resources
     * @return this
     * @throws IllegalArgumentException on invalid setup
     */
    LogHelper setLogfile(String logFilePath);

    /**
     * Delete all data from file
     *
     */
    void cleanLogs();

    /**
     * Add text to file
     *
     * @param message text to add
     */
    void addToLogs(String message);

    /**
     * Show all data from log file in Allure attach
     *
     */
    void showDataFromLogs();

    /**
     * Get expected text count from log file
     *
     * @param expectedText expected text
     * @param regex true: use regex, false: strict string
     * @return int with count
     */
    int countInLogs(String expectedText, boolean regex);

    /**
     * Assert that log file contains expected text(by regex)
     * <p><b>with await</b>
     *
     * @param expectedText expected text with regex
     * @throws AssertionError on assert fail
     */
    void seeLogsContainsRegex(String expectedText);

    /**
     * Assert that log file contains expected text(exactly - not regex)
     * <p><b>with await</b>
     *
     * @param expectedText expected text
     * @throws AssertionError on assert fail
     */
    void seeLogsContainString(String expectedText);

    /**
     * Assert that log file not contains expected text(exactly)
     *
     * @param unexpectedText unexpected text
     * @throws AssertionError on assert fail
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

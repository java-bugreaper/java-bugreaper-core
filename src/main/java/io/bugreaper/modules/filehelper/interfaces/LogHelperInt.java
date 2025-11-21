package io.bugreaper.modules.filehelper.interfaces;


import io.bugreaper.modules.filehelper.LogHelper;
import org.awaitility.core.ConditionTimeoutException;

public interface LogHelperInt {

    /**
     * Configure await in asserts with await
     *
     * @param awaitMs ms await
     * @return this
     * @throws IllegalArgumentException on invalid setup
     */
    LogHelper withAwaitMs(int awaitMs);

    /**
     * Configure log file for interaction
     *
     * @param logFilePath log file in test resources
     * @return this
     * @throws IllegalArgumentException on invalid setup
     */
    LogHelper withLogfile(String logFilePath);

    /**
     * Truncate file with logs
     *
     */
    void cleanLogs();

    /**
     * Add text to file
     *
     * @param message text to add
     */
    void addToLogs( String message);

    /**
     * Show all data from log file in Allure attach
     *
     */
    void showDataFromLogs();

    /**
     * Get expected text count from log file
     *
     * @param expectedText expected text
     * @return int with count
     */
    int countInLogs( String expectedText, boolean regex);

    /**
     * Assert that log file contains expected text(by regex) with await
     *
     * @param expectedText expected text with regex
     * @throws ConditionTimeoutException on assert fail
     */
    void seeLogsContainsRegex(String expectedText);

    /**
     * Assert that log file contains expected text(exactly - not regex) with await
     *
     * @param expectedText expected text
     * @throws ConditionTimeoutException on assert fail
     */
    void seeLogsContainString(String expectedText);

    /**
     * Assert that log file not contains expected text(exactly)
     *
     * @param unexpectedText unexpected text
     * @throws ConditionTimeoutException on assert fail
     */
    void seeLogsDoesNotContainString(String unexpectedText);

}

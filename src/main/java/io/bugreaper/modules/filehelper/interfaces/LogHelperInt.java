package io.bugreaper.modules.filehelper.interfaces;


import org.awaitility.core.ConditionTimeoutException;

public interface LogHelperInt {

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

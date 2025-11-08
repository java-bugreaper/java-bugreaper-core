package io.bugreaper.modules.filehelper.interfaces;


import org.awaitility.core.ConditionTimeoutException;

public interface FileHelperInt {

    /**
     * Truncate file
     *
     * @param filePath file path/name in test resources
     */
    void cleanFile(String filePath);

    /**
     * Add text to file
     *
     * @param filePath file path/name in test resources
     * @param message text to add
     */
    void addToFile(String filePath, String message);

    /**
     * Show all data from file in Allure attach
     *
     * @param filePath file path/name in test resources
     */
    void showDataFromFile(String filePath);

    /**
     * Get expected text count from file
     *
     * @param filePath      file path and name in test resources
     * @param expectedText expected text
     * @return int with count
     */
    int countMatchesInFile(String filePath, String expectedText, boolean regex);

    /**
     * Assert that file contains expected text(by regex)
     *
     * @param filePath      file path/name in test resources
     * @param expectedText expected text with regex
     * @throws ConditionTimeoutException on assert fail
     */
    void seeFileContainsRegex(String filePath, String expectedText);

    /**
     * Assert that file contains expected text(exactly - not regex)
     *
     * @param filePath      file path/name in test resources
     * @param expectedText expected text
     * @throws ConditionTimeoutException on assert fail
     */
    void seeFileContainString(String filePath, String expectedText);

    /**
     * Assert that file not contains expected text(exactly)
     *
     * @param filePath        file path/name in test resources
     * @param unexpectedText unexpected text
     * @throws ConditionTimeoutException on assert fail
     */
    void seeFileDoesNotContainString(String filePath, String unexpectedText);

}

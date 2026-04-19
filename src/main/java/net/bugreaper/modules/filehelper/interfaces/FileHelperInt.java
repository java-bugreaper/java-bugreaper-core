package net.bugreaper.modules.filehelper.interfaces;


import net.bugreaper.modules.filehelper.FileHelper;

public interface FileHelperInt {

    /**
     * Configure await in asserts with await
     *
     * @param awaitMs ms await
     * @return this
     * @throws IllegalArgumentException on invalid setup
     */
    FileHelper setAwaitMs(int awaitMs);

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
     * @param message  text to add
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
     * @param filePath     file path and name in test resources
     * @param expectedText expected text
     * @return int with count
     */
    int countMatchesInFile(String filePath, String expectedText, boolean regex);

    /**
     * Assert that file contains expected text(by regex)
     * <p><b>with await</b>
     *
     * @param filePath     file path/name in test resources
     * @param expectedText expected text with regex
     * @throws AssertionError on assert fail
     */
    void seeFileContainsRegex(String filePath, String expectedText);

    /**
     * Assert that file contains expected text(exactly - not regex)
     * <p><b>with await</b>
     *
     * @param filePath     file path/name in test resources
     * @param expectedText expected text
     * @throws AssertionError on assert fail
     */
    void seeFileContainString(String filePath, String expectedText);

    /**
     * Assert that file not contains expected text(exactly)
     *
     * @param filePath       file path/name in test resources
     * @param unexpectedText unexpected text
     * @throws AssertionError on assert fail
     */
    void seeFileDoesNotContainString(String filePath, String unexpectedText);

    /**
     * Assert that file hash in MD5 equal to expected hash
     *
     * @param filePath     file path/name in test resources
     * @param expectedHash expected hash in MD5
     * @throws AssertionError on assert fail
     */
    void seeFileHashMd5Equal(String filePath, String expectedHash);

    /**
     * Assert that file hash in SHA-1 equal to expected hash
     *
     * @param filePath     file path/name in test resources
     * @param expectedHash expected hash in SHA-1
     * @throws AssertionError on assert fail
     */
    void seeFileHashSha1Equal(String filePath, String expectedHash);

    /**
     * Assert that file hash in SHA-256 equal to expected hash
     *
     * @param filePath     file path/name in test resources
     * @param expectedHash expected hash in SHA-256
     * @throws AssertionError on assert fail
     */
    void seeFileHashSha256Equal(String filePath, String expectedHash);

    /**
     * Assert that file hash in SHA-512 equal to provided hash
     *
     * @param filePath     file path/name in test resources
     * @param expectedHash expected hash in SHA-512
     * @throws AssertionError on assert fail
     */
    void seeFileHashSha512Equal(String filePath, String expectedHash);

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

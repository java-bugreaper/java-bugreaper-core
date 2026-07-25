package net.bugreaper.modules.filehelper.interfaces;


import net.bugreaper.modules.filehelper.FileHelper;

public interface FileHelperInt {


    /**
     * Delete all data from file
     * <p>create file if not exist</p>
     *
     * @param filePath file path/name in test resources
     */
    void cleanFile(String filePath);

    /**
     * Delete file
     * <p>not cause error if ile not exist, only WARN</p>
     *
     * @param filePath file path/name in test resources
     */
    void deleteFile(String filePath);

    /**
     * Add text to end of file
     * <p>create file if not exist</p>
     *
     * @param filePath file path/name in test resources
     * @param message  text to add
     */
    void addToFile(String filePath, String message);

    /**
     * Create file with specified size (dummy data)
     * <p>Size is limited by {@link FileHelper#maxFileSize}</p>
     *
     * @param filePath file path/name in test resources
     * @param sizeInBytes  size in bites
     */
    void createFileWithSize(String filePath, long sizeInBytes);

    //get data

    /**
     * Return file size in bytes
     *
     * @param filePath file path/name in test resources
     * @return long with bytes
     */
    long getFileSize(String filePath);

    /**
     * Show all data from file in Allure attach
     * <p>Size is limited by {@link FileHelper#maxFileSize}</p>
     *
     * @param filePath file path/name in test resources
     */
    void showDataFromFile(String filePath);

    /**
     * Return how many times the text occurs in the file
     * <p>Size is limited by {@link FileHelper#maxFileSize}</p>
     *
     * @param filePath     file path and name in test resources
     * @param expectedText expected text
     * @return int with count
     */
    int countMatchesInFile(String filePath, String expectedText, boolean regex);

    //asserts

    /**
     * Assert size of file exactly as expected
     * <p><b>with await</b>
     *
     * @param filePath file path and name in test resources
     * @param expectedSize  expected size in bytes
     * @throws AssertionError on assert fail
     */
    void seeFileSizeIsExactly(String filePath, long expectedSize);

    /**
     * Assert size of file greater than minSize
     * <p><b>with await</b>
     *
     * @param filePath file path and name in test resources
     * @param minSize  minimum size in bytes
     * @throws AssertionError on assert fail
     */
    void seeFileSizeIsGreaterThan(String filePath, long minSize);

    /**
     * Assert size of file less than maxSize
     * <p><b>with await</b>
     *
     * @param filePath file path and name in test resources
     * @param maxSize  maximum size in bytes
     * @throws AssertionError on assert fail
     */
    void seeFileSizeIsLessThan(String filePath, long maxSize);

    /**
     * Assert that file contains expected text(by regex)
     * <p>Size is limited by {@link FileHelper#maxFileSize}</p>
     * <p><b>with await</b>
     *
     * @param filePath     file path/name in test resources
     * @param expectedText expected text with regex
     * @throws AssertionError on assert fail
     */
    void seeFileContainsRegex(String filePath, String expectedText);

    /**
     * Assert that file contains expected text(not regex)
     * <p>Size is limited by {@link FileHelper#maxFileSize}</p>
     * <p><b>with await</b>
     *
     * @param filePath     file path/name in test resources
     * @param expectedText expected text
     * @throws AssertionError on assert fail
     */
    void seeFileContainString(String filePath, String expectedText);

    /**
     * Assert that file not contains text(not regex)
     * <p>Size is limited by {@link FileHelper#maxFileSize}</p>
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

    //configs

    /**
     * Configure await in asserts with await
     *
     * @param awaitMs ms await
     * @return this
     * @throws IllegalArgumentException on invalid setup
     */
    FileHelper setAwaitMs(int awaitMs);

    /**
     * Set max file size for some methods
     *
     * @param maxFileSize size in bytes
     * @return this
     * @throws IllegalArgumentException on invalid value (less 200)
     */
    FileHelper setMaxFileSize(long maxFileSize);

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

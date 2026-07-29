package net.bugreaper.modules.filehelper.interfaces;


import net.bugreaper.core.exceptions.FileReaderException;
import net.bugreaper.modules.filehelper.FileHelper;

public interface FileHelperInt {


    /**
     * Clears all content from the file in <b>test</b> resources.
     *
     * <p>Creates the file if it does not exist.</p>
     *
     * @param filePath path to file in <b>test</b> resources
     * @throws FileReaderException if the file is missing or cannot be read
     */
    void cleanFile(String filePath);

    /**
     * Deletes the file from <b>test</b> resources.
     *
     * <p>Does not throw an exception if the file does not exist. A warning is logged instead.</p>
     *
     * @param filePath path to file in <b>test</b> resources
     * @throws FileReaderException if deleting fails
     */
    void deleteFile(String filePath);

    /**
     * Appends text to the end of the file in <b>test</b> resources.
     *
     * <p>Creates the file if it does not exist.</p>
     *
     * @param filePath path to file in <b>test</b> resources
     * @param message  text to append
     */
    void addToFile(String filePath, String message);

    /**
     * Creates a file in <b>test</b> resources with the specified size using dummy data.
     *
     * <p>File size is limited by {@link FileHelper#maxFileSize}.</p>
     *
     * @param filePath    path to file in <b>test</b> resources
     * @param sizeInBytes desired file size in bytes
     */
    void createFileWithSize(String filePath, long sizeInBytes);

    //get data

    /**
     * Returns the size of the file in <b>test</b> resources in bytes.
     *
     * @param filePath path to file in <b>test</b> resources
     * @return file size in bytes
     */
    long getFileSize(String filePath);

    /**
     * Adds the file content from <b>test</b> resources as an Allure attachment.
     *
     * <p>File size is limited by {@link FileHelper#maxFileSize}.</p>
     *
     * @param filePath path to file in <b>test</b> resources
     */
    void showDataFromFile(String filePath);

    /**
     * Returns the number of times the specified text occurs in the <b>test</b> resources file.
     *
     * <p>File size is limited by {@link FileHelper#maxFileSize}.</p>
     *
     * @param filePath     path to file in <b>test</b> resources
     * @param expectedText expected text or regular expression
     * @param regex        whether the expected text should be treated as a regular expression
     * @return number of occurrences in the file
     */
    int countMatchesInFile(String filePath, String expectedText, boolean regex);

    //asserts

    /**
     * Asserts that the <b>test</b> resources file size is exactly the expected size
     * <p><b>with await</b>
     *
     * @param filePath     path to file in <b>test</b> resources
     * @param expectedSize expected file size in bytes
     * @throws AssertionError if the assertion fails
     */
    void seeFileSizeIsExactly(String filePath, long expectedSize);

    /**
     * Asserts that the size of the file in <b>test</b> resources is greater than the specified minimum size.//TODO good
     * <p><b>with await</b>
     *
     * @param filePath path to file in <b>test</b> resources
     * @param minSize  minimum file size in bytes
     * @throws AssertionError if the assertion fails
     */
    void seeFileSizeIsGreaterThan(String filePath, long minSize);

    /**
     * Asserts that the size of the file in <b>test</b> resources is less than the specified maximum size.//TODO good
     * <p><b>with await</b>
     *
     * @param filePath path to file in <b>test</b> resources
     * @param maxSize  maximum file size in bytes
     * @throws AssertionError if the assertion fails
     */
    void seeFileSizeIsLessThan(String filePath, long maxSize);

    /**
     * Asserts that the <b>test</b> resources file contains text matching the specified regular expression.
     * <p><b>with await</b>
     * <p>File size is limited by {@link FileHelper#maxFileSize}.</p>
     *
     * @param filePath            path to file in <b>test</b> resources
     * @param expectedTextPattern expected text pattern (regular expression)
     * @throws AssertionError if the assertion fails
     */
    void seeFileContainsRegex(String filePath, String expectedTextPattern);

    /**
     * Asserts that the <b>test</b> resources file contains the specified text (not a regular expression).
     * <p><b>with await</b>
     * <p>File size is limited by {@link FileHelper#maxFileSize}.</p>
     *
     * @param filePath     path to the file in <b>test</b> resources
     * @param expectedText expected text
     * @throws AssertionError if the assertion fails
     */
    void seeFileContainString(String filePath, String expectedText);

    /**
     * Asserts that the <b>test</b> resources file does not contain the specified text (not a regular expression).
     *
     * <p>File size is limited by {@link FileHelper#maxFileSize}.</p>
     *
     * @param filePath       path to the file in <b>test</b> resources
     * @param unexpectedText text that must not be present
     * @throws AssertionError if the assertion fails
     */
    void seeFileDoesNotContainString(String filePath, String unexpectedText);

    /**
     * Asserts that the <b>test</b> resources file's MD5 hash matches the expected hash.
     *
     * @param filePath     path to file in <b>test</b> resources
     * @param expectedHash expected MD5 hash
     * @throws AssertionError if the assertion fails
     */
    void seeFileHashMd5Equal(String filePath, String expectedHash);

    /**
     * Asserts that the <b>test</b> resources file's SHA-1 hash matches the expected hash.
     *
     * @param filePath     path to file in <b>test</b> resources
     * @param expectedHash expected SHA-1 hash
     * @throws AssertionError if the assertion fails
     */
    void seeFileHashSha1Equal(String filePath, String expectedHash);

    /**
     * Asserts that the <b>test</b> resources file's SHA-256 hash matches the expected hash.
     *
     * @param filePath     path to file in <b>test</b> resources
     * @param expectedHash expected SHA-256 hash
     * @throws AssertionError if the assertion fails
     */
    void seeFileHashSha256Equal(String filePath, String expectedHash);

    /**
     * Asserts that the <b>test</b> resources file's SHA-512 hash matches the expected hash.
     *
     * @param filePath     path to file in <b>test</b> resources
     * @param expectedHash expected SHA-512 hash
     * @throws AssertionError if the assertion fails
     */
    void seeFileHashSha512Equal(String filePath, String expectedHash);

    //configs

    /**
     * Configures the global await timeout for assertions and operations that use await.
     *
     * @param awaitMs await timeout in milliseconds
     * @return this
     * @throws IllegalArgumentException if the provided timeout is invalid or less than 200 milliseconds
     */
    FileHelper setAwaitMs(int awaitMs);

    /**
     * Sets the maximum file size used by file operations.
     *
     * @param maxFileSize maximum file size in bytes
     * @return this
     * @throws IllegalArgumentException if the provided size is invalid
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

package net.bugreaper.core.filereaders;

import net.bugreaper.core.exceptions.FileReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import static net.bugreaper.core.filereaders.pathfinder.ProjectPaths.getTestResourcesPath;


/**
 * Utility class for reading files from the test resources directory.
 *
 * <p>Supports reading dynamic files created during test execution.</p>
 */
@SuppressWarnings("squid:S5960")
public final class ResourcesFileReader {

    private ResourcesFileReader() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesFileReader.class);
    private static final String FAILED_READ_MESSAGE = "Failed to read file: ";
    private static final String FAILED_TO_LOG_MESSAGE = "Failed to {} file '{}'. Check that the path is valid and the test resource directory exists.";


    /**
     * Reads the file content from <b>test</b> resources as a String.
     *
     * @param filePath path to file in <b>test</b> resources
     * @return file content as a String
     * @throws FileReaderException with a missing file
     */
    public static String readResourceFile(String filePath) {

        Path path = getProjectFilePath(filePath);

        try {
            return Files.readString(path);
        } catch (Exception e) {
            LOGGER.error(FAILED_TO_LOG_MESSAGE, "read", path);
            throw new FileReaderException(FAILED_READ_MESSAGE + path, e);
        }
    }

    /**
     * Overwrites a dynamic file in <b>test</b> resources with the specified content.
     *
     * <p>Existing file content is truncated before writing.</p>
     *
     * @param filePath path to file in <b>test</b> resources
     * @param content  data to write
     * @throws FileReaderException if writing the file fails
     */
    public static void overwriteTextToResourceFile(String filePath, String content) {

        Path path = getProjectFilePath(filePath);

        try {
            Files.writeString(path,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            failedToWrite(path, e);
        }
    }

    /**
     * Appends content to a dynamic file in <b>test</b> resources.
     *
     * <p>Existing file content is preserved.</p>
     *
     * @param filePath path to the file in <b>test</b> resources
     * @param content data to append
     * @throws FileReaderException if writing the file fails
     */
    public static void writeTextToResourceFile(String filePath, String content) {

        Path path = getProjectFilePath(filePath);

        try {
            Files.writeString(path,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (Exception e) {
            failedToWrite(path, e);
        }
    }

    private static void failedToWrite(Path path, Exception e) {
        LOGGER.error(FAILED_TO_LOG_MESSAGE, "write", path);
        throw new FileReaderException("Failed to write file: " + path, e);
    }


    private static Path getProjectFilePath(String filePath) {
        return Path.of(getTestResourcesPath(), filePath);
    }

    /**
     * Deletes the file in <b>test</b> resources.
     * <p>Does not throw an exception if the file does not exist. A warning is logged instead.</p>
     *
     * @param filePath path to file in <b>test</b> resources
     * @throws FileReaderException if deleting fails
     */
    public static void deleteResourceFile(String filePath) {

        File file = new File(getTestResourcesPath(), filePath);
        try {
            Files.delete(Path.of(getTestResourcesPath(), filePath));
            LOGGER.info("File deleted successfully: {}", filePath);
        } catch (NoSuchFileException e) {
            LOGGER.warn("File to delete does not exist, skipping: {}", file);
        } catch (Exception e) {
            throw new FileReaderException("Failed to delete file: " + file, e);
        }

    }

    /**
     * Creates a file in <b>test</b> resources with the specified size.
     *
     * <p>Overrides the existing file if it already exists.</p>
     *
     * @param filePath     path to file in <b>test</b> resources
     * @param sizeInBytes size in bytes
     * @throws FileReaderException if writing fails
     */
    public static void createResourceFileWithSize(String filePath, long sizeInBytes) {

        byte[] pattern = "Abcdefg".getBytes();

        try (FileOutputStream fos = new FileOutputStream(getTestResourcesPath() + filePath)) {
            long bytesWritten = 0;
            while (bytesWritten < sizeInBytes) {
                long bytesToWrite = Math.min(pattern.length, sizeInBytes - bytesWritten);
                fos.write(pattern, 0, (int) bytesToWrite);
                bytesWritten += bytesToWrite;
            }
        } catch (IOException e) {
            LOGGER.error(FAILED_TO_LOG_MESSAGE, "create", filePath);
            throw new FileReaderException("Failed to create file: " + getTestResourcesPath() + filePath, e);
        }

        LOGGER.debug("File with size {} bytes created: {}{}", sizeInBytes, getTestResourcesPath(), filePath);
    }

    /**
     * Get file(in resources) size
     *
     * @param fileName name (or folder/name - folder must exist) in resource directory
     * @return long with bytes
     * @throws FileReaderException on read fail
     */
    public static long getResourceFileSize(String fileName) {
        Path path = Paths.get(getTestResourcesPath(), fileName);

        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new FileReaderException(FAILED_READ_MESSAGE + getTestResourcesPath() + fileName, e);
        }
    }

    /**
     * Returns the file(in resources) existing flag
     *
     * @param fileName name (or folder/name) in resource directory
     * @return boolean
     */
    public static boolean resourceFileExistsStatus(String fileName) {
        File file = new File(getTestResourcesPath(), fileName);
        return file.exists();
    }

    /**
     * Check is file(in resources) exists
     *
     * @param fileName name (or folder/name) in resource directory
     * @throws AssertionError on failed
     */
    public static void seeResourceFileExists(String fileName) {

        if (!resourceFileExistsStatus(fileName)) {
            throw new AssertionError("File '%s%s' not exists".formatted(getTestResourcesPath(), fileName));
        }
    }

    /**
     * Check is file(in resources) NOT exists
     *
     * @param fileName name (or folder/name) in resource directory
     * @throws AssertionError on failed
     */
    public static void seeResourceFileNotExists(String fileName) {

        if (resourceFileExistsStatus(fileName)) {
            throw new AssertionError("File '%s%s' expected to not exist, but exists".formatted(getTestResourcesPath(), fileName));
        }
    }

    /**
     * Check is file(in resources) NOT empty
     *
     * @param fileName name (or folder/name) in resource directory
     * @throws AssertionError on failed
     */
    public static void seeResourceFileNotEmpty(String fileName) {

        if (getResourceFileSize(fileName) == 0) {
            throw new AssertionError("File '%s%s' expected to not be empty".formatted(getTestResourcesPath(), fileName));
        }
    }
}

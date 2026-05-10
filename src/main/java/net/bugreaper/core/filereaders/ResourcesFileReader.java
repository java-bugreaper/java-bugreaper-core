package net.bugreaper.core.filereaders;

import net.bugreaper.core.exceptions.FileReaderException;
import org.opentest4j.AssertionFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.MessageFormat;

import static net.bugreaper.core.filereaders.pathfinder.ProjectPaths.getTestResourcesPath;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Class for operation with files in test resources directory
 *
 * <p> Work with dynamic files (that recreates while running tests)
 */
@SuppressWarnings("squid:S5960")
public final class ResourcesFileReader {

    private ResourcesFileReader() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesFileReader.class);
    private static final String FAILED_READ_MESSAGE = "Failed to read file: ";
    private static final String FAILED_WRITE_MESSAGE = "Failed to write file {} is directory exists?";


    /**
     * Read dynamic(from project) file in resources (any type)
     *
     * @param filePath path to file in resources
     * @return String with data
     * @throws FileReaderException with a missing file
     */
    public static String readResourceFile(String filePath) {

        Path path = getProjectFilePath(filePath);

        try {
            return Files.readString(path);
        } catch (Exception e) {
            throw new FileReaderException(FAILED_READ_MESSAGE + path, e);
        }
    }

    /**
     * Overwrite(truncate old data) dynamic(from project) file in resources (any type)
     *
     * @param filePath path to file in resources
     * @param content  data to write
     * @throws FileReaderException on write error
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
     * Write(add to old data) dynamic(from project) file in resources (any type)
     *
     * @param filePath path to file in resources
     * @param content  data to write
     * @throws FileReaderException on write error
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
        LOGGER.error(FAILED_WRITE_MESSAGE, path);
        throw new FileReaderException("Failed to write file: " + path, e);
    }


    private static Path getProjectFilePath(String filePath) {
        return Path.of(getTestResourcesPath(), filePath);
    }

    /**
     * Delete file in resources
     *
     * @param filePath path to file in resources
     * <p>no exception if file not exist
     */
    public static void deleteResourceFile(String filePath) {

        File file = new File(getTestResourcesPath(), filePath);
        try {
            Files.delete(Path.of(getTestResourcesPath(), filePath));
            LOGGER.info("File deleted successfully: {}", filePath);
        } catch (NoSuchFileException e) {
            LOGGER.warn("File for delete not exist: {}", file);
        }  catch (Exception e) {
            throw new FileReaderException("Failed to delete file: " + file, e);
        }

    }

    /**
     * Create file(in resources) with specific size (will override existing file)
     *
     * @param fileName name (or folder/name - folder must exist)
     * @param sizeInBytes size in bytes
     *
     * @throws FileReaderException        on assert fail
     */
    public static void createResourceFileWithSize(String fileName, long sizeInBytes) {

        byte[] pattern = "Abcdefg".getBytes();

        try (FileOutputStream fos = new FileOutputStream(getTestResourcesPath() + fileName)) {
            long bytesWritten = 0;
            while (bytesWritten < sizeInBytes) {
                long bytesToWrite = Math.min(pattern.length, sizeInBytes - bytesWritten);
                fos.write(pattern, 0, (int) bytesToWrite);
                bytesWritten += bytesToWrite;
            }
        } catch (IOException e){
            LOGGER.error("Failed to write file {}, is directory exists in recourses?", fileName);
            throw new FileReaderException("Failed to create file: " + getTestResourcesPath() + fileName, e);
        }

        LOGGER.debug("File with size {} bytes created {}{}", sizeInBytes, getTestResourcesPath(),fileName);
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
            LOGGER.error("Failed read file {}, is directory exists in recourses?", fileName);
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
     * @throws AssertionFailedError on failed
     */
    public static void seeResourceFileExists(String fileName) {

        if(!resourceFileExistsStatus(fileName)) {
            fail(MessageFormat.format("File {0}{1} not exists", getTestResourcesPath(), fileName));
        }
    }

    /**
     * Check is file(in resources) NOT exists
     *
     * @param fileName name (or folder/name) in resource directory
     * @throws AssertionFailedError on failed
     */
    public static void seeResourceFileNotExists(String fileName) {

        if(resourceFileExistsStatus(fileName)) {
            fail(MessageFormat.format("File {0}{1} exists", getTestResourcesPath(), fileName));
        }
    }

    /**
     * Check is file(in resources) NOT empty
     *
     * @param fileName name (or folder/name) in resource directory
     * @throws AssertionFailedError on failed
     */
    public static void seeResourceFileNotEmpty(String fileName) {

        if(getResourceFileSize(fileName) == 0) {
            fail(MessageFormat.format("File {0}{1} is empty", getTestResourcesPath(), fileName));
        }
    }
}

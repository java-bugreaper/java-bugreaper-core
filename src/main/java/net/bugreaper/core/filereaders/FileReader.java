package net.bugreaper.core.filereaders;

import net.bugreaper.core.exceptions.FileReaderException;
import net.bugreaper.core.assertions.JsonAsserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.bugreaper.core.filereaders.pathfinder.ProjectPaths.getProjectPath;


/**
 * Utility class for working with static resource files loaded by the classloader.
 *
 * <p>Vot support dynamic files created or updated during test execution.</p>
 *
 * <p>For dynamic files, use {@link ResourcesFileReader}.</p>
 */
public final class FileReader {

    private FileReader() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);

    private static final String FAILED_READ_MESSAGE = "Failed to read file: ";


    // From resources classPath

    /**
     * Reads the content of a static resource file from the classpath as a String.
     *
     * @param filePath path to the file in resources
     * @return file content as a String
     * @throws FileReaderException if the file is missing or cannot be read
     */
    public static String readTextFromFile(String filePath) {
        Path path = getFilePath(filePath);

        try {
            return Files.readString(path);
        } catch (Exception e) {
            logFullPath();
            throw new FileReaderException("Failed to read resource(classpath) file: " + filePath, e);
        }
    }

    /**
     * Reads the content of a static JSON resource file from the classpath.
     *
     * @param filePath path to the file in resources
     * @return JSON content as a String
     * @throws FileReaderException if the file is missing or does not contain valid JSON
     */
    public static String readJsonFromFile(String filePath) {
        Path path = getFilePath(filePath);

        try {
            String result = Files.readString(path);
            JsonAsserts.assertValidJson(result);
            return result;
        } catch (IllegalArgumentException e) {
            logFullPath();
            throw new FileReaderException("File is not a valid JSON file: " + filePath, e);
        } catch (Exception e) {
            logFullPath();
            throw new FileReaderException("Failed to read resource(classpath) file: " + filePath, e);
        }
    }

    /**
     * Reads a CSV resource file from the classpath and converts it to a two-dimensional list.
     *
     * @param filePath path to the file in resources
     * @return CSV data as a {@code List<List<String>>}, where each inner list represents a row
     * @throws FileReaderException if the file is missing or cannot be read
     */
    @SuppressWarnings("squid:S5998")
    public static List<List<String>> readCsvToArray(String filePath) {
        List<List<String>> records;

        Path path = getFilePath(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            records = reader.lines()
                    .map(line -> Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")))
                    .toList();
        } catch (NoSuchFileException e) {
            logFullPath();
            throw new FileReaderException("File not found: " + filePath, e);
        } catch (Exception e) {
            logFullPath();
            throw new FileReaderException(FAILED_READ_MESSAGE + filePath, e);
        }
        return records;
    }

    private static void logFullPath() {
        LOGGER.info(
                "Full path to project: {}",
                getProjectPath());
    }

    private static Path getFilePath(String filePath) {
        try {
            return Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(filePath)).getPath());
        } catch (NullPointerException e) {
            logFullPath();
            throw new FileReaderException("File does not exist in resources: " + filePath, e);
        }

    }

}

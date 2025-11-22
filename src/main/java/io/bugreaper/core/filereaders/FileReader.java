package io.bugreaper.core.filereaders;

import io.bugreaper.core.exceptions.FileReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.bugreaper.core.assertions.JsonAsserts.assertValidJson;
import static io.bugreaper.core.filereaders.pathfinder.ProjectPaths.getTestResourcesPath;


/**
 * Class for operation with files in resources directory by classLoader
 *
 * <p> CAN BE PROBLEM with dynamic files (that recreates while running tests)
 * <p> For dynamic files use {@link ResourcesFileReader}
 */
public final class FileReader {

    private FileReader() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);

    private static final String FAILED_READ_MESSAGE = "Failed to read file: ";


    // From resources classPath

    /**
     * Read static(from ClassPath) resource file (any type)
     *
     * @param filePath path to file in resources
     * @return String with data
     * @throws FileReaderException with a missing file
     */
    public static String readTextFromFile(String filePath) {
        Path path = getFilePath(filePath);

        try {
            return Files.readString(path);
        } catch (Exception e) {
            logFullPath();
            throw new FileReaderException("Some problem with: " + filePath, e);
        }
    }

    /**
     * Read static(from ClassPath) resource file (Json/JsonArray type)
     *
     * @param filePath path to file in resources
     * @return String with data
     * @throws FileReaderException with a missing file or not JSON type
     */
    public static String readJsonFromFile(String filePath) {
        Path path = getFilePath(filePath);

        try {
            String result = Files.readString(path);
            assertValidJson(result);
            return result;
        } catch (IllegalArgumentException e) {
            logFullPath();
            throw new FileReaderException("File not JSON type: " + filePath, e);
        } catch (Exception e) {
            logFullPath();
            throw new FileReaderException("Can't find file in resources: " + filePath, e);
        }
    }

    /**
     * Read file in resources CSV and convert to array
     *
     * @param filePath path to file in resources
     * @return List with data
     * @throws FileReaderException with a missing file
     */
    @SuppressWarnings("squid:S5998")
    public static List<List<String>> readCsvToArray(String filePath) {
        List<List<String>> records;

        Path path = getFilePath(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            records = reader.lines()
                    .map(line -> Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")))
                    .toList();
        }catch (NoSuchFileException e) {
            logFullPath();
            throw new FileReaderException("File not found: " + path, e);
        }catch (Exception e) {
            logFullPath();
            throw new FileReaderException(FAILED_READ_MESSAGE + path, e);
        }
        return records;
    }

    private static void logFullPath(){
        LOGGER.error(
                "Project path info: {}",
                getTestResourcesPath());
    }

    private static Path getFilePath(String filePath){
        try {
            return Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(filePath)).getPath());
        }catch (NullPointerException e) {
            logFullPath();
            throw new FileReaderException("File not exist in resources: " + filePath, e);
        }

    }

}

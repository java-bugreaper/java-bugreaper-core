package io.bugreaper.core.filereaders;

import io.bugreaper.core.exceptions.FileReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static io.bugreaper.core.assertions.JsonAsserts.checkJson;


/**
 * Class for operation with files in resources directory by classLoader
 *
 * <p> CAN BE PROBLEM with dynamic files (that recreates while running tests)
 * <p> For dynamic files use {@link ResourcesFileReader}
 */
public class FileReader {

    private FileReader() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);
    static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


    // From resources classPath

    /**
     * Read static(from ClassPath) resource file (any type)
     *
     * @param filePath path to file in resources
     * @return String with data
     * @throws FileReaderException with a missing file
     */
    public static String readTextFromFile(String filePath) {

        try {
            return Files.readString(Path.of(Objects.requireNonNull(classLoader.getResource(filePath)).getPath()));
        } catch (Exception e) {
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

        try {
            String result = Files.readString(Path.of(Objects.requireNonNull(classLoader.getResource(filePath)).getPath()));
            checkJson(result);
            return result;
        } catch (IllegalArgumentException e) {
            throw new FileReaderException("File not JSON type: " + filePath, e);
        } catch (Exception e) {
            LOGGER.error("Is file exists? {}", classLoader.getResource(filePath));
            throw new FileReaderException("Can't find file in resources: " + filePath, e);
        }
    }

}

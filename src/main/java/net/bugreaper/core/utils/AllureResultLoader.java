package net.bugreaper.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bugreaper.core.exceptions.AllureValidatorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

public class AllureResultLoader {

    private AllureResultLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static JsonNode loadByTestName(String testName) {
        ObjectMapper mapper = new ObjectMapper();

        try (Stream<Path> files = Files.list(resultsDir().toPath())) {
            return files
                    .filter(p -> p.getFileName().toString().endsWith("-result.json"))
                    .map(p -> {
                        try {
                            return mapper.readTree(p.toFile());
                        } catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(json -> json.path("name").asText().contains(testName))
                    .findFirst()
                    .orElseThrow();
        } catch (Exception e) {
            throw new AllureValidatorException(e);
        }
    }

    /**
     * Removes all files from the Allure results directory.
     * If the directory does not exist, it will be created.
     */
    public static void cleanResultsDir() {
        File dir = resultsDir();

        try {
            if (dir.exists()) {
                try (var paths = Files.walk(dir.toPath())) {
                    paths.sorted(Comparator.reverseOrder())
                            .filter(path -> !path.equals(dir.toPath()))
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    throw new AllureValidatorException(
                                            "Failed to delete: " + path, e);
                                }
                            });
                }
            }

            Files.createDirectories(dir.toPath());

        } catch (IOException e) {
            throw new AllureValidatorException(
                    "Failed to clean Allure results directory: "
                            + dir.getAbsolutePath(),
                    e
            );
        }
    }

    public static File resultsDir() {
        String path = System.getProperty(
                "allure.results.directory",
                "allure-results"
        );

        return new File(path).getAbsoluteFile();
    }
}
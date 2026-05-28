package net.bugreaper.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bugreaper.core.exceptions.AllureValidatorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static File resultsDir() {
        String path = System.getProperty(
                "allure.results.directory",
                "allure-results"
        );

        return new File(path).getAbsoluteFile();
    }
}
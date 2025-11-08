package io.bugreaper.core.filereaders.pathfinder;


import io.bugreaper.core.exceptions.FileReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectPaths {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectPaths.class);

    private ProjectPaths() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Resolves absolute path to the "src/test/resources" directory of the current runtime project.
     * Works when core is a dependency (Maven or Gradle), regardless of module structure.
     */
    public static String getTestResourcesPath() {
        try {
            // Try to locate a known resource from the test resources classpath
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resourceUrl = classLoader.getResource(".");
            if (resourceUrl == null) {
                throw new IllegalStateException("No resource directory found in classpath.");
            }

            // Convert to filesystem path
            Path testClassesDir = Paths.get(resourceUrl.toURI());

            // Usually ends with: /build/resources/test OR /target/test-classes
            Path projectRoot = findProjectRoot(testClassesDir);

            //can be moved to param or to config
            Path testResources = projectRoot.resolve("src/test/resources");

            if (!testResources.toFile().exists()) {
                LOGGER.warn("Test resources directory not found: {}", testResources);
            }

            return testResources.toAbsolutePath() + "/";

        } catch (Exception e) {
            throw new FileReaderException("Failed to determine test resources path", e);
        }
    }

    /**
     * Walks up the filesystem tree until the project root is found.
     * Assumes presence of 'build.gradle', 'pom.xml', or '.git' as project root marker.
     */
    private static Path findProjectRoot(Path start) {
        Path current = start.toAbsolutePath();
        while (current != null) {
            if (new File(current.toFile(), "pom.xml").exists()
                    || new File(current.toFile(), "build.gradle").exists()
                    || new File(current.toFile(), ".git").exists()) {
                return current;
            }
            current = current.getParent();
        }

        return start.getParent().getParent().getParent();
    }

}

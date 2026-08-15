package net.bugreaper.core.filereaders.pathfinder;


import net.bugreaper.core.exceptions.FileReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectPaths {

    private ProjectPaths() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectPaths.class);


    /**
     * Resolves absolute path to the "src/test/resources" directory of the current runtime project.
     * Works when core is a dependency (Maven or Gradle), regardless of module structure.
     */
    public static String getTestResourcesPath() {
        // Try to locate a known resource from the test resources classpath

        //can be moved to param or to config
        Path testResources = getProjectPath().resolve("src/test/resources");

        if (!testResources.toFile().exists()) {
            LOGGER.warn("Test resources directory not found: {}", testResources);
        }

        return testResources.toAbsolutePath() + "/";
    }

    public static Path getProjectPath() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resourceUrl = classLoader.getResource(".");
            if (resourceUrl == null) {
                throw new IllegalStateException("No resource directory found in classpath.");
            }

            // Convert to filesystem path
            Path testClassesDir = Paths.get(resourceUrl.toURI());

            return findProjectRoot(testClassesDir);

        } catch (Exception e) {
            throw new FileReaderException("Failed to determine test resources path", e);
        }
    }

    /**
     * Walks up the filesystem tree until the project root is found.
     * Assumes presence of 'build.gradle' or 'pom.xml' as project root marker.
     */
    private static Path findProjectRoot(Path start) {
        Path current = start.toAbsolutePath();
        while (current != null) {
            if (new File(current.toFile(), "pom.xml").exists()
                    || new File(current.toFile(), "build.gradle").exists()) {
                return current;
            }
            current = current.getParent();
        }

        return start.getParent().getParent().getParent();
    }

}

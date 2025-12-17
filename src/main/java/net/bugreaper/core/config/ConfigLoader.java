package net.bugreaper.core.config;

import net.bugreaper.core.exceptions.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class responsible for selecting the correct YAML configuration
 * filename based on the optional system property {@code -Dbugreaper=<suffix>}.
 */
public class ConfigLoader {

    private ConfigLoader() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);

    /**
     * Loads a YAML file and returns its content as a nested Map.
     *
     * @param fileName   name of the YAML file in resources
     * @return parsed Map representation of the YAML
     * @throws ConfigException if file not found or failed to load
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadYaml(String fileName) {

        Map<String, Object> result = new LinkedHashMap<>();

        LOGGER.debug("Start read config from: {}", fileName);
        try (InputStream input = getInputStream(fileName, ConfigLoader.class)) {


            Yaml yaml = new Yaml();
            Object loaded = yaml.load(input);

            if (loaded instanceof Map) {
                result = (Map<String, Object>) loaded;
            }

        } catch (Exception e) {
            throw new ConfigException("Failed to load YAML: " + fileName + "\n" + e.getMessage());
        }

        return result;
    }

    /**
     * Loads a file from the classpath as an {@link InputStream}.
     *
     * @param fileName   name of the file in resources
     * @param classRunFrom class used to resolve the classloader
     * @return input stream for the resource
     * @throws ConfigException if file is not found
     */
    public static InputStream getInputStream(String fileName, Class<?> classRunFrom) {

        InputStream is = classRunFrom.getClassLoader().getResourceAsStream(fileName);

        if (is == null) {
            LOGGER.error("Create config file in resources!");
            throw new ConfigException("Config file not found: " + fileName);
        }
        return is;
    }

}
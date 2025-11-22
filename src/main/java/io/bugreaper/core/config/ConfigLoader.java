package io.bugreaper.core.config;

import io.bugreaper.core.exceptions.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigLoader {

    private ConfigLoader() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);

    /**
     * Loads a YAML file and returns its content as a nested Map.
     * Returns empty map if file not found or failed to load.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadYaml() {
        String fileName = YamlUtils.getConfigFileName();

        Map<String, Object> result = new LinkedHashMap<>();

        try (InputStream input = getInputStream(fileName, ConfigLoader.class)) {

            if (input == null) {
                LOGGER.error("Create config file in resources!");
                throw new ConfigException("Config file not found: " + fileName);
            }

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

    public static InputStream getInputStream(String fileName, Class<?> classRunFrom) {
        try {
            // 1. Check external file (project root or absolute path)
            File file = new File(fileName);
            if (file.exists()) {
                return new FileInputStream(file);
            }

            // 2. Check classpath resources
            return classRunFrom.getClassLoader().getResourceAsStream(fileName);

        } catch (Exception e) {
            return null;
        }
    }

}
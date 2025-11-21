package io.bugreaper.core.config;

import io.bugreaper.core.exceptions.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class YamlUtils {

    private YamlUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtils.class);

    /**
     *
     * @param root config in Map format
     * @param path path to key
     * @param isOptional if true - key can be absent, false - key is required
     * @return Object with value
     * @throws ConfigException on missing required fields or other errors
     */
    @SuppressWarnings("unchecked")
    public static Object getValueByPath(Map<String, Object> root, String path, boolean isOptional) {

        if (root == null) {
            throw new ConfigException("Config file is empty");
        } else if (path == null || path.isEmpty()) {
            throw new ConfigException("Field not provided or empty");
        }

        String[] keys = path.split("\\.");
        Object current = root;

        for (String key : keys) {

            // Must be a Map at each level
            if (!(current instanceof Map)) {
                if (isOptional){
                    return optionalFieldMissing(path);
                }
                throw new ConfigException("Path segment '" + key + "' does not lead to a map (path: " + path + ")");
            }

            Map<String, Object> map = (Map<String, Object>) current;

            // Key must exist
            if (!map.containsKey(key)) {
                if (isOptional){
                    return optionalFieldMissing(path);
                }
                throw new ConfigException("Missing required config field: " + path);
            }

            current = map.get(key);
        }

        if (current == null) {
            throw new ConfigException(
                    "Config key '" + path + "' is present but null. Null is not allowed"
            );
        }
        return current; // valid, non-null optional
    }

    private static Object optionalFieldMissing(String path){
        LOGGER.error("Optional config field <{}> not found - using default value.", path);
        return null;
    }

    /**
     * Returns the YAML config file name based on the system property `bugreaperEnv`.
     * Default: bugreaper.yml
     * Example: -DbugreaperEnv=test  bugreaper-test.yml
     */
    public static String getConfigFileName() {
        String suffix = System.getProperty("bugreaperEnv");
        return (suffix == null || suffix.isEmpty())
                ? "bugreaper.yml"
                : String.format("bugreaper-%s.yml", suffix);
    }

}

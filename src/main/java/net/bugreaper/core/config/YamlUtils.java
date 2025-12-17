package net.bugreaper.core.config;

import net.bugreaper.core.exceptions.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Utility class for loading, caching, and reading values from YAML configuration files.
 * <p>
 * This class loads the YAML configuration and caches it in memory.
 * If the system property {@code -Dbugreaper=<suffix>} is changed during runtime,
 * the cache should be cleared using {@link #clearCache()} to force reload.
 */
public class YamlUtils {

    private YamlUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtils.class);

    /**
     * Cached YAML configuration map.
     * Loaded once unless {@link #clearCache()} is called.
     */
    private static Map<String, Object> cachedConfig;

    /**
     * Returns the parsed YAML configuration.
     * <p>
     * If the config is not yet loaded, it will be loaded automatically
     *
     * @return the YAML configuration map
     */
    public static synchronized Map<String, Object> getConfig() {
        if (cachedConfig == null) {
            String fileName = getConfigFileName();
            cachedConfig = ConfigLoader.loadYaml(fileName);
        }
        return cachedConfig;
    }

    /**
     * Clears the cached YAML configuration.
     * <p>
     * After clearing, the next call to {@link #getConfig()} will reload the file.
     */
    public static synchronized void clearCache() {
        cachedConfig = null;
    }

    /**
     * Reads a  nested required YAML String value by dot-separated path.
     * <p>
     * Example:
     * <pre>
     * users.user1.name
     * </pre>
     *
     * @param path dot-separated path to the key
     * @return String with data
     * @throws ConfigException when field not provided or data is null
     */
    public static String getStringValueByPath(String path){

        Object value = getValueByPath(path, false);

        checkType(value, path, String.class);
        return value.toString();
    }

    /**
     * Reads a  nested required YAML integer value by dot-separated path.
     * <p>
     * Example:
     * <pre>
     * users.user1.id
     * </pre>
     *
     * @param path dot-separated path to the key
     * @return int with data
     * @throws ConfigException when field not provided or data is null
     */
    public static int getIntegerValueByPath(String path){

        Object value = getValueByPath(path, false);
        checkType(value, path, Integer.class);
        return (int) value;
    }

    /**
     * Reads a  nested required YAML boolean value by dot-separated path.
     * <p>
     * Example:
     * <pre>
     * users.user1.is-admin
     * </pre>
     *
     * @param path dot-separated path to the key
     * @return boolean
     * @throws ConfigException when field not provided or data is null
     */
    public static boolean getBooleanValueByPath(String path){

        Object value = getValueByPath(path, false);
        checkType(value, path, Boolean.class);
        return (boolean) value;
    }

    private static void checkType(Object value, String path, Class<?> expectedType) {
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException(
                    String.format("%s must be a %s but was: %s",
                            getValueKey(path),
                            expectedType.getSimpleName(),
                            value == null ? "null" : value.getClass().getName() //null validated on previous steps!
                    )
            );
        }
    }

    /**
     *
     * @param path path to key
     * @param isOptional if true - key can be absent, false - key is required
     * @return Object with value
     * @throws ConfigException on missing required fields or other errors
     */
    @SuppressWarnings("unchecked")
    public static Object getValueByPath(String path, boolean isOptional) {
        Map<String, Object> root = getConfig();
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
        LOGGER.debug("Optional config field <{}> not found - using default value.", path);
        return null;
    }

    private static String getValueKey(String path) {
        return path.substring(path.lastIndexOf('.') + 1);
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

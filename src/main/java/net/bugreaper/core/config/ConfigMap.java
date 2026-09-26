package net.bugreaper.core.config;

import net.bugreaper.core.exceptions.ConfigException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import static net.bugreaper.core.config.YamlUtils.isLongValue;

/**
 * Wrapper around a YAML configuration section that provides typed access to its entries.
 * <p>
 * Typed getter methods remove consumed keys from the underlying map. Call {@link #checkUnknown()}
 * after reading expected options to fail fast when unsupported properties remain.
 */
public class ConfigMap {

    private final Map<String, Object> conMap;
    private final String optionsPath;


    /**
     * Creates a configuration map wrapper for the specified YAML section.
     *
     * @param map section values keyed by property name
     * @param optionsPath dot-separated path to the section, used in error messages
     */
    public ConfigMap(Map<String, Object> map, String optionsPath) {
        this.conMap = map;
        this.optionsPath = optionsPath;
    }

    /**
     * Performs the given action for each remaining entry in this configuration map.
     *
     * @param action action to perform for each key-value pair
     */
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        conMap.forEach(action);
    }

    /**
     * Returns a copy of the keys contained in this configuration map.
     *
     * @return a set containing the configuration keys
     */
    public Set<String> copyKeySet() {
        return new HashSet<>(conMap.keySet());
    }

    /**
     * Returns whether this configuration map currently contains the specified key.
     *
     * @param key key whose presence should be tested
     * @return {@code true} if the key is present, otherwise {@code false}
     */
    public boolean containsKey(Object key) {
        return conMap.containsKey(key);
    }

    /**
     * Returns whether this configuration map is empty.
     *
     * @return {@code true} if the configuration map contains no keys, otherwise {@code false}
     */
    public boolean isEmpty() {
        return conMap.isEmpty();
    }
    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     */
    public Object get(Object key) {
        return conMap.get(key);
    }

    /**
     * Verifies that all configuration entries have been consumed by typed getters.
     *
     * @throws ConfigException if any unknown properties remain
     */
    public void checkUnknown() {
        if (!conMap.isEmpty()) {
            throw new ConfigException(
                    "Unknown setup properties for '%s': %s".formatted(optionsPath, conMap.keySet()));
        }
    }

    /**
     * Reads and removes a required numeric property as a {@code long}.
     *
     * @param key property key inside this configuration section
     * @return property value converted to {@code long}
     * @throws IllegalArgumentException if the key is missing, null, or not numeric
     */
    public long getLong(String key) {
        return ((Number) checkValueType(key, long.class)).longValue();
    }

    /**
     * Reads and removes a required numeric property as an {@code int}.
     *
     * @param key property key inside this configuration section
     * @return property value converted to {@code int}
     * @throws IllegalArgumentException if the key is missing, null, or not numeric
     */
    public int getInt(String key) {
        return (int) checkValueType(key, Integer.class);
    }


    /**
     * Reads and removes a required boolean property.
     *
     * @param key property key inside this configuration section
     * @return property value
     * @throws IllegalArgumentException if the key is missing, null, or not a {@link Boolean}
     */
    public boolean getBoolean(String key) {
        return (Boolean) checkValueType(key, Boolean.class);
    }

    /**
     * Reads and removes a required string property.
     *
     * @param key property key inside this configuration section
     * @return property value
     * @throws IllegalArgumentException if the key is missing, null, or not a {@link String}
     */
    public String getString(String key) {
        return (String) checkValueType(key, String.class);
    }

    private Object checkValueType(String key, Class<?> expectedType) {

        Object value = conMap.remove(key);

        boolean valid = value != null
                // YAML may provide an Integer even when a long is expected
                && ((expectedType == long.class && isLongValue(value))
                || expectedType.isInstance(value));

        if (!valid) {
            throw new IllegalArgumentException(
                    String.format(
                            "Config property key '%s:%s' must have type <%s>, but got: <%s>(%s)",
                            optionsPath,
                            key,
                            expectedType.getSimpleName(),
                            value == null ? "null" : value.getClass().getName(),
                            value
                    )
            );
        }
        
        return value;
    }

}

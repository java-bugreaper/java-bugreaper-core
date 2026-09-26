package net.bugreaper.core.config;

import net.bugreaper.core.exceptions.ConfigException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlUtils2Tests {


    @BeforeEach
    void copyConfig() {
        YamlUtils.clearCache();
    }

    @Test
    void testEmptyConfigFile() {
        System.setProperty("bugreaperEnv", "empty");

        Throwable exception = assertThrows(ConfigException.class, () ->
                YamlUtils.getValueByPath("test.none", true));

        assertEquals("Config file is empty",
                exception.getMessage());
    }

    @Test
    void testNotExistConfigFile() {
        System.setProperty("bugreaperEnv", "notexist");

        Throwable exception = assertThrows(ConfigException.class, () ->
                YamlUtils.getValueByPath("test.none", true));

        assertEquals("""
                        Failed to load YAML: bugreaper-notexist.yml
                        Config file not found: bugreaper-notexist.yml""",
                exception.getMessage());
    }

    @Test
    void testConfigMapNullValue() {
        Map<String, Object> map = new HashMap<>();
        //ConfigLoader will except first!
        map.put("test", null);

        ConfigMap confMap = new ConfigMap(map, "for-test.map-key");

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                confMap.getLong("test"));

        assertEquals("Config property key 'for-test.map-key:test' must have type <long>, but got: <null>(null)",
                exception.getMessage());
    }

}

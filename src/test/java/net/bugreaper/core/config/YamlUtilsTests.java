package net.bugreaper.core.config;

import ch.qos.logback.classic.Level;
import net.bugreaper.core.exceptions.ConfigException;
import net.bugreaper.core.utils.LogWatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("java:S2699")
class YamlUtilsTests {

    @BeforeEach
    void copyConfig() {
        System.setProperty("bugreaperEnv", "yml");
        YamlUtils.clearCache();
    }

    @Test
    void testConfigLoad() {
        LogWatcher logWatcher = new LogWatcher("net.bugreaper.core", Level.DEBUG);
        YamlUtils.getValueByPath("none", true);
        assertEquals(
                "[[INFO] Start read config from: bugreaper-yml.yml]",
                logWatcher.getLoggedEvents(Level.INFO).toString());
        assertEquals(
                "[[DEBUG] Optional config field 'none' not found - using default value.]",
                logWatcher.getLoggedEvents(Level.DEBUG).toString());
        logWatcher.detach();
    }

    @Test
    void testNotExistOptionalField() {
        assertNull(YamlUtils.getValueByPath("none", true));
    }

    @Test
    void testNotExistRequiredField() {

        Throwable exception = assertThrows(ConfigException.class, () ->
                YamlUtils.getValueByPath("none.text", false));

        assertEquals("Missing required config field: 'none.text'",
                exception.getMessage());
    }

    // SPECIFIC

    @Test
    void testForEach() {
        ConfigMap myConfig = YamlUtils.getConfigMapValueByPath("for-test.map-key3", false);
        Map<String, Object> map2 = new HashMap<>();

        for (String key : myConfig.copyKeySet()) {
            if ("int".equals(key)) {
                map2.put(key, myConfig.getInt(key));
            }
        }

        assertEquals(10, map2.get("int"));
        assertTrue(myConfig.isEmpty());
    }

    @Test
    void testCopyKeySet() {
        ConfigMap myConfig = YamlUtils.getConfigMapValueByPath("for-test.map-key2", false);
        Map<String, Object> map2 = new HashMap<>();
        myConfig.forEach(map2::put);
        assertEquals("test", map2.get("test.string2"));
    }

    @Test
    void testGetString() {
        assertEquals("my-string", YamlUtils.getStringValueByPath("for-test.test"));
    }

    @Test
    void testGetInteger() {
        assertEquals(2147483647, YamlUtils.getIntegerValueByPath("for-test.test-n1"));
    }

    @Test
    void testGetIntegerShort() {
        assertEquals(1, YamlUtils.getIntegerValueByPath("for-test.test-short"));
    }

    @Test
    void testGetLong() {
        assertEquals(9223372036854775807L, YamlUtils.getLongValueByPath("for-test.test-long"));
    }

    @Test
    void testGetLongButInt() {
        assertEquals(2147483647, YamlUtils.getLongValueByPath("for-test.test-n1"));
    }

    @Test
    void testGetLongButShort() {
        assertEquals(1, YamlUtils.getLongValueByPath("for-test.test-short"));
    }

    @Test
    void testGetBoolean() {
        assertFalse(YamlUtils.getBooleanValueByPath("for-test.test-b"));
    }

    @Test
    void testGetMap() {
        ConfigMap map = YamlUtils.getConfigMapValueByPath("for-test.map-key", false);

        int erInt = 2147483647;
        int arInt2 = 0;

        //not ramove
        assertEquals(erInt, map.get("int"));

        if (map.containsKey("int")) {

            arInt2 = map.getInt("int");
        }


        assertEquals(erInt, arInt2);

        int erLong = 7000;
        assertEquals(erLong, map.getLong("long"));


        long erLong2 = 9223372036854775807L;
        assertEquals(erLong2, map.getLong("longBig"));

        assertEquals("myString", map.getString("string"));

        assertTrue(map.getBoolean("boolean"));

        Throwable exception = assertThrows(ConfigException.class, map::checkUnknown);

        assertEquals("Unknown setup properties for 'for-test.map-key': [string:dots]",
                exception.getMessage());
    }

    @Test
    void testGetMapNullValue() {
        ConfigMap map = YamlUtils.getConfigMapValueByPath("for-test.map-key4", false);

        assertNull(map.get("data"));
    }

    @Test
    void testGetMapWrong() {
        ConfigMap map = YamlUtils.getConfigMapValueByPath("for-test.map-key2", false);

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                map.getLong("test.string2"));

        assertEquals("Config property key 'for-test.map-key2:test.string2' must have type <long>, but got: <java.lang.String>(test)",
                exception.getMessage());

        map.checkUnknown(); //pass
    }

    @Test
    void testGetMapEmpty() {
        ConfigMap map = YamlUtils.getConfigMapValueByPath("for-test.map-key1", true);
        if(!map.isEmpty()){
            throw new AssertionError("Map is not empty!");
        }
    }


    @Test
    void testGetStringError() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getStringValueByPath("for-test.test-w1"));

        assertEquals("Config key 'for-test.test-w1' must have type <String>, but got: <java.lang.Integer>(1111)",
                exception.getMessage());
        }


    @Test
    void testGetStringNullError() {

        Throwable exception = assertThrows(ConfigException.class, () ->
                YamlUtils.getStringValueByPath("for-test.test-null"));

        assertEquals("Config key 'for-test.test-null' is present but null. Null is not allowed",
                exception.getMessage());
    }

    @Test
    void testGetIntegerTypeError() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getIntegerValueByPath("for-test.test-w3"));

        assertEquals("Config key 'for-test.test-w3' must have type <Integer>, but got: <java.lang.String>(string_data)",
                exception.getMessage());
    }

    @Test
    void testGetIntegerTypeLongError() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getIntegerValueByPath("for-test.test-long"));

        assertEquals("Config key 'for-test.test-long' must have type <Integer>, but got: <java.lang.Long>(9223372036854775807)",
                exception.getMessage());
    }

    @Test
    void testGetLongTypeBigIntError() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getLongValueByPath("for-test.test-big"));

        assertEquals("Config key 'for-test.test-big' must have type <long>, but got: <java.math.BigInteger>(9992233720368547758079999999999)",
                exception.getMessage());
    }

    @Test
    void testGetLongTypeDecimal() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getLongValueByPath("for-test.test-dec"));

        assertEquals("Config key 'for-test.test-dec' must have type <long>, but got: <java.lang.Double>(22.55)",
                exception.getMessage());
    }

    @Test
    void testGetBooleanError() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getBooleanValueByPath("for-test.test-w4"));

        assertEquals("Config key 'for-test.test-w4' must have type <Boolean>, but got: <java.lang.String>(true)",
                exception.getMessage());
    }

    @Test
    void testGetMapError() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getConfigMapValueByPath("for-test.test-w1", false));

            assertEquals("Config key 'for-test.test-w1' must have type <Map>, but got: <java.lang.Integer>(1111)",
                    exception.getMessage());
    }

}

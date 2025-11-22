package io.bugreaper.core.config;

import io.bugreaper.core.exceptions.ConfigException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlUtilsTests {

    @Test
    void testGetString() {
        Map<String, Object>  rawData = Map.of("modules",  Map.of( "test", "my-string"));

        assertEquals("my-string", YamlUtils.getStringValueByPath(rawData, "modules.test"));
    }

    @Test
    void testGetInteger() {
        Map<String, Object>  rawData = Map.of("modules",  Map.of( "test", 8080));

        assertEquals(8080, YamlUtils.getIntegerValueByPath(rawData, "modules.test"));
    }

    @Test
    void testGetBoolean() {
        Map<String, Object>  rawData = Map.of("modules",  Map.of( "test", false));

        assertFalse(YamlUtils.getBooleanValueByPath(rawData, "modules.test"));
    }

    @Test
    void testGetStringError() {

        Map<String, Object>  rawData = Map.of("modules",  Map.of( "test", 1111));

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getStringValueByPath(rawData, "modules.test"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("test must be a String but was: java.lang.Integer"));
    }

    @Test
    void testGetStringNullError() {

        Map<String, Object>  nullable = new java.util.HashMap<>(Map.of());
        nullable.put("test", null);

        Map<String, Object>  rawData = new java.util.HashMap<>(Map.of());
        rawData.put("modules", nullable);

        Throwable exception = assertThrows(ConfigException.class, () ->
                YamlUtils.getStringValueByPath(rawData, "modules.test"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Config key 'modules.test' is present but null. Null is not allowed"));
    }

    @Test
    void testGetIntegerError() {

        Map<String, Object>  rawData = Map.of("modules",  Map.of( "test", "string_data"));

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getIntegerValueByPath(rawData, "modules.test"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("test must be a Integer but was: java.lang.String"));
    }


    @Test
    void testGetBooleanError() {

        Map<String, Object>  rawData = Map.of("modules",  Map.of( "test", "true"));

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getBooleanValueByPath(rawData, "modules.test"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("test must be a Boolean but was: java.lang.String"));
    }
    


}

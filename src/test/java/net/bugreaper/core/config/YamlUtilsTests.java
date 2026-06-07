package net.bugreaper.core.config;

import net.bugreaper.core.exceptions.ConfigException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YamlUtilsTests {


    @BeforeAll
    static void copyConfig() {
        System.setProperty("bugreaperEnv", "yml");
        YamlUtils.clearCache();
    }

    @Test
    void testGetString() {
        assertEquals("my-string", YamlUtils.getStringValueByPath("for-test.test"));
    }

    @Test
    void testGetInteger() {
        assertEquals(8080, YamlUtils.getIntegerValueByPath("for-test.test-n1"));
    }

    @Test
    void testGetBoolean() {
        assertFalse(YamlUtils.getBooleanValueByPath("for-test.test-b"));
    }

    @Test
    void testGetStringError() {


        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getStringValueByPath("for-test.test-w1"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("test-w1 must be a String but was: java.lang.Integer"));
    }


    @Test
    void testGetStringNullError() {

        Throwable exception = assertThrows(ConfigException.class, () ->
                YamlUtils.getStringValueByPath("for-test.test-null"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Config key 'for-test.test-null' is present but null. Null is not allowed"));
    }

    @Test
    void testGetIntegerError() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getIntegerValueByPath("for-test.test-w3"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("test-w3 must be a Integer but was: java.lang.String"));
    }


    @Test
    void testGetBooleanError() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                YamlUtils.getBooleanValueByPath("for-test.test-w4"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("test-w4 must be a Boolean but was: java.lang.String"));
    }

}

package net.bugreaper.core.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTests {

    @Test
    void printConfigTest() {
        TestConfig config = new TestConfig();

        String result = printConfig(config);

        assertTrue(result.contains("host=localhost"));
        assertTrue(result.contains("port=6379"));
        assertTrue(result.contains("enabled=true"));
    }

    @Test
    void printConfigShouldExcludeObjectPropertiesTest() {
        TestConfig config = new TestConfig();

        String result = printConfig(config);

        assertFalse(result.contains("class="));
    }

    @Test
    void printConfigShouldPrintNullValuesTest() {
        TestConfig config = new TestConfig();

        String result = printConfig(config);

        assertTrue(result.contains("username=null"));
    }

    @Test
    void printConfigShouldPrintAllPropertiesSeparatedBySystemLineSeparatorTest() {
        TestConfig config = new TestConfig();

        String result = printConfig(config);

        assertTrue(result.contains(
                "host=localhost" + System.lineSeparator() + "port=6379"
        ));
    }

    @Test
    void printConfigShouldRejectNullTest() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> printConfig(null)
        );

        assertEquals("Configuration must not be null", thrown.getMessage());
    }

    private String printConfig(Object config) {
        // call your actual utility
        return ConfigurationReader.formatConfiguration(config);
    }

    static class TestConfig {

        private String host = "localhost";
        private int port = 6379;
        private boolean enabled = true;
        private String username;

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getUsername() {
            return username;
        }
    }

    @Test
    void printConfigShouldFailWhenPropertyCannotBeReadTest() {
        BrokenConfig config = new BrokenConfig();

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> ConfigurationReader.formatConfiguration(config)
        );

        assertTrue(thrown.getMessage().contains("broken"));
    }

    static class BrokenConfig {

        public String getBroken() {
            throw new IllegalStateException("test");
        }
    }

}

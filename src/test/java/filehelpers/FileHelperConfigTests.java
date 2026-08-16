package filehelpers;

import ch.qos.logback.classic.Level;
import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.core.utils.LogWatcher;
import net.bugreaper.modules.filehelper.FileHelper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FileHelperConfigTests {

    public static final String PROPERTY = "bugreaperEnv";

    private LogWatcher logWatcher;
    @BeforeEach
    void setup() {
        logWatcher = new LogWatcher("net.bugreaper.modules.filehelper.FileHelper", Level.DEBUG);
        System.clearProperty(PROPERTY);
        YamlUtils.clearCache();
    }

    @AfterEach
    void teardown() {
        logWatcher.detach();
    }


    @Test
    void testDefaultConfig() {
        FileHelper fileHelper = FileHelper.getInstance();


        MatcherAssert.assertThat(
                "Info summary",
                fileHelper.getConfigSummary(),
                StringContains.containsString("""
                        FileHelper:
                            await=550
                            await-poll-interval=110
                            maxFileSize=800"""));
    }

    @Test
    void testNoOptionalAwaitConfig() {
        System.setProperty(PROPERTY, "nooptional");
        FileHelper fileHelper = new FileHelper();

        MatcherAssert.assertThat(
                "Info summary",
                fileHelper.getConfigSummary(),
                StringContains.containsString("""
                        FileHelper:
                            await=2000
                            await-poll-interval=100
                            maxFileSize=1048576"""));
    }

    @Test
    void testNoConfigFile() {
        System.setProperty(PROPERTY, "nofile");
        FileHelper fileHelper = new FileHelper();

        MatcherAssert.assertThat(
                "Info summary",
                fileHelper.getConfigSummary(),
                StringContains.containsString("""
                        FileHelper:
                            await=2000
                            await-poll-interval=100
                            maxFileSize=1048576"""));

        assertEquals(
                """
                        [[WARN] Config file error, but FileHelper is not expected required keys: Failed to load YAML: bugreaper-nofile.yml
                        Config file not found: bugreaper-nofile.yml]""",
                logWatcher.getLoggedEvents(Level.WARN).toString());
    }

    @Test
    void configFileHelperAwaitMsTest() {
        FileHelper fileHelper = new FileHelper();
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                fileHelper.setAwaitMs(199));

        assertEquals(
                "awaitMs too small (can`t bee less 200ms)",
                exception.getMessage(),
                "Error on config .setAwaitMs validation");
    }

    @Test
    void configFileHelperMaxFileSizeTest() {
        FileHelper fileHelper = new FileHelper();
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                fileHelper.setMaxFileSize(-1));

        assertEquals(
                "maxFileSize too small (can`t bee less 1)",
                exception.getMessage(),
                "Error on config .setMaxFileSize validation");
    }

}

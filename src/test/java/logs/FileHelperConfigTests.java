package logs;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.modules.filehelper.FileHelper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FileHelperConfigTests {

    public static final String PROPERTY = "bugreaperEnv";

    @BeforeEach
    void copyConfig() {
        System.clearProperty(PROPERTY);
        YamlUtils.clearCache();
    }

    @Test
    void testDefaultConfig() {
        FileHelper fileHelper = new FileHelper();

        assertEquals(550, fileHelper.getAwait(), "Await from default config");
        fileHelper.getConfigSummary();

        MatcherAssert.assertThat(
                "Info summary",
                fileHelper.getConfigSummary(),
                StringContains.containsString("""
                        FileHelper:
                            await=550"""));
    }

    @Test
    void testNoOptionalAwaitConfig() {
        System.setProperty(PROPERTY, "nooptional");
        FileHelper fileHelper = new FileHelper();

        assertEquals(2000, fileHelper.getAwait(), "Default await used");
    }

}

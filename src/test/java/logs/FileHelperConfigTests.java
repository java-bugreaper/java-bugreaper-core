package logs;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.modules.filehelper.FileHelper;
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
    }

    @Test
    void testNoOptionalAwaitConfig() {
        System.setProperty(PROPERTY, "nooptional");
        FileHelper fileHelper = new FileHelper();

        assertEquals(2000, fileHelper.getAwait(), "Default await used");
    }

}

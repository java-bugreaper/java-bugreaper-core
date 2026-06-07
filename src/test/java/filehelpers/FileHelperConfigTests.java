package filehelpers;

import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.modules.filehelper.FileHelper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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


        MatcherAssert.assertThat(
                "Info summary",
                fileHelper.getConfigSummary(),
                StringContains.containsString("""
                        FileHelper:
                            await=550
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
                            maxFileSize=1048576"""));
    }

}

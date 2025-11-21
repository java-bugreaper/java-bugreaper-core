package logs;

import io.bugreaper.modules.filehelper.FileHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FileHelperConfigTests {

    public static final String PROPERTY = "bugreaperEnv";

    @BeforeEach
    void copyConfig() {
        System.clearProperty(PROPERTY);
    }

    @Test
    void testDefaultConfig() {
        FileHelper fileHelper = new FileHelper();

        assertEquals(550, fileHelper.getAwait(), "Await from default config");
        fileHelper.getSummary();
    }

    @Test
    void testNoOptionalAwaitConfig() {
        System.setProperty(PROPERTY, "nooptional");
        FileHelper fileHelper = new FileHelper();

        assertEquals(2000, fileHelper.getAwait(), "Default await used");
    }

}

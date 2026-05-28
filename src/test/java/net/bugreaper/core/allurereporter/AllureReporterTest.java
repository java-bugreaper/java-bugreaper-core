package net.bugreaper.core.allurereporter;

import ch.qos.logback.classic.Level;
import net.bugreaper.core.utils.LogWatcher;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static net.bugreaper.core.allurereporter.AllureReporter.*;
import static org.hamcrest.MatcherAssert.assertThat;


@SuppressWarnings("squid:S2699")
class AllureReporterTest {


    @Test
    void testReporter() {
        LogWatcher logWatcher = new LogWatcher("net.bugreaper.core.allurereporter.AllureReporter", Level.INFO);
        String expectedLog = "=====test=====";

        reporter("test");

        assertThat(
                "Check Actual list log table",
                logWatcher.getLoggedEvents(Level.INFO).toString(),
                StringContains.containsString(expectedLog));
    }

    @Test
    void testBasicAllureWork() {
        String type = "text/plain";
        ArrayList<String> actualList = new ArrayList<>();

        addStepAttachment("text", "some data");

        attachFromFile("description message", "files/test.txt");
        attachFromFile("description message", type, "files/test.txt");
        attachFromFileNoStep("description message", "files/test.txt");

        attachJson("text", "{}");
        attachFromList("name_2", actualList);

        attachCanBeNull("name_1", null);
        attachCanBeNull("name_1", "not_null");

    }


}

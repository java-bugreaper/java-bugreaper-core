package net.bugreaper.core.allurereporter;

import net.bugreaper.core.filereaders.FileReader;
import net.bugreaper.core.mappers.StringMappers;
import io.qameta.allure.Allure;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.bugreaper.core.filereaders.FileReader.readTextFromFile;
import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class AllureReporter {

    private AllureReporter() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureReporter.class);

    private static final String TYPE_JSON = "application/json";
    private static final String TYPE_TEXT = "text/plain";

    /**
     * Method to create STEP with some important message
     *
     * @param message text message
     */
    @Step("=={message}==")
    public static void reporter(@Param(mode = HIDDEN) String message) {
        LOGGER.info("====={}=====", message);
    }

    /**
     * Method to create STEP with custom attachment
     *
     * @param message    text message for step
     * @param attachment attachment text
     */
    @Step("=> {message}")
    public static void addStepAttachment(@Param(mode = HIDDEN) String message, @Param(mode = HIDDEN) String attachment) {
        Allure.addAttachment(message, TYPE_TEXT, attachment);
    }

    @Step("=> {description}")
    public static void attachFromFile(@Param(mode = HIDDEN) String description, String path) {
        Allure.addAttachment(description, TYPE_JSON, readTextFromFile(path));
    }

    @Step("=> {description}")
    public static void attachFromFile(@Param(mode = HIDDEN) String description, String type, String path) {
        Allure.addAttachment(description, type, readTextFromFile(path));
    }

    public static void attachFromFileNoStep(String name, String path) {
        Allure.addAttachment(name, TYPE_JSON, readTextFromFile(path));
    }

    public static void createHtmlAllureAttachment(String name, String attachment) {
        Allure.addAttachment(name, "text/html", attachment, ".html");
    }

    public static void attachCanBeNull(String name, String result) {
        if (result == null) {
            result = "null";
        }
        Allure.addAttachment(name, TYPE_JSON, result);
    }

    public static void attachJson(String name, String content) {
        Allure.addAttachment(name, TYPE_JSON, content);
    }

    public static void attachFromList(String name, List<String> list) {
        Allure.addAttachment(name, TYPE_JSON, StringMappers.listToString(list));
    }


    /**
     * Method to create attach from CSV (for decision table and test cases)
     *
     * @param name     attachmentName
     * @param filePath path in resources
     */
    public static void attachFromCsv(String name, String filePath) {
        AllureBuilder.reportHtmlCsvCases(name, FileReader.readCsvToArray(filePath));
    }

}
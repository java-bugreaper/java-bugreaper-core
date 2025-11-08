package io.bugreaper.core.assertions;

import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.hamcrest.Matcher;
import org.opentest4j.AssertionFailedError;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;

import static io.bugreaper.core.assertions.JsonAsserts.*;
import static io.bugreaper.core.mappers.StringMappers.listToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ListAsserts {

    private ListAsserts() {
        throw new IllegalStateException("Utility class");
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(ListAsserts.class);
    private static final String LIST_LOG_MESSAGE = "Start assert for list:\n{}";
    private static final String ASSERT_LOG_MESSAGE = "Assert log: {}";


    // List asserts

    public static void customStringMatcherInList(Matcher<String> matcher, List<String> actualList) {

        StringBuilder trace = listAsserterBuilder(matcher, actualList);

        if (trace != null) {
            assertionMessage(trace, matcher.toString(), actualList, "match to");
        }
    }

    public static void equalsStringInList(String expect, List<String> actualList) {

        StringBuilder trace = listAsserterBuilder(is(expect), actualList);

        if (trace != null) {
            assertionMessage(trace, expect, actualList, "equals to string");
        }
    }

    public static void containsStringInList(String expectedPart, List<String> actualList) {

        StringBuilder trace = listAsserterBuilder(containsString(expectedPart), actualList);

        if (trace != null) {
            assertionMessage(trace, expectedPart, actualList, "contain substring");
        }
    }

    // List JSON asserts

    public static void isJsonTypeInList(List<String> actualList) {

        StringBuilder trace = listJsonTypeBuilder(actualList);

        if (trace != null) {
            assertionMessage(trace, "", actualList, "with type JSON");
        }

    }

    public static void equalsJsonInList(String expectedJson, List<String> actualList) {

        assertValidJson(expectedJson);
        
        StringBuilder trace = listJsonAsserterBuilder(expectedJson, actualList, JSONCompareMode.STRICT);

        if (trace != null) {
            assertionMessage(trace, expectedJson, actualList, "equal to JSON");
        }

    }

    public static void containsJsonInList(String expectedJsonPart, List<String> actualList) {

        assertValidJson(expectedJsonPart);

        StringBuilder trace = listJsonAsserterBuilder(expectedJsonPart, actualList, JSONCompareMode.LENIENT);

        if (trace != null) {
            assertionMessage(trace, expectedJsonPart, actualList, "contains JSON");
        }

    }

    public static void jsonSchemaCheckInList(String expectedSchema, List<String> actualBodiesList) {

        assertValidJson(expectedSchema);

        StringBuilder trace = listJsonSchemaBuilder(expectedSchema, actualBodiesList);

        if (trace != null) {
            assertionMessage(trace, expectedSchema, actualBodiesList, "with valid JSON Schema");
        }

    }

    private static void assertionMessage(StringBuilder trace, String expectedObject, List<String> actualList, String modifier){

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Actual list:\n{}", listToString(actualList));
        }

        throw new AssertionFailedError(
                MessageFormat.format("There is no elements in the list {0}:\n{1}\n{2}",
                        modifier, expectedObject, trace));
    }

    public static void assertCountElementsInList(int expectedCnt, List<String> actualList) {

        assertAll(() -> assertEquals(
                expectedCnt,
                actualList.size(),
                "Count of elements in list not equal: " + expectedCnt
        ));
    }

    // List builders

    private static StringBuilder listAsserterBuilder(Matcher<String> matcher, List<String> actualList) {

        StringBuilder trace = traceInit(actualList);

        for (String actual : actualList) {
            try {
                assertThat(actual, matcher);
                return null;
            } catch (AssertionError ex) {
                trace = traceBuilder(trace, ex.getMessage());
            }
        }
        return trace;
    }

    private static StringBuilder listJsonTypeBuilder(List<String> actualList) {

        StringBuilder trace = traceInit(actualList);

        for (String actual : actualList) {
            try {
                assertValidJson(actual);
                return null;
            } catch (AssertionError | IllegalArgumentException ex) {
                trace = traceBuilder(trace, ex.getMessage());
            }
        }

        return trace;
    }

    private static StringBuilder listJsonAsserterBuilder(String expectedAct, List<String> actualList, JSONCompareMode compareMode) {

        StringBuilder trace = traceInit(actualList);

        for (String actual : actualList) {
            try {
                assertJsonMethod(expectedAct, actual, compareMode);
                return null;
            } catch (AssertionError | IllegalArgumentException ex) {
                trace = traceBuilder(trace, ex.getMessage());
            }
        }

        return trace;
    }

    private static StringBuilder listJsonSchemaBuilder(String expectedSchema, List<String> actualList) {

        StringBuilder trace = traceInit(actualList);

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

        int num = 0;
        for (String actual : actualList) {
            try {
                validationJsonSchemaMethod(expectedSchema, actual, factory, false);
                return null;
            } catch (AssertionError | IllegalArgumentException ex) {
                num = num + 1;
                trace = traceNumBuilder(trace, num, ex.getMessage());
            }
        }

        return trace;
    }

    private static StringBuilder traceInit(List<String> actualList) {
        LOGGER.debug(LIST_LOG_MESSAGE, actualList);
        return new StringBuilder();
    }

    private static StringBuilder traceBuilder(StringBuilder trace, String exceptionString) {
        LOGGER.debug(ASSERT_LOG_MESSAGE, exceptionString);
        return trace.append("\n-----------\n").append(exceptionString).append("\n");
    }

    private static StringBuilder traceNumBuilder(StringBuilder trace, int num,  String exceptionString) {
        LOGGER.debug(ASSERT_LOG_MESSAGE, exceptionString);
        return trace.append("\n-----------").append(num).append("\n").append(exceptionString).append("\n");
    }

}

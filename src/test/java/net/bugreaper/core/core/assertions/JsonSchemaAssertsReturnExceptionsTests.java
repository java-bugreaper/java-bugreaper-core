package net.bugreaper.core.core.assertions;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;

import static net.bugreaper.core.assertions.ListAsserts.isJsonTypeInList;
import static net.bugreaper.core.assertions.ListAsserts.jsonSchemaCheckInList;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SuppressWarnings("java:S5976")
class JsonSchemaAssertsReturnExceptionsTests {


    private final String jsonValidation = """
            {
                "type": "object",
                "properties": {
                     "id_wrong": {
                     "type": "integer"
                     }
                },
                "additionalProperties": false
            }""";


    @Test
    @SuppressWarnings("squid:S2699")
    void testAssertJsonSchemaPass() {
        String notValid = """
                {
                  "id": "test"
                }""";

        String actual = """
                {
                  "id_wrong": 1
                }""";

        ArrayList<String> actualList = new ArrayList<>();

        actualList.add(notValid);
        actualList.add(actual);

        jsonSchemaCheckInList(jsonValidation, actualList);

    }

    @Test
    void testAssertJsonSchemaFailed1() {

        String actual = """
                {
                  "id": 1
                }""";

        ArrayList<String> actualList = new ArrayList<>();

        actualList.add(actual);

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                jsonSchemaCheckInList(jsonValidation, actualList));

        MatcherAssert.assertThat(
                "Exception on failed schema",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list with valid JSON Schema"));

        MatcherAssert.assertThat(
                "Exception text on failed schema",
                exception.getMessage(),
                StringContains.containsString("$.id: is not defined in the schema and the schema does not allow additional properties"));
    }

    @Test
    void testAssertJsonSchemaWrongTypeString() {

        String actual = "some text";

        ArrayList<String> actualList = new ArrayList<>();

        actualList.add(actual);


        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                jsonSchemaCheckInList(jsonValidation, actualList));

        MatcherAssert.assertThat(
                "Exception on failed schema (string provided)",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list with valid JSON Schema"));

        MatcherAssert.assertThat(
                "Exception text on failed schema (string provided)",
                exception.getMessage(),
                StringContains.containsString("Schema assert failed (Actual body not JSON)"));
    }


    @Test
    void testAssertJsonSchemaWrongTypeArray() {

        String actual = """
                [{
                  "id_wrong": 1
                }]""";

        ArrayList<String> actualList = new ArrayList<>();

        actualList.add(actual);


        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                jsonSchemaCheckInList(jsonValidation, actualList));

        MatcherAssert.assertThat(
                "Exception on failed schema (array provided)",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list with valid JSON Schema"));

        MatcherAssert.assertThat(
                "Exception text on failed schema (array provided)",
                exception.getMessage(),
                StringContains.containsString("$: array found, object expected"));
    }


    @Test
    void testAssertIsJsonType() {

        String actual = """
                {"wrong": 1""";

        ArrayList<String> actualList = new ArrayList<>();

        actualList.add(actual);

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                isJsonTypeInList(actualList));

        MatcherAssert.assertThat(
                "Exception on failed schema (array provided)",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list with type JSON"));

    }
}

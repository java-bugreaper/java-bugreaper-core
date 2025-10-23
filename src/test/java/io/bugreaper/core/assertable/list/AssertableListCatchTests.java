package io.bugreaper.core.assertable.list;


import io.bugreaper.core.assertable.AssertableStringList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;

import static io.bugreaper.core.assertable.stringlist.ListOperators.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("squid:S5778")
class AssertableListCatchTests {


    String json = """
                {
                  "status": 11,
                  "name": "Alex",
                  "array":[
                    {"id": 1},
                    {"id": 2}
                  ]
                }""";


    @Test
    void testListAssertsJsonContainsCatch() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);


        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                listForTest
                        .testInLIst(jsonContainsInList("""
                  {
                    "status": 12
                  }""")));

        MatcherAssert.assertThat(
                "Exception on failed validation JSON Schema",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list contains JSON:"));

    }

    @Test
    void testListAssertsJsonEqualCatch() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                listForTest
                        .testInLIst(jsonEqualsInList("""
                  {
                    "status": 12
                  }""")));

        MatcherAssert.assertThat(
                "Exception on failed validation JSON type",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list equal to JSON:"));


    }

    @Test
    void testListAssertsJsonTypeCatch() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        var listForTest = new AssertableStringList(actualList);


        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                listForTest
                        .testInLIst(isJsonType()));

        MatcherAssert.assertThat(
                "Exception on failed validation JSON type",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list with type JSON:"));
    }


    @Test
    void testListAssertsJsonSchemaCatch() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                listForTest
                        .testInLIst(jsonSchemaCheckInList("""
                        {
                          "type": "object",
                          "required": [
                            "status-list",
                            "name"
                          ],
                          "additionalProperties": false
                        }""")));

        MatcherAssert.assertThat(
                "Exception on failed validation JSON Schema",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list with valid JSON Schema:"));
    }


    @Test
    void testListAssertsJsonContainsCatchInput() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);


        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                listForTest
                        .testInLIst(jsonContainsInList("not json")));

        MatcherAssert.assertThat(
                "Exception on failed validation JSON contains input",
                exception.getMessage(),
                StringContains.containsString("Wrong JSON/JSONArray format"));

    }

    @Test
    void testListAssertsJsonEqualCatchInput() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                listForTest
                        .testInLIst(jsonEqualsInList("not json")));

        MatcherAssert.assertThat(
                "Exception on failed validation JSON equal input",
                exception.getMessage(),
                StringContains.containsString("Wrong JSON/JSONArray format"));


    }


    @Test
    void testListAssertsJsonSchemaCatchInput() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                listForTest
                        .testInLIst(jsonSchemaCheckInList("not json")));

        MatcherAssert.assertThat(
                "Exception on failed validation JSON Schema input",
                exception.getMessage(),
                StringContains.containsString("Wrong JSON/JSONArray format"));
    }

}

package net.bugreaper.core.assertions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static net.bugreaper.core.assertions.ListAsserts.*;


@SuppressWarnings("squid:S2699")
class ListAssertionsPassedTests {

    @Test
    void testStringEqualInList4() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("test");
        actualList.add("DO not CHECK");

        equalsStringInList("test", actualList);

    }

    @Test
    void testStringNotEqualInList4() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("test1");
        actualList.add("1test");

        notEqualsStringInList("test", actualList);
    }

    @Test
    void testStringListCount() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("logs/test");

        assertCountElementsInList(3, actualList);

    }

    @Test
    void testJsonEqualListCount() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("""
                {"id": 3}""");
        actualList.add("""
                {"id": 4}""");

        equalsJsonInList("""
                {"id": 3}""", actualList);

    }

    @Test
    void testJsonContainsListCount() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("""
                {"id": 3, "test": 2}""");

        containsJsonInList("""
                {"id": 3}""", actualList);

    }


    @Test
    void testJsonSchemaCheckListCount() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("""
                {"id": 3}""");

        jsonSchemaCheckInList("""
                            {
                            "type": "object",
                            "properties": {
                                 "id": {
                                 "type": "integer"
                                 }
                            },
                            "additionalProperties": false
                        }""",
                actualList);
    }

    @Test
    void testIsJsonType() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("""
                {"id": 3}""");

        isJsonTypeInList(actualList);
    }

}

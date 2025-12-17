package net.bugreaper.core.core.assertable.list;


import net.bugreaper.core.assertable.AssertableStringList;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("squid:S2699")
class AssertableListTests {


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
    void testListAssertsAndGrabLastCount() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("test");
        var listForTest = new AssertableStringList(actualList);

        String er = listForTest
                .seeListHasExactlyCount(3)
                .seeListAnyEquals("dummy")
                .seeListAnyContains("tes")
                .seeListAnyMatcher(startsWithIgnoringCase("DU"))
                .seeListAnyMatcher(stringContainsInOrder("te", "st"))
                .grabLastElement();

        assertEquals("test", er,
                "Grab last element ");
    }

    @Test
    void testListAssertsAndGrabLastNullCount() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        var listForTest = new AssertableStringList(actualList);

        String er = listForTest
                .seeListAnyEquals("dummy")
                .seeListAnyEquals(null)
                .seeListAnyMatcher(matchesRegex("..mmy"))
                .grabLastElement();

        assertNull(er, "Grab last element");
    }

    @Test
    void testListAssertsJsonAsserts() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        listForTest
                .seeListAnyEqualsJson(json)
                .seeListAnyContainsJson("""
                  {
                    "status": 11
                  }""")
                .seeListAnyJsonMatchSchema("""
                        {
                          "type": "object",
                          "required": [
                            "status",
                            "name",
                            "array"
                          ],
                          "additionalProperties": false,
                          "properties": {
                            "status": {
                              "type": "integer"
                            },
                            "name": {
                              "type": "string"
                            },
                            "array": {
                              "type": "array"
                            }
                          }
                        }""")
                .seeListAnyJsonType();

    }

    @Test
    void testListAssertsJsonFromFileAsserts() {
        ArrayList<String> actualList = new ArrayList<>();


        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        listForTest
                .seeListAnyJsonType()
                .seeListAnyContainsJson(Path.of("testdata/json/json_contains.json"))
                .seeListAnyJsonMatchSchema(Path.of("testdata/json/schema_1.json"))
                .seeListAnyEqualsJson(Path.of("testdata/json/json_equal.json"))
                .seeListAnyJsonType();

    }

}

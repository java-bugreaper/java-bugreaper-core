package io.bugreaper.core.assertable.list;


import io.bugreaper.core.assertable.AssertableStringList;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;


import static io.bugreaper.core.assertable.stringlist.ListOperators.*;
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
                .testInLIst(elementsCountInList(3))
                .testInLIst(stringEqualsInList("dummy"))
                .testInLIst(stringContainsInList("test"))
                .testInLIst(customStringMatcherInList(startsWithIgnoringCase("DU")))
                .testInLIst(customStringMatcherInList(stringContainsInOrder("te", "st")))
                .extractFromList(grabLastElementInList());

        assertAll(() -> assertEquals("test", er,
                "Grab last element "));
    }

    @Test
    void testListAssertsAndGrabLastNullCount() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        var listForTest = new AssertableStringList(actualList);

        String er = listForTest
                .testInLIst(stringEqualsInList("dummy"))
                .testInLIst(stringEqualsInList(null))
                .testInLIst(customStringMatcherInList(matchesRegex("..mmy")))
                .extractFromList(grabLastElementInList());

        assertAll(() -> assertNull(er, "Grab last element "));
    }

    @Test
    void testListAssertsJsonAsserts() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        listForTest
                .testInLIst(isJsonType())
                .testInLIst(jsonContainsInList("""
                  {
                    "status": 11
                  }"""))
                .testInLIst(jsonEqualsInList(json))
                .testInLIst(jsonSchemaCheckInList("""
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
                        }"""));

    }

    @Test
    void testListAssertsJsonFromFileAsserts() {
        ArrayList<String> actualList = new ArrayList<>();


        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        listForTest
                .testInLIst(isJsonType())
                .testInLIst(jsonContainsInList(Path.of("testdata/json/json_contains.json")))
                .testInLIst(jsonEqualsInList(Path.of("testdata/json/json_equal.json")))
                .testInLIst(jsonSchemaCheckInList(Path.of("testdata/json/schema_1.json")));

    }

}

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
                .verifyInList(hasExactCount(3))
                .verifyInList(stringEqual("dummy"))
                .verifyInList(stringContains("test"))
                .verifyInList(stringMatchesCustom(startsWithIgnoringCase("DU")))
                .verifyInList(stringMatchesCustom(stringContainsInOrder("te", "st")))
                .extractFromList(grabLastElement());

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
                .verifyInList(stringEqual("dummy"))
                .verifyInList(stringEqual(null))
                .verifyInList(stringMatchesCustom(matchesRegex("..mmy")))
                .extractFromList(grabLastElement());

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
                .verifyInList(isJsonType())
                .verifyInList(jsonContains("""
                  {
                    "status": 11
                  }"""))
                .verifyInList(jsonEqual(json))
                .verifyInList(jsonMatchesSchema("""
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
                .verifyInList(isJsonType())
                .verifyInList(jsonContains(Path.of("testdata/json/json_contains.json")))
                .verifyInList(jsonEqual(Path.of("testdata/json/json_equal.json")))
                .verifyInList(jsonMatchesSchema(Path.of("testdata/json/schema_1.json")));

    }

}

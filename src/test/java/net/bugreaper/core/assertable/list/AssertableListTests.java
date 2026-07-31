package net.bugreaper.core.assertable.list;


import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import net.bugreaper.core.assertable.AssertableStringList;
import net.bugreaper.core.utils.AllureAssert;
import net.bugreaper.core.utils.AllureResultLoader;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("squid:S2699")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    String jsonWr = """
                {
                  "status": 12,
                  "name": "Anna",
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
    void testListGrabAllList() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        var listForTest = new AssertableStringList(actualList);

        List<String> er = listForTest
                .seeListHasExactlyCount(2)
                .grabLikeList();

        assertEquals("dummy", er.get(0));
        assertNull(er.get(1));
    }

    @Test
    void testListGreaterAndLess() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add("test");
        var listForTest = new AssertableStringList(actualList);

        listForTest
                .seeListHasExactlyCount(3)
                .seeListSizeIsGreaterThan(2)
                .seeListSizeIsLessThan(4);
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
        actualList.add(jsonWr);
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
    void testListAnyContainsExtendedJson() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(jsonWr);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        listForTest
                .seeListAnyEqualsJson(json)
                .seeListAnyContainsJson("""
                  {
                    "status": 11
                  }""")
                .seeListAnyContainsExtendedJson("""
                {
                  "status": 11,
                  "name": "Alex",
                  "array":[
                    {"id": 1},
                    {"id": 2}
                  ]
                }""");
    }


    @Test
    void testListAssertsJsonSubsetAsserts() {
        String actual = """
                {
                  "messages": [
                    {
                      "To": [
                        { "Address": "email2@mail.com", "Name": "Alex" },
                        { "Address": "email@mail.com", "Name": "John" }
                      ]
                    }
                  ]
                }""";

        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(actual);
        var listForTest = new AssertableStringList(actualList);

        listForTest
                .seeListAnyContainsJsonSubset("""
                  {
                  "messages": [
                    {
                      "To": [
                        { "Address": "email@mail.com" }
                      ]
                    }
                  ]
                }""")
                .seeListAnyJsonType();

    }

    @Test
    @Order(1)
    void testListAssertsJsonFromFileAsserts() {
        ArrayList<String> actualList = new ArrayList<>();


        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);

        createAssertList(actualList)
                .seeListAnyJsonType()
                .seeListAnyContainsJson(Path.of("testdata/json/json_contains.json"))
                .seeListAnyJsonMatchSchema(Path.of("testdata/json/schema_1.json"))
                .seeListAnyEqualsJson(Path.of("testdata/json/json_equal.json"))
                .seeListAnyJsonType();

    }

    @Step("Create list")
    AssertableStringList createAssertList(ArrayList<String> list){
        return new AssertableStringList(list);
    }

    @Test
    @Order(2)
    //can not work from UI check from console
    void allureForListCheck() {
        JsonNode result = AllureResultLoader.loadByTestName("testListAssertsJsonFromFileAsserts");

        AllureAssert.assertThat(result)
                .hasStep("Create list")
                .hasStep("↑(Assert) List should have element with JSON type")
                .hasStep("↑(Assert) List have JSON CONTAINS part:")
                .hasAttachment("testdata/json/json_contains.json", """
                        {
                          "name": "Alex",
                          "array":[
                            {"id": 2},
                            {"id": 1}
                          ]
                        }""")
                .hasStep("↑(Assert) List should have JSON MATCH SCHEMA")
                .hasAttachment("testdata/json/schema_1.json")
                .hasStep("↑(Assert) List should have JSON EQUAL to:");

    }


}

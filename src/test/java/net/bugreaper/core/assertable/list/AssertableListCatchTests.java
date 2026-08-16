package net.bugreaper.core.assertable.list;


import net.bugreaper.core.assertable.AssertableStringList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.startsWithIgnoringCase;
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


        Throwable exception = assertThrows(AssertionError.class, () ->
                listForTest
                        .seeListAnyContainsJson("""
                  {
                    "status": 12
                  }"""));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list contains JSON:"));

    }

    @Test
    void testListAssertsJsonContainsExtendedCatch() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);


        Throwable exception = assertThrows(AssertionError.class, () ->
                listForTest
                        .seeListAnyContainsExtendedJson("""
                  {
                      "status:>": 12,
                      "name:like": "nn"
                  }"""));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list contains JSON (EXTENDED):"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        -----------
                        JSON comparison failed:
                        • status: expected >[12] but was [11]
                        • name: expected like [nn] but was [Alex]"""));

    }

    @Test
    void testListAssertsJsonEqualCatch() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(AssertionError.class, () ->
                listForTest
                        .seeListAnyEqualsJson("""
                  {
                    "status": 12
                  }"""));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list equal to JSON:"));


    }

    @Test
    void testListAssertsJsonTypeCatch() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        var listForTest = new AssertableStringList(actualList);


        Throwable exception = assertThrows(AssertionError.class, listForTest::seeListAnyJsonType);

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

        Throwable exception = assertThrows(AssertionError.class, () ->
                listForTest
                        .seeListAnyJsonMatchSchema("""
                        {
                          "type": "object",
                          "required": [
                            "status-list",
                            "name"
                          ],
                          "additionalProperties": false
                        }"""));

        MatcherAssert.assertThat(
                "Exception on failed validation JSON Schema",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list with valid JSON Schema:"));
    }

    @Test
    void testListAssertsJsonSubsetAssertsCatch() {
        String actual = """
                {
                  "messages": [
                    {
                      "To": [
                        { "Address": "email1@mail.com", "Name": "Alex" },
                        { "Address": "email2@mail.com", "Name": "John" }
                      ]
                    }
                  ]
                }""";

        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(actual);
        var listForTest = new AssertableStringList(actualList);


        Throwable exception = assertThrows(AssertionError.class, () ->
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
                        .seeListAnyJsonType());

        MatcherAssert.assertThat(
                "Exception on failed validation JSON Schema",
                exception.getMessage(),
                StringContains.containsString("There is no elements in the list contains JSON(ignoring extensive array elements):"));
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
                        .seeListAnyContainsJson("not json"));

        assertEquals("""
                        Invalid JSON or JSON array format (strict):
                        not json""",
                exception.getMessage());

    }

    @Test
    void testListAssertsJsonContainsSubsetCatchInput() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(null);
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);


        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                listForTest
                        .seeListAnyContainsJsonSubset("not json"));

        assertEquals("""
                        Invalid JSON or JSON array format (strict):
                        not json""",
                exception.getMessage());

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
                        .seeListAnyEqualsJson("not json"));

        assertEquals("""
                        Invalid JSON or JSON array format (strict):
                        not json""",
                exception.getMessage());


    }

    @Test
    void testListAssertsJsonSchemaCatchInput() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                listForTest
                        .seeListAnyJsonMatchSchema("not json"));

        assertEquals("""
                        Invalid JSON or JSON array format (strict):
                        not json""",
                exception.getMessage());
    }

    @Test
    void testListAssertsListHasExactlyCountInput() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add(json);
        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(AssertionError.class, () ->
                listForTest
                        .seeListHasExactlyCount(1));

        MatcherAssert.assertThat(
                "Exception on failed hasExactlyCount",
                exception.getMessage(),
                StringContains.containsString("Count of elements in list not equal: 1 ==> expected: <1> but was: <2>"));
    }

    @Test
    void testListAssertsListMatcher() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(AssertionError.class, () ->
                listForTest
                        .seeListAnyMatcher(startsWithIgnoringCase("SU")));

        MatcherAssert.assertThat(
                "Exception on failed custom matcher",
                exception.getMessage(),
                StringContains.containsString("""
                        There is no elements in the list match to:
                        a string starting with "SU" ignoring case"""));
    }

    @Test
    void testGrabLastElementFailed() {
        ArrayList<String> actualList = new ArrayList<>();

        var listForTest = new AssertableStringList(actualList);

        Throwable exception = assertThrows(IllegalArgumentException.class, listForTest::grabLastElement);

        assertEquals("List is empty",
                exception.getMessage());
    }

}

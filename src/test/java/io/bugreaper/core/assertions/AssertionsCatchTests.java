package io.bugreaper.core.assertions;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static io.bugreaper.core.assertions.Asserts.*;
import static io.bugreaper.core.assertions.JsonAsserts.*;
import static org.junit.jupiter.api.Assertions.*;

class AssertionsCatchTests {

    @Test
    void testAssertGreaterFailed() {
        assertThrows(AssertionError.class, () ->
                assertGreater(2, 2));
    }

    @Test
    void testAssertLessFailed() {
        assertThrows(AssertionError.class, () ->
                assertLess(5, 5));
    }

    @Test
    void testAssertStringFailed() {
        String actual = "str1";
        String expected = "str2";

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertStrings(expected, actual));

        MatcherAssert.assertThat(
                "Exception on failed assert String",
                exception.getMessage(),
                StringContains.containsString("expected: <str2> but was: <str1>"));
    }

    @Test
    void testAssertBooleansFailed() {

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertBooleans(true, false));

        MatcherAssert.assertThat(
                "Exception on failed assert Booleans",
                exception.getMessage(),
                StringContains.containsString("expected: <true> but was: <false>"));
    }

    @Test
    void testAssertJsonContainsArrayActualHasExtensibleFailed() {
        String actual = """
                {
                  "id": 1,
                  "array": [1,2,3]
                }""";

        String expected = """
                {
                  "id": 1,
                  "array": [1,2]
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                containsJson(expected, actual));

        assertEquals("array[]: Expected 2 values but got 3",
                exception.getMessage(),
                "Exception on failed Contains JSON with array assertion (extensible element)");
    }

    @Test
    void testAssertJsonFailedTypes() {
        String actual = """
                {
                  "id": 1010
                }""";

        String expected = """
                {
                  "id": "1010"
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                containsJson(expected, actual));

        assertEquals("""
                        id
                        Expected: 1010
                             got: 1010
                        """,
                exception.getMessage(),
                "Exception on failed JSON assertion with same data in different types");
    }

    @Test
    void testAssertJsonFailed() {
        String actual = """
                {
                  "id": 1010,
                  "name": "Alex"
                }""";

        String expected = """
                {
                  "id": 1010
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertJson(expected, actual));

        assertEquals("""
                        
                        Unexpected: name
                        """,
                exception.getMessage(),
                "Exception on failed equal JSON assertion");
    }

    @Test
    void testContainsJsonFailed() {
        String actual = """
                {
                  "id": 1010,
                  "name": "Alex"
                }""";

        String expected = """
                {
                  "id": 1011
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                containsJson(expected, actual));

        assertEquals("""
                        id
                        Expected: 1011
                             got: 1010
                        """,
                exception.getMessage(),
                "Exception on failed contains JSON assertion");
    }

    @Test
    void testAssertNoStrictOrderJsonFailed() {
        String actual = """
                {
                  "id": 1,
                  "text": "test",
                  "array": [3,2,1]
                }""";

        String expected = """
                {
                  "id": 1,
                  "array": [1,2,3]
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertNoStrictOrderJson(expected, actual));

        assertEquals("""
                        
                        Unexpected: text
                        """,
                exception.getMessage(),
                "Exception on failed assertNoStrictOrderJson with extensible key");

    }

    @Test
    void testStrictOrderJsonFailed() {
        String actual = """
                {
                  "idAll": 1,
                  "array": [
                    {
                      "id": 901,
                      "test": "one"
                    },
                    {
                      "id": 902,
                      "test": "two"
                    }
                  ]
                }""";

        String expected = """
                {
                  "idAll": 1,
                  "array": [
                    {
                      "id": 902,
                      "test": "two"
                    },
                    {
                      "id": 901,
                      "test": "one"
                    }
                  ]
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                containsStrictOrderJson(expected, actual));

        assertEquals("""
                        array[0].id
                        Expected: 902
                             got: 901
                         ; array[0].test
                        Expected: two
                             got: one
                         ; array[1].id
                        Expected: 901
                             got: 902
                         ; array[1].test
                        Expected: one
                             got: two
                        """,
                exception.getMessage(),
                "Exception on failed STRICT ORDER JSON assertion");
    }

    @Test
    void testStrictArrayJsonFailed() {
        String actual = """
                {
                  "idAll": 1,
                  "array": [
                    {
                      "id": 901,
                      "test": "one"
                    },
                    {
                      "id": 902,
                      "test": "two"
                    }
                  ]
                }""";

        String expected = """
                {
                  "idAll": 1,
                  "array": [
                    {
                      "id": 902,
                      "test": "two"
                    }
                  ]
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                containsStrictOrderJson(expected, actual));

        assertEquals("array[]: Expected 1 values but got 2",
                exception.getMessage(),
                "Exception on failed STRICT ARRAY JSON assertion");
    }

    @Test
    void testContainsJsonWrongType() {
        String actual = """
                {
                  "id": 1010
                }""";

        String expected = """
                 1010""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                containsJson(expected, actual));

        containsStrings("got: a JSON object",
                exception.getMessage());
    }


    @Test
    void testAssertJsonWrongType() {
        String actual = """
                {
                  "id": 1010
                }""";

        String expected = """
                 1010""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertJson(expected, actual));

        assertTrue(
                exception.getMessage().contains("got: a JSON object"),
                "Exception for broken JSON");
    }

    @Test
    void testContainsJsonWrongActualType() {

        String all = """
                 1010, 77}""";

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                containsJson("77", all));

        assertEquals("org.json.JSONException: Unparsable JSON string: " + all,
                exception.getMessage(),
                "Exception for contains JSON type error");
    }

    @Test
    void testAssertJsonNotContains() {

        String actual = """
                {
                  "id": 1,
                  "test": 2
                }""";
        String unexpected = """
                {
                  "id": 1
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertJsonNotContains(unexpected, actual));

        assertEquals("Actual Json contains unexpected Json part:\n" + unexpected,
                exception.getMessage(),
                "Exception for assert JSON not contains assert");
    }

    @Test
    void testAssertJsonWrongExpectedType() {

        String actual = """
                {
                  "id": 1010
                }""";
        String expected = "1010}";

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                assertJson(expected, actual));

        assertEquals("org.json.JSONException: Unparsable JSON string: " + expected,
                exception.getMessage(),
                "Exception for assert JSON type error");
    }


    @Test
    void testJsonTest() {

        String actual = """
                {
                  "id": 1010,
                }""";

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                checkJson(actual));

        assertEquals("Wrong JSON/JSONArray format:\n" + actual,
                exception.getMessage(),
                "Exception for assert JSON type error");
    }

}

package io.bugreaper.core.assertions;

import org.junit.jupiter.api.Test;

import static io.bugreaper.core.assertions.Asserts.assertBooleans;
import static io.bugreaper.core.assertions.Asserts.assertStrings;
import static io.bugreaper.core.assertions.JsonAsserts.*;


@SuppressWarnings({"squid:S2699","java:S5976"})
class AssertionsPassedTests {

    @Test
    void testAssertString() {
       assertStrings("str1", "str1");
    }

    @Test
    void testAssertBooleans() {
        assertBooleans(true, true);
    }

    @Test
    void testAssertJsonContainsArrayPass() {
        String actual = """
                {
                  "id": 1,
                  "array": [1,2,3]
                }""";

        String expected = """
                {
                  "id": 1,
                  "array": [1,2,3]
                }""";

        containsJson(expected, actual);
    }

    @Test
    void testAssertJsonEmulateEmptyBody() {
        String actual = """
                {
                  "id": 11
                }""";

        String expected = """
                {}""";

        containsJson(expected, actual);
    }

    @Test
    void testAssertJsonEmulateEmptyBody2() {
        String actual = """
                {}""";

        String expected = """
                {}""";

        containsJson(expected, actual);
    }

    @Test
    void testAssertNoStrictOrderJsonPass() {
        String actual = """
                {
                  "id": 1,
                  "array": [3,2,1]
                }""";

        String expected = """
                {
                  "id": 1,
                  "array": [1,2,3]
                }""";

        assertNoStrictOrderJson(expected, actual);
    }

    @Test
    void testStrictOrderJsonEqual() {
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

        containsStrictOrderJson(actual, actual);
    }



    @Test
    void testStrictOrderJsonContainArray() {
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
                  "array": [
                    {
                      "id": 901
                    },
                    {
                      "test": "two"
                    }
                  ]
                }""";

        containsStrictOrderJson(expected, actual);
    }

    @Test
    void testAssertJsonNotContains() {

        String actual = """
                {
                  "id": 1,
                  "test": 2
                }""";

        assertJsonNotContains("""
                {
                "id": 2
                }""", actual);

        assertJsonNotContains("""
                {
                "some": 1
                }""", actual);

    }

}

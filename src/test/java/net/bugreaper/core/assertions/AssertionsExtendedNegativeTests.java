package net.bugreaper.core.assertions;

import net.bugreaper.core.exceptions.JsonMappersException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.assertions.JsonAsserts.assertJsonsExtended;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SuppressWarnings({"squid:S2699", "java:S5976"})
class AssertionsExtendedNegativeTests {

    String actual = """
            {
             "user": "Alex",
             "num": 2,
             "array": [1, 2, 3]
            }""";

    @Test
    void brokenJsonTest() {

        Throwable exception = assertThrows(JsonMappersException.class, () ->
                assertJsonsExtended("""
                                {
                                 "user": "Alex",
                                }""",
                        actual));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Invalid JSON: Unexpected character"));

    }

    @Test
    void nullJsonTest() {

        Throwable exception = assertThrows(JsonMappersException.class, () ->
                assertJsonsExtended(null, actual));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        Invalid JSON: argument "content" is null"""));

    }

    @Test
    void wrongOperatorTest() {

        Throwable exception = assertThrows(JsonMappersException.class, () ->
                assertJsonsExtended("""
                                {
                                 "array:some": [1]
                                }""",
                        actual));

        assertEquals(
                """
                       Invalid extend setup: array: unsupported operator [some]""",
                exception.getMessage());
    }

    @Test
    void wrongArrayOperatorTest() {

        Throwable exception = assertThrows(JsonMappersException.class, () ->
                assertJsonsExtended("""
                                {
                                 "user:some": "Alex"
                                }""",
                        actual));

        assertEquals(
                """
                        Invalid extend setup: user: unsupported operator [some]""",
                exception.getMessage());
    }

    @Test
    void wrongOperatorDistinctValueTest() {

        Throwable exception = assertThrows(JsonMappersException.class, () ->
                assertJsonsExtended("""
                                {
                                 "array:distinct": 1
                                }""",
                        actual));

        assertEquals(
                """
                        Invalid extend setup: array: distinct operator requires boolean""",
                exception.getMessage());
    }

    @Test
    void wrongOperatorInValueTest() {

        Throwable exception = assertThrows(JsonMappersException.class, () ->
                assertJsonsExtended("""
                                {
                                 "array:in": 1
                                }""",
                        actual));

        assertEquals(
                """
                        Invalid extend setup: array: 'in' requires array""",
                exception.getMessage());
    }

    @Test
    void wrongOperatorNumericTest() {

        Throwable exception = assertThrows(JsonMappersException.class, () ->
                assertJsonsExtended("""
                                {
                                 "num:<": "test"
                                }""",
                        actual));

        assertEquals(
                """
                        Invalid extend setup: num: numeric compare failed""",
                exception.getMessage());
    }

}

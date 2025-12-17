package net.bugreaper.core.core.assertions;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.assertions.JsonAsserts.assertJsonNotContains;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JsonAssertsReturnExceptionTests {


    @Test
    void testAssertJsonNotContainsWrongType() {

        String actual = """
                {
                  "id": 1,
                  "test": 2
                }""";

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                assertJsonNotContains("string_text", actual));

        MatcherAssert.assertThat(
                "Exception on failed JSON type on not contains",
                exception.getMessage(),
                StringContains.containsString("Unparsable JSON string: string_text"));
    }

}

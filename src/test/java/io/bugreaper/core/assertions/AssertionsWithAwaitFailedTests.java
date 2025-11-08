package io.bugreaper.core.assertions;

import io.bugreaper.core.exceptions.AssertionWithAwaitFailedError;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static io.bugreaper.core.assertions.AssertsWithAwait.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SuppressWarnings("squid:S2699")
class AssertionsWithAwaitFailedTests {

    @Test
    void testAssertLess() {

        Throwable exception = assertThrows(AssertionWithAwaitFailedError.class, () ->
                assertLessThanExpectedWithAwait(2, 3, 101, "Goods"));

        MatcherAssert.assertThat(
                "Exception await less assert",
                exception.getMessage(),
                is("Goods expected to be LESS than <2> but got <3> within 101 milliseconds"));
    }

    @Test
    void testAssertGreater() {

        Throwable exception = assertThrows(AssertionWithAwaitFailedError.class, () ->
                assertGreaterThanExpectedWithAwait(3, 2, 102, "Goods"));

        MatcherAssert.assertThat(
                "Exception await greater assert",
                exception.getMessage(),
                is("Goods expected to be GREATER than <3> but got <2> within 102 milliseconds"));

    }

    @Test
    void testAssertEqualFailed() {

        Throwable exception = assertThrows(AssertionWithAwaitFailedError.class, () ->
                assertIntEqualsWithAwait(2, 3, 1000, "Goods"));

        MatcherAssert.assertThat(
                "Exception await equals assert",
                exception.getMessage(),
                is("Goods expected to be EXACTLY <2> but got <3> within 1 second"));

    }

    @Test
    void testAssertNotEmpty() {

        Throwable exception = assertThrows(AssertionWithAwaitFailedError.class, () ->
                assertNotEmptyWithAwait( 0, 1200, "Goods"));

        MatcherAssert.assertThat(
                "Exception await not empty assert",
                exception.getMessage(),
                is("Goods expected to be NOT <0> but got <0> within 1 second 200 milliseconds"));
    }

    @Test
    void testAssertEmpty() {

        Throwable exception = assertThrows(AssertionWithAwaitFailedError.class, () ->
                assertEmptyWithAwait( 1, 107, "Goods"));

        MatcherAssert.assertThat(
                "Exception await empty assert",
                exception.getMessage(),
                is("Goods expected to be <0> but got <1> within 107 milliseconds"));
    }

}

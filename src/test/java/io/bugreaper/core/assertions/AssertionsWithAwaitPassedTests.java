package io.bugreaper.core.assertions;

import org.junit.jupiter.api.Test;

import static io.bugreaper.core.assertions.AssertsWithAwait.*;


@SuppressWarnings("squid:S2699")
class AssertionsWithAwaitPassedTests {

    @Test
    void testAssertLess() {
        assertLessThanExpectedWithAwait(3, 2, 200, "Goods");
    }

    @Test
    void testAssertGreater() {
        assertGreaterThanExpectedWithAwait(1, 2, 200, "Goods");
    }

    @Test
    void testAssertEqual() {
        assertIntEqualsWithAwait(2, 2, 101, "Goods");
    }

    @Test
    void testAssertNotEmpty() {
        assertNotEmptyWithAwait(1, 200,  "Goods");
    }

    @Test
    void testAssertEmpty() {
        assertEmptyWithAwait(0, 200,  "Goods");
    }

}

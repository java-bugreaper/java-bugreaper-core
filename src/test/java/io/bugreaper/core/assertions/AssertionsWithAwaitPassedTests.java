package io.bugreaper.core.assertions;

import org.junit.jupiter.api.Test;

import static io.bugreaper.core.assertions.AssertsWithAwait.*;


@SuppressWarnings("squid:S2699")
class AssertionsWithAwaitPassedTests {

    @Test
    void testAssertLess() {
        assertLessWithAwait(2, 3, 200, "Goods");
    }

    @Test
    void testAssertGreater() {
        assertGreaterWithAwait(2, 1, 200, "Goods");
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

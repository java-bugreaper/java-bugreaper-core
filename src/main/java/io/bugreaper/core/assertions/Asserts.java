package io.bugreaper.core.assertions;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class Asserts {

    private Asserts() {
        throw new IllegalStateException("Utility class");
    }


    public static void assertStrings(String expectedString, String actualString) {
        assertEquals(expectedString, actualString);
    }

    public static void containsStrings(String expectedSubString, String actualString) {
        assertTrue(actualString.contains(expectedSubString));
    }

    public static void assertBooleans(boolean expected, boolean actual) {
        assertEquals(expected, actual);
    }

    public static void assertIntEquals(int expected, int actual) {
        assertEquals(expected, actual);
    }

    public static void assertGreaterThanExpected(int expectedMin, int actual) {
        assertTrue(actual > expectedMin);
    }

    public static void assertLessThanExpected(int expectedMax, int actual) {
        assertTrue(actual < expectedMax);
    }

    public static void assertNotEmpty(int actual) {
        assertNotEquals(0,  actual);
    }

    public static void assertEmpty(int actual) {
        assertEquals(0,  actual);
    }

}

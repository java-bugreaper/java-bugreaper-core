package io.bugreaper.core.assertions;

import static org.junit.jupiter.api.Assertions.*;

public final class Asserts {

    private Asserts() {
        throw new IllegalStateException("Utility class");
    }


    public static void assertStrings(String expectedString, String actualString) {
        assertAll(() -> assertEquals(expectedString, actualString));
    }

    public static void containsStrings(String expectedSubString, String actualString) {
        assertAll(() -> assertTrue(actualString.contains(expectedSubString)));
    }

    public static void assertBooleans(boolean expected, boolean actual) {
        assertAll(() -> assertEquals(expected, actual));
    }

}

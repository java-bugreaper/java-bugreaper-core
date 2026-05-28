package net.bugreaper.core.generators;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static net.bugreaper.core.generators.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.*;


class DataTests {


    // generateText


    @Test
    void generateText_shouldReturnExactNumberOfWords() {
        int words = 5;

        String text = generateText(words);

        assertNotNull(text);
        assertFalse(text.isBlank());

        String[] split = text.split(" ");

        assertEquals(words, split.length);
    }

    @Test
    void generateText_shouldNotHaveTrailingOrLeadingSpaces() {
        String text = generateText(3);
        System.out.println(generateText(5));
        assertEquals(text.trim(), text);
    }

    @Test
    void generateText_shouldWorkForLargeInput() {
        int words = 10_000;

        String text = generateText(words);

        assertEquals(words, text.split(" ").length);
    }

    @Test
    void testGenerateText() {
        String text = generateText(5);
        assertNotNull(text);
        assertEquals(5, text.split(" ").length);
    }

    // generateWord

    @Test
    void generateWord_shouldReturnWordWithValidLength() {
        String word = generateWord();

        assertNotNull(word);
        assertFalse(word.isBlank());

        // Expected length between 3 and 10
        assertTrue(word.length() >= 3);
        assertTrue(word.length() <= 10);

        // Only lowercase letters
        assertTrue(word.matches("[a-z]+"));
    }

    @Test
    void testGenerateWord() {
        String word = generateWord();
        assertNotNull(word);
        assertTrue(word.length() >= 3 && word.length() <= 10);
        assertTrue(word.matches("[a-z]+"));
    }


    // generateBigInteger

    @Test
    void testGenerateNumberBigInteger() {
        int count = 20;
        BigInteger num = generateBigInteger(count);
        assertEquals(count, num.toString().length());
        assertNotEquals('0', num.toString().charAt(0));
    }

    // generateNumberString

    @Test
    void testGenerateNumberString() {
        int count = 15;
        String str = generateNumberString(count);
        assertEquals(count, str.length());
        assertNotEquals('0', str.charAt(0));
    }

    // generateLong

    @Test
    void testGenerateLong() {
        long val = generateLong(10);
        assertEquals(10, String.valueOf(val).length());
        assertNotEquals('0', String.valueOf(val).charAt(0));
    }

    // generateInt

    @Test
    void testIntFirstDigit() {
        for (int i = 1; i <= 50; i++) {
            int value = generateInt(2);
            String s = String.valueOf(value);
            assertNotEquals('0', s.charAt(0), "First digit should not be 0");
        }
    }

    @Test
    void testIntLengthAndFirstDigit() {
        for (int count = 1; count <= 9; count++) {
            int value = generateInt(count);
            String s = String.valueOf(value);
            assertEquals(count, s.length(), "Length should match count");
            assertNotEquals('0', s.charAt(0), "First digit should not be 0");
        }
    }

    @Test
    void testIntMultipleCalls() {
        for (int i = 0; i < 100; i++) {
            int value = generateInt(5);
            String s = String.valueOf(value);
            assertEquals(5, s.length());
            assertNotEquals('0', s.charAt(0));
        }
    }

    // generateString

    @Test
    void testLength() {
        for (int length = 1; length <= 20; length++) {
            String s = generateString(length);
            assertNotNull(s, "Generated string should not be null");
            assertEquals(length, s.length(), "Generated string length should match count");
        }
    }

    @Test
    void testAlphabeticOnly() {
        for (int i = 0; i < 100; i++) {
            String s = generateString(10);
            assertTrue(s.matches("[a-zA-Z]+"), "String should contain only alphabetic characters");
        }
    }

    //negative

    @Test
    void testInvalidIntCount() {
        assertThrows(IllegalArgumentException.class, () -> generateInt(0));
        assertThrows(IllegalArgumentException.class, () -> generateInt(-2));
        assertThrows(IllegalArgumentException.class, () -> generateInt(10));
    }

    @Test
    void testInvalidLongCount() {
        assertThrows(IllegalArgumentException.class, () -> generateLong(0));
        assertThrows(IllegalArgumentException.class, () -> generateLong(-5));
        assertThrows(IllegalArgumentException.class, () -> generateLong(19));
    }

    @Test
    void testInvalidBigIntCount() {
        assertThrows(IllegalArgumentException.class, () -> generateBigInteger(0));
        assertThrows(IllegalArgumentException.class, () -> generateBigInteger(-1));
    }

    @Test
    void testInvalidStringCount() {
        assertThrows(IllegalArgumentException.class, () -> generateString(0));
        assertThrows(IllegalArgumentException.class, () -> generateString(-2));
    }


    @Test
    void generateText_shouldThrowExceptionForZeroWords() {
        assertThrows(IllegalArgumentException.class, () -> generateText(0));
        assertThrows(IllegalArgumentException.class, () -> generateText(-6));
    }
}

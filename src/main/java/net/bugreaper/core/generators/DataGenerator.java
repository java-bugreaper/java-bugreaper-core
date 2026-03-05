package net.bugreaper.core.generators;

import java.math.BigInteger;
import java.security.SecureRandom;

import java.util.concurrent.ThreadLocalRandom;


@SuppressWarnings("squid:S2245")
public class DataGenerator {

    private DataGenerator() {
        throw new IllegalStateException("Utility class");
    }

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";


    /**
     * Generates a random int with exactly `count` digits.
     * First digit is never 0.
     * Maximum count for long is 9 digits.
     *
     * @param count number of digits (1-9)
     * @return random int
     * @throws IllegalArgumentException if {@code count <= 0 or > 9}
     */
    public static int generateInt(int count) {
        if (count <= 0 || count > 9) {
            throw new IllegalArgumentException("Count must be between 1 and 9 for int type");
        }

        int min = 1;
        for (int i = 1; i < count; i++) {
            min *= 10;
        }

        int max = min * 10 - 1;

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Generates a random long with exactly `count` digits.
     * First digit is never 0.
     * Maximum count for long is 18 digits.
     *
     * @param count number of digits (1-18)
     * @return random long
     * @throws IllegalArgumentException if {@code count <= 0 or > 18}
     */
    public static Long generateLong(int count) {
        if (count <= 0 || count > 18) {
            throw new IllegalArgumentException("Count must be between 1 and 18");
        }

        long min = 1;
        for (int i = 1; i < count; i++) {
            min *= 10;
        }

        long max = min * 10 - 1;

        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    /**
     * Generates a random BigInteger with exactly `count` digits.
     * First digit is never 0.
     *
     * @param count number of digits, must be > 0
     * @return random BigInteger
     * @throws IllegalArgumentException if {@code count <= 0}
     */
    public static BigInteger generateBigInteger(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }

        BigInteger min = BigInteger.TEN.pow(count - 1);

        BigInteger max = BigInteger.TEN.pow(count).subtract(BigInteger.ONE);

        BigInteger range = max.subtract(min).add(BigInteger.ONE);

        BigInteger result;
        do {
            result = new BigInteger(range.bitLength(), RANDOM);
        } while (result.compareTo(range) >= 0);

        return result.add(min);
    }

    /**
     * Generates a random string of the specified length using ThreadLocalRandom.
     * The string contains only letters (a-z, A-Z) with a random mix of upper and lower case.
     *
     * <p>Example:</p>
     * <pre>
     * generateString(5) -> "aZbQf"
     * generateString(10) -> "qWeRtYuIoP"
     * </pre>
     *
     * @param count number of characters to generate; must be positive
     * @return a random string of length {@code count}
     * @throws IllegalArgumentException if {@code count <= 0}
     */
    public static String generateString(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] letters = new char[count];

        for (int i = 0; i < count; i++) {

            char c = (char) ('a' + random.nextInt(26));
            // 50% chance uppercase
            if (random.nextBoolean()) {
                c = Character.toUpperCase(c);
            }
            letters[i] = c;
        }

        return new String(letters);
    }

    /**
     * Generates a random numeric String with exactly `length` digits.
     * First digit is never 0.
     *
     * @param length number of digits, must be > 0
     * @return numeric string
     * @throws IllegalArgumentException if {@code count <= 0}
     */
    public static String generateNumberString(int length) {
        return generateBigInteger(length).toString();
    }

    /**
     * Generates random text with a given number of words.
     * Each word is 3-10 lowercase letters, separated by spaces.
     *
     * @param words number of words to generate, must be > 0
     * @return generated text string
     * @throws IllegalArgumentException if {@code words <= 0}
     */
    public static String generateText(int words) {
        if (words <= 0) {
            throw new IllegalArgumentException("Words count must be positive");
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(generateWord());
        }

        return result.toString();
    }

    /**
     * Generates a single random word (3-10 letters)
     */
    public static String generateWord() {
        int length = RANDOM.nextInt(8) + 3;
        StringBuilder word = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            word.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }

        return word.toString();
    }

}

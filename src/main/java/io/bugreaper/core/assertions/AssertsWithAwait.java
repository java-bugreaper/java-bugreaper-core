package io.bugreaper.core.assertions;

import io.bugreaper.core.exceptions.AssertionWithAwaitFailedError;
import org.awaitility.core.ConditionTimeoutException;

import java.text.MessageFormat;

import static io.bugreaper.core.assertions.Asserts.*;
import static java.time.Duration.ofMillis;
import static org.awaitility.Awaitility.await;

public final class AssertsWithAwait {

    private AssertsWithAwait() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check that expected int EXACTLY like actual with timeout
     * @param expected int
     * @param actual int
     * @param awaitMs await in ms
     * @param messageStart Start for assert message
     */
    public static void assertIntEqualsWithAwait(int expected, int actual, long awaitMs, String messageStart) {
        try {
            await().with()
                    .atMost(ofMillis(awaitMs)).untilAsserted(() ->
                            assertIntEquals(expected, actual));
        }catch (ConditionTimeoutException e){
            throw new AssertionWithAwaitFailedError(
                    MessageFormat.format(
                            "{0} expected to be EXACTLY <{1}> but got <{2}> within {3} milliseconds",
                            messageStart, expected, actual, awaitMs));
        }
    }

    /**
     * Check that expected int GREATER than actual with timeout
     * @param expected int
     * @param actual int
     * @param awaitMs await in ms
     * @param messageStart Start for assert message
     */
    public static void assertGreaterWithAwait(int expected, int actual, long awaitMs, String messageStart) {
        try {
            await().with()
                    .atMost(ofMillis(awaitMs)).untilAsserted(() ->
                            assertGreater(expected, actual));
        }catch (ConditionTimeoutException e){
            throw new AssertionWithAwaitFailedError(
                    MessageFormat.format(
                            "{0} expected to be GREATER than <{1}> but got <{2}> within {3} milliseconds",
                            messageStart, expected, actual, awaitMs));
        }
    }

    /**
     * Check that expected int LESS than actual with timeout
     * @param expected int
     * @param actual int
     * @param awaitMs await in ms
     * @param messageStart Start for assert message
     */
    public static void assertLessWithAwait(int expected, int actual, long awaitMs, String messageStart) {
        try {
            await().with()
                    .atMost(ofMillis(awaitMs)).untilAsserted(() ->
                            assertLess(expected, actual));
        }catch (ConditionTimeoutException e){
            throw new AssertionWithAwaitFailedError(
                    MessageFormat.format(
                            "{0} expected to be LESS than <{1}> but got <{2}> within {3} milliseconds",
                            messageStart, expected, actual, awaitMs));
        }
    }

    /**
     * Check that expected int NOT equals zero with timeout
     * @param actual int
     * @param awaitMs await in ms
     * @param messageStart Start for assert message
     */
    public static void assertNotEmptyWithAwait(int actual, long awaitMs, String messageStart) {

        try {
            await().with()
                    .atMost(ofMillis(awaitMs)).untilAsserted(() ->
                            assertNotEmpty(actual));
        }catch (ConditionTimeoutException e){
            throw new AssertionWithAwaitFailedError(
                    MessageFormat.format(
                            "{0} expected to be NOT <0> but got <{1}> within {2} milliseconds",
                            messageStart, actual, awaitMs));
        }
    }

    /**
     * Check that expected int equals zero with timeout
     * @param actual int
     * @param awaitMs await in ms
     * @param messageStart Start for assert message
     */
    public static void assertEmptyWithAwait(int actual, long awaitMs, String messageStart) {

        try {
            await().with()
                    .atMost(ofMillis(awaitMs)).untilAsserted(() ->
                            assertEmpty(actual));
        }catch (ConditionTimeoutException e){
            throw new AssertionWithAwaitFailedError(
                    MessageFormat.format(
                            "{0} expected to be <0> but got <{1}> within {2} milliseconds",
                            messageStart, actual, awaitMs));
        }
    }

}

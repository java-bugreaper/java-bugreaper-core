package io.bugreaper.core.assertions;

import io.bugreaper.core.exceptions.AssertionWithAwaitFailedError;
import org.awaitility.core.ConditionTimeoutException;

import java.text.MessageFormat;

import static io.bugreaper.core.assertions.Asserts.*;
import static io.bugreaper.core.mappers.StringMappers.formatMilliseconds;
import static java.time.Duration.ofMillis;
import static org.awaitility.Awaitility.await;

public final class AssertsWithAwait {

    private AssertsWithAwait() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check that actual int EXACTLY like expected with timeout
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
                            "{0} expected to be EXACTLY <{1}> but got <{2}> within {3}",
                            messageStart, expected, actual, formatMilliseconds(awaitMs)));
        }
    }

    /**
     * Check that actual int GREATER than expected with timeout
     * @param expected int
     * @param actual int
     * @param awaitMs await in ms
     * @param messageStart Start for assert message
     */
    public static void assertGreaterThanExpectedWithAwait(int expected, int actual, long awaitMs, String messageStart) {
        try {
            await().with()
                    .atMost(ofMillis(awaitMs)).untilAsserted(() ->
                            assertGreaterThanExpected(expected, actual));
        }catch (ConditionTimeoutException e){
            throw new AssertionWithAwaitFailedError(
                    MessageFormat.format(
                            "{0} expected to be GREATER than <{1}> but got <{2}> within {3}",
                            messageStart, expected, actual, formatMilliseconds(awaitMs)));
        }
    }

    /**
     * Check that actual int LESS than expected with timeout
     * @param expected int
     * @param actual int
     * @param awaitMs await in ms
     * @param messageStart Start for assert message
     */
    public static void assertLessThanExpectedWithAwait(int expected, int actual, long awaitMs, String messageStart) {
        try {
            await().with()
                    .atMost(ofMillis(awaitMs)).untilAsserted(() ->
                            assertLessThanExpected(expected, actual));
        }catch (ConditionTimeoutException e){
            throw new AssertionWithAwaitFailedError(
                    MessageFormat.format(
                            "{0} expected to be LESS than <{1}> but got <{2}> within {3}",
                            messageStart, expected, actual, formatMilliseconds(awaitMs)));
        }
    }

    /**
     * Check that actual int NOT equals zero with timeout
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
                            "{0} expected to be NOT <0> but got <{1}> within {2}",
                            messageStart, actual, formatMilliseconds(awaitMs)));
        }
    }

    /**
     * Check that actual int equals zero with timeout
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
                            "{0} expected to be <0> but got <{1}> within {2}",
                            messageStart, actual, formatMilliseconds(awaitMs)));
        }
    }

}

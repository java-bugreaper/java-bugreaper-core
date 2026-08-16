package net.bugreaper.core.utils;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.awaitility.core.ConditionTimeoutException;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static net.bugreaper.core.mappers.StringMappers.formatMilliseconds;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Utility class for creating preconfigured Awaitility ConditionFactory instances
 * with custom polling interval and maximum wait time.
 */
@SuppressWarnings("java:S5960")
public class AwaitUtils {

    private AwaitUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a preconfigured {@link ConditionFactory} with:
     * <ul>
     *     <li>Zero initial delay before the first condition check.</li>
     *     <li>Maximum wait time specified by {@code awaitMs}.</li>
     * </ul>
     *
     * @param atMostMs the maximum time in milliseconds to wait for the condition
     * @return a {@link ConditionFactory} instance configured for fast polling
     */
    public static ConditionFactory awaitCustom(int atMostMs) {
        return Awaitility.await()
                .pollDelay(Duration.ZERO)
                .pollInterval(Duration.ofMillis(100))
                .atMost(Duration.ofMillis(atMostMs));
    }

    /**
     * Returns a preconfigured {@link ConditionFactory} with:
     * <ul>
     *     <li>Zero initial delay before the first condition check.</li>
     *     <li>Custom polling interval specified by {@code pollIntervalMs} between checks.</li>
     *     <li>Maximum wait time specified by {@code atMostMs}.</li>
     * </ul>
     *
     * @param atMostMs       the maximum time in milliseconds to wait for the condition
     * @param pollIntervalMs the polling interval in milliseconds between subsequent checks
     * @return a {@link ConditionFactory} instance configured with the specified timing
     */
    public static ConditionFactory awaitCustom(int atMostMs, int pollIntervalMs) {
        return Awaitility.await()
                .pollDelay(Duration.ZERO)
                .pollInterval(Duration.ofMillis(pollIntervalMs))
                .atMost(Duration.ofMillis(atMostMs));
    }


    /**
     * Waits until the actual value equals the expected value.
     *
     * @param expected      expected value
     * @param actual        supplier that provides the actual value; evaluated repeatedly until the expected value is reached
     * @param awaitMs       maximum time to wait in milliseconds
     * @param awaitInterval interval between value checks in milliseconds
     * @param subject       message fragment describing what is being checked
     * @param container       message fragment describing the container or context of the check
     * @param identifier    identifier, key, pattern, or other value related to the check
     * @param <T>           type of the expected and actual values
     * @throws AssertionError if the expected value is not reached within the specified timeout
     */
    public static <T> void awaitEquals(
            T expected,
            Supplier<T> actual,
            long awaitMs,
            long awaitInterval,
            String subject,
            String container,
            String identifier) {

        AtomicReference<T> lastActual = new AtomicReference<>();

        try {
            Awaitility.await()
                    .pollDelay(Duration.ZERO)
                    .pollInterval(Duration.ofMillis(awaitInterval))
                    .atMost(Duration.ofMillis(awaitMs))
                    .untilAsserted(() -> {
                        T value = actual.get();
                        lastActual.set(value);

                        assertEquals(expected, value);
                    });
        } catch (ConditionTimeoutException e) {
            throw new AssertionError(
                    "Expected EXACTLY <%s> %s in %s '%s', but got <%s> within %s"
                            .formatted(
                                    expected,
                                    subject,
                                    container,
                                    identifier,
                                    lastActual.get(),
                                    formatMilliseconds(awaitMs)
                            )
            );
        }
    }

    /**
     * Waits until the actual numeric value is equal to zero.
     *
     * @param actual        supplier that provides the actual numeric value; invoked repeatedly while waiting
     * @param awaitMs       maximum time to wait in milliseconds
     * @param awaitInterval interval between consecutive value checks in milliseconds
     * @param subject       message fragment describing what is being checked
     * @param container     message fragment providing additional context
     * @param identifier    identifier, key, pattern, or other value related to the check
     * @param <T>           numeric type of the actual value
     * @throws AssertionError if the actual value is not zero within the specified timeout
     */
    public static <T extends Number> void awaitIsEmpty(
            Supplier<T> actual,
            long awaitMs,
            long awaitInterval,
            String subject,
            String container,
            String identifier) {


        AtomicReference<T> lastActual = new AtomicReference<>();

        try {
            Awaitility.await()
                    .pollDelay(Duration.ZERO)
                    .pollInterval(Duration.ofMillis(awaitInterval))
                    .atMost(Duration.ofMillis(awaitMs))
                    .untilAsserted(() -> {
                        T value = actual.get();
                        lastActual.set(value);

                        assertEquals(0, value);
                    });
        } catch (ConditionTimeoutException e) {
            throw new AssertionError(
                    "Expected %s '%s' to be EMPTY, but got <%s> %s within %s"
                            .formatted(
                                    container,
                                    identifier,
                                    lastActual.get(),
                                    subject,
                                    formatMilliseconds(awaitMs)
                            )
            );
        }
    }

    /**
     * Waits until the actual numeric value is greater than zero.
     *
     * @param actual        supplier that provides the actual numeric value; invoked repeatedly while waiting
     * @param awaitMs       maximum time to wait in milliseconds
     * @param awaitInterval interval between consecutive value checks in milliseconds
     * @param subject       message fragment describing what is being checked
     * @param container     message fragment providing additional context
     * @param identifier    identifier, key, pattern, or other value related to the check
     * @param <T>           numeric type of the actual value
     * @throws AssertionError if the actual value is not greater than zero within the specified timeout
     */
    public static <T extends Number> void awaitIsNotEmpty(
            Supplier<T> actual,
            long awaitMs,
            long awaitInterval,
            String subject,
            String container,
            String identifier) {


        try {
            Awaitility.await()
                    .pollDelay(Duration.ZERO)
                    .pollInterval(Duration.ofMillis(awaitInterval))
                    .atMost(Duration.ofMillis(awaitMs))
                    .untilAsserted(() -> assertNotEquals(0, actual.get()));
        } catch (ConditionTimeoutException e) {
            throw new AssertionError(
                    "Expected %s '%s' to be NOT EMPTY, but got no %s within %s"
                            .formatted(
                                    container,
                                    identifier,
                                    subject,
                                    formatMilliseconds(awaitMs)
                            )
            );
        }
    }

    /**
     * Waits until the actual numeric value is greater than the specified minimum value.
     *
     * @param minCount      minimum numeric value that the actual value must exceed
     * @param actual        supplier that provides the actual numeric value; invoked repeatedly while waiting
     * @param awaitMs       maximum time to wait in milliseconds
     * @param awaitInterval interval between consecutive value checks in milliseconds
     * @param subject       message fragment describing what is being checked
     * @param container     message fragment providing additional context
     * @param identifier    identifier, key, pattern, or other value related to the check
     * @param <T>           numeric type of the expected and actual values
     * @throws AssertionError if the actual value is not greater than the minimum value within the specified timeout
     */
    public static <T extends Number> void awaitGraterThan(
            T minCount,
            Supplier<T> actual,
            long awaitMs,
            long awaitInterval,
            String subject,
            String container,
            String identifier) {

        AtomicReference<T> lastActual = new AtomicReference<>();

        try {
            Awaitility.await()
                    .pollDelay(Duration.ZERO)
                    .pollInterval(Duration.ofMillis(awaitInterval))
                    .atMost(Duration.ofMillis(awaitMs))
                    .untilAsserted(() -> {
                        T value = actual.get();
                        lastActual.set(value);

                        assertTrue(value.longValue() > minCount.longValue());
                    });
        } catch (ConditionTimeoutException e) {
            throw new AssertionError(
                    "Expected the number of %s in %s '%s' to be GREATER than <%s>, but got <%s> within %s"
                            .formatted(
                                    subject,
                                    container,
                                    identifier,
                                    minCount,
                                    lastActual.get(),
                                    formatMilliseconds(awaitMs)
                            )
            );
        }
    }

    /**
     * Waits until the actual numeric value is less than the specified maximum value.
     *
     * @param maxCount      maximum numeric value that the actual value must be below
     * @param actual        supplier that provides the actual numeric value; invoked repeatedly while waiting
     * @param awaitMs       maximum time to wait in milliseconds
     * @param awaitInterval interval between consecutive value checks in milliseconds
     * @param subject       message fragment describing what is being checked
     * @param container     message fragment providing additional context
     * @param identifier    identifier, key, pattern, or other value related to the check
     * @param <T>           numeric type of the expected and actual values
     * @throws AssertionError if the actual value is not less than the maximum value within the specified timeout
     */
    public static <T extends Number> void awaitLessThan(
            T maxCount,
            Supplier<T> actual,
            long awaitMs,
            long awaitInterval,
            String subject,
            String container,
            String identifier) {

        AtomicReference<T> lastActual = new AtomicReference<>();

        try {
            Awaitility.await()
                    .pollDelay(Duration.ZERO)
                    .pollInterval(Duration.ofMillis(awaitInterval))
                    .atMost(Duration.ofMillis(awaitMs))
                    .untilAsserted(() -> {
                        T value = actual.get();
                        lastActual.set(value);

                        assertTrue(value.longValue() < maxCount.longValue());
                    });
        } catch (ConditionTimeoutException e) {
            throw new AssertionError(
                    "Expected the number of %s in %s '%s' to be LESS than <%s>, but got <%s> within %s"
                            .formatted(
                                    subject,
                                    container,
                                    identifier,
                                    maxCount,
                                    lastActual.get(),
                                    formatMilliseconds(awaitMs)
                            )
            );
        }
    }

}

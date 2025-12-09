package io.bugreaper.core.utils;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;

import java.time.Duration;

/**
 * Utility class for creating preconfigured Awaitility ConditionFactory instances
 * with custom polling interval and maximum wait time.
 */
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
}

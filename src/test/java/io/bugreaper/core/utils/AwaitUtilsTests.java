package io.bugreaper.core.utils;

import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static io.bugreaper.core.utils.AwaitUtils.awaitCustom;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AwaitUtilsTests {

    @Test
    void utilityClass() throws NoSuchMethodException {
        Constructor<AwaitUtils> constructor = AwaitUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assert (cause instanceof IllegalStateException);
        assert ("Utility class".equals(cause.getMessage()));
    }


    @Test
    void testCustomAtPass() {

        int ar = 10;

        awaitCustom(1000).untilAsserted(() ->
                assertEquals(10, ar));

    }

    @Test
    @SuppressWarnings("squid:S5778")
    void testCustomAtAndPollFailed() {

        int ar = 11;

        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                awaitCustom(500, 100).untilAsserted(() ->
                        assertEquals(10, ar, "Check assert")));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Check assert ==> expected: <10> but was: <11> within 500 milliseconds."));
    }

    @Test
    void testCustomAtParallelPassed() {
        AtomicInteger ar = new AtomicInteger(12);

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> awaitCustom(1000).untilAsserted(() ->
                assertEquals(10, ar.get(), "Check assert")));

        CompletableFuture<Void> future2 =  CompletableFuture.runAsync(() -> ar.set(sleepUpdate()));

        CompletableFuture.allOf(future1, future2).join();

    }

    @SuppressWarnings("squid:S2925")
    private int sleepUpdate() {
        try {
            sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 10;
    }

}

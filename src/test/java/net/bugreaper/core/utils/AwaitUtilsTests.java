package net.bugreaper.core.utils;

import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static net.bugreaper.core.utils.AwaitUtils.*;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("java:S2699")
class AwaitUtilsTests {


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

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> ar.set(sleepUpdate()));

        CompletableFuture.allOf(future1, future2).join();
    }

    @Test
    void awaitEqualsIntPassedTest() {
        AtomicInteger counter = new AtomicInteger();
        AwaitUtils.awaitEquals(3, counter::incrementAndGet, 1000, 100, "messages", "topic", "topic-name");
    }

    @Test
    void awaitIsNotEmptyIntPassedTest() {
        AtomicInteger counter = new AtomicInteger();
        AwaitUtils.awaitIsNotEmpty(counter::incrementAndGet, 100, 100, "messages", "topic", "topic-name");
    }

    @Test
    void awaitGraterThanIntPassedTest() {
        AtomicInteger counter = new AtomicInteger();
        AwaitUtils.awaitGraterThan(2, counter::incrementAndGet, 1000, 100, "messages", "topic", "topic-name");
    }

    @Test
    void awaitLessThanIntPassedTest() {
        AtomicInteger ar = new AtomicInteger(20);

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> awaitCustom(1000).untilAsserted(() ->
                AwaitUtils.awaitLessThan(15, ar::get, 1000, 100, "messages", "topic", "topic-name")));

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> ar.set(sleepUpdate()));

        CompletableFuture.allOf(future1, future2).join();
    }

    @Test
    void awaitEqualsStringPassedTest() {
        long start = System.currentTimeMillis();

        AtomicLong validAfter = new AtomicLong(
                System.currentTimeMillis() + 300
        );

        AwaitUtils.awaitEquals(
                "READY",
                () -> System.currentTimeMillis() >= validAfter.get()
                        ? "READY"
                        : "NOT_READY",
                1000,
                50,
                "status",
                "test",
                "example"
        );

        long elapsed = System.currentTimeMillis() - start;

        assertTrue(elapsed >= 300);
    }


    @Test
    void awaitEqualsIntFailedTest() {

        Throwable exception = assertThrows(AssertionError.class, () ->
                AwaitUtils.awaitEquals(3, this::sleepUpdate, 400, 100, "messages", "topic", "topic-name"));

        assertEquals("Expected EXACTLY <3> messages in topic 'topic-name', but got <10> within 400 milliseconds",
                exception.getMessage());
    }

    @Test
    void awaitIsNotEmptyIntPassedFailed() {


        Throwable exception = assertThrows(AssertionError.class, () ->
                AwaitUtils.awaitIsNotEmpty(this::returnZero, 200, 100, "messages", "topic", "topic-name"));

        assertEquals("Expected topic 'topic-name' to be NOT EMPTY, but got no messages within 200 milliseconds",
                exception.getMessage());
    }


    @Test
    void awaitIsEmptyIntPassedFailed() {
        AtomicInteger ar = new AtomicInteger(10);

        Throwable exception = assertThrows(AssertionError.class, () ->
                AwaitUtils.awaitIsEmpty(ar::get, 200, 100, "messages", "topic", "topic-name"));

        assertEquals("Expected topic 'topic-name' to be EMPTY, but got <10> messages within 200 milliseconds",
                exception.getMessage());
    }

    @Test
    void awaitGraterThanIntFailedTest() {
        AtomicInteger ar = new AtomicInteger(12);

        Throwable exception = assertThrows(AssertionError.class, () ->
                AwaitUtils.awaitGraterThan(13, ar::get, 200, 100, "messages", "topic", "topic-name"));

        assertEquals("Expected the number of messages in topic 'topic-name' to be GREATER than <13>, but got <12> within 200 milliseconds",
                exception.getMessage());
    }

    @Test
    void awaitGraterThanLongFailedTest() {
        AtomicLong ar = new AtomicLong(12);

        Throwable exception = assertThrows(AssertionError.class, () ->
                AwaitUtils.awaitGraterThan(12, ar::get, 200, 100, "messages", "topic", "topic-name"));

        assertEquals("Expected the number of messages in topic 'topic-name' to be GREATER than <12>, but got <12> within 200 milliseconds",
                exception.getMessage());
    }

    @Test
    void awaitLessThanIntFailedTest() {
        AtomicInteger ar = new AtomicInteger(12);

        Throwable exception = assertThrows(AssertionError.class, () ->
                AwaitUtils.awaitLessThan(11, ar::get, 200, 100, "messages", "topic", "topic-name"));

        assertEquals("Expected the number of messages in topic 'topic-name' to be LESS than <11>, but got <12> within 200 milliseconds",
                exception.getMessage());
    }

    @Test
    void awaitLessThanLongFailedTest() {
        AtomicLong ar = new AtomicLong(12);


        Throwable exception = assertThrows(AssertionError.class, () ->
                AwaitUtils.awaitLessThan(12, ar::get, 200, 100, "messages", "topic", "topic-name"));

        assertEquals("Expected the number of messages in topic 'topic-name' to be LESS than <12>, but got <12> within 200 milliseconds",
                exception.getMessage());
    }



    @Test
    void awaitEqualsStringFailedTest() {

        AtomicLong validAfter = new AtomicLong(
                System.currentTimeMillis() + 500
        );
        Throwable exception = assertThrows(AssertionError.class, () ->
                AwaitUtils.awaitEquals(
                        "READY",
                        () -> System.currentTimeMillis() >= validAfter.get()
                                ? "READY"
                                : "NOT_READY",
                        300,
                        100,
                        "status",
                        "test",
                        "example"
                ));

        assertEquals("Expected EXACTLY <READY> status in test 'example', but got <NOT_READY> within 300 milliseconds",
                exception.getMessage());

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

    private int returnZero() {
        return 0;
    }

}

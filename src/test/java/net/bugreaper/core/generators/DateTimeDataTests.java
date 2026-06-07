package net.bugreaper.core.generators;

import net.bugreaper.core.generators.common.DateTimeGeneratorAbstract;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DateTimeDataTests extends DateTimeGeneratorAbstract {

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2025-02-10T14:00:00Z"),
            ZoneId.of( "UTC")
    );

    // date

    @Test
    void testDateToday() {
        assertEquals("2025-02-10", generateDateMethod(null, fixedClock));
    }

    @Test
    void testDatePlus1Year() {
        assertEquals("2026-02-10", generateDateMethod("+1y", fixedClock));
    }

    @Test
    void testDateMinus1YearPlus3Days() {
        assertEquals("2024-02-13", generateDateMethod("-1y +3d", fixedClock));
    }


    // dateTime

    @Test
    void testDateTimeNow() {
        assertEquals("2025-02-10 14:00:00", generateDateTimeMethod("", fixedClock));
    }

    @Test
    void testDateTimeMinus1Year() {
        assertEquals("2024-02-10 14:00:00", generateDateTimeMethod("-1y", fixedClock));
    }

    @Test
    void testDateTimeMinus1YearPlus2Month2Days() {
        assertEquals("2024-04-12 14:00:00", generateDateTimeMethod("-1y+2M 2d", fixedClock));
    }

    @Test
    void testDateTimeMinus1YearWithStaticTime() {
        assertEquals("2024-02-10 14:15:16", generateDateTimeMethod("-1y", "14:15:16", fixedClock));
    }

    @Test
    void testDateTimePlus1YearWithStaticTimeZone() {
        assertEquals("2026-02-10T15:30:00Z", generateDateTimeMethod("+1y", "T15:30:00Z", fixedClock));
    }

}

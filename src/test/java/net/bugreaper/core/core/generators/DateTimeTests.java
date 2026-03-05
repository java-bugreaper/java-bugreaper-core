package net.bugreaper.core.core.generators;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.generators.DateTimeGenerator.generateDate;
import static net.bugreaper.core.generators.DateTimeGenerator.generateDateTime;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertThrows;


class DateTimeTests {

    @Test
    void testDateTimePattern() {

        String ar = generateDateTime("+1y2M3d");

        MatcherAssert.assertThat(
                ar,
                matchesRegex("\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]"));

    }

    @Test
    void testDateTimeStaticPattern() {

        String ar = generateDateTime("+1y2M3d", "20:00:00");

        MatcherAssert.assertThat(
                ar,
                matchesRegex("\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) 20:00:00"));

    }

    @Test
    void testDatePattern() {

        String ar = generateDate("-1y2M3d");

        MatcherAssert.assertThat(
                ar,
                matchesRegex("\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])"));

    }

    // negative

    @Test
    void testDateTimeExceptionWrongKey() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                generateDateTime("+1y1S"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Unsupported time unit: 'S'. Allowed units: [y, M, d, H, m, s]"));

    }

    @Test
    void testDateTimeExceptionTwiceKey() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                generateDateTime("+1y1y"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Time unit 'y' is used more than once"));

    }

    @Test
    void testDateTimeExceptionWrongPattern() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                generateDateTime("+-1y"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Invalid pattern format: '+-1y'"));

    }

    @Test
    void testDateTimeExceptionEmptyTimePattern() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                generateDateTime("+1y", ""));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Time must not be null or blank"));
    }

    @Test
    void testDateTimeExceptionNullTimePattern() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                generateDateTime("+1y", null));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Time must not be null or blank"));
    }

    @Test
    void testDateExceptionWrongKey() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                generateDate("+1y1h"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Unsupported time unit: 'h'. Allowed units: [y, M, d]"));

    }

    @Test
    void testDateExceptionTwiceKey() {


        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                generateDate("+1y2y"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Time unit 'y' is used more than once"));

    }

}

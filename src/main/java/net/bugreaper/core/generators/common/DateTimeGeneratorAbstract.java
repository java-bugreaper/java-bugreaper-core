package net.bugreaper.core.generators.common;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("squid:S5852")
public abstract class DateTimeGeneratorAbstract {

    protected DateTimeGeneratorAbstract() {
    }

    private static final Pattern PATTERN =
            Pattern.compile("([+-]?\\d+)([a-zA-Z])");

    private static final List<Character> ALLOWED_DATE_TIME_UNITS =
            List.of('y', 'M', 'd', 'H', 'm', 's');

    private static final List<Character> ALLOWED_DATE_UNITS =
            List.of('y', 'M', 'd');

    protected static String generateDateTimeMethod(String pattern, Clock clock) {
        LocalDateTime dateTime = LocalDateTime.now(clock);

        if (pattern == null || pattern.isBlank()) {
            return format(dateTime);
        }

        // Ignore separators: colons and all whitespace
        String normalized = pattern.replaceAll("[:\\s]", "");

        Matcher matcher = PATTERN.matcher(normalized);

        Set<Character> usedUnits = new HashSet<>();
        int matchedLength = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);

            check(unit, usedUnits);

            switch (unit) {
                case 'y' -> dateTime = dateTime.plusYears(value);
                case 'M' -> dateTime = dateTime.plusMonths(value);
                case 'd' -> dateTime = dateTime.plusDays(value);
                case 'H' -> dateTime = dateTime.plusHours(value);
                case 'm' -> dateTime = dateTime.plusMinutes(value);
                case 's' -> dateTime = dateTime.plusSeconds(value);
                default -> throw new IllegalArgumentException(
                        "Unsupported time unit: '" + unit +
                                "'. Allowed units: " + ALLOWED_DATE_TIME_UNITS
                );
            }

            matchedLength += matcher.group().length();
        }

        checkPatten(pattern, normalized, matchedLength);

        return format(dateTime);
    }

    protected static String generateDateTimeMethod(String pattern, String time, Clock clock) {

        if (time == null || time.isBlank()) {
            throw new IllegalArgumentException("Time must not be null or blank");
        }

        if (Character.isDigit(time.charAt(0))) {
            return generateDateMethod(pattern, clock) + " " + time;
        } else {
            return generateDateMethod(pattern, clock) + time;
        }
    }


    protected static String generateDateMethod(String pattern, Clock clock) {
        LocalDate date = LocalDate.now(clock);

        if (pattern == null || pattern.isBlank()) {
            return format(date);
        }

        // Ignore separators: colons and all whitespace
        String normalized = pattern.replaceAll("[:\\s]", "");

        Matcher matcher = PATTERN.matcher(normalized);

        Set<Character> usedUnits = new HashSet<>();
        int matchedLength = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);

            check(unit, usedUnits);

            switch (unit) {
                case 'y' -> date = date.plusYears(value);
                case 'M' -> date = date.plusMonths(value);
                case 'd' -> date = date.plusDays(value);
                default -> throw new IllegalArgumentException(
                        "Unsupported time unit: '" + unit +
                                "'. Allowed units: " + ALLOWED_DATE_UNITS
                );
            }

            matchedLength += matcher.group().length();
        }

        if (matchedLength != normalized.length()) {
            throw new IllegalArgumentException(
                    "Invalid pattern format: '" + pattern + "'"
            );
        }

        checkPatten(pattern, normalized, matchedLength);

        return format(date);
    }

    private static void check(char unit, Set<Character> usedUnits) {

        if (!usedUnits.add(unit)) {
            throw new IllegalArgumentException(
                    "Time unit '" + unit + "' is used more than once"
            );
        }
    }

    private static void checkPatten(String pattern, String normalized, int matchedLength) {
        if (matchedLength != normalized.length()) {
            throw new IllegalArgumentException(
                    "Invalid pattern format: '" + pattern + "'"
            );
        }
    }

    private static String format(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
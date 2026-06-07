package net.bugreaper.core.generators;

import net.bugreaper.core.generators.common.DateTimeGeneratorAbstract;

import java.time.Clock;
import java.time.LocalDateTime;

public class DateTimeGenerator extends DateTimeGeneratorAbstract {

    private DateTimeGenerator() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Generates a date string relative to the current date
     * ({@link java.time.LocalDate#now()}) based on a human-readable offset pattern.
     *
     * <p>The pattern consists of numeric values followed by date units.
     * Each unit may optionally have a '+' or '-' sign.
     * Units may appear in any order but can be used only once.</p>
     *
     * <p>Supported units (each allowed once)</p>
     * <ul>
     *   <li>{@code y} - years</li>
     *   <li>{@code M} - months</li>
     *   <li>{@code d} - days</li>
     * </ul>
     *
     * <p>Pattern rules</p>
     * <ul>
     *   <li>Units are case-sensitive</li>
     *   <li>Each unit may be used only once</li>
     *   <li>Supported separators such as spaces are ignored</li>
     *   <li>If {@code pattern} is {@code null} or blank, today's date is returned</li>
     * </ul>
     *
     * <p>Examples</p>
     * <pre>{@code
     * generateDate("-1y");        // today minus 1 year
     * generateDate("+2M10d");     // today plus 2 months and 10 days
     * generateDate("-1y2M4d");    // combined offset
     * generateDate("-1y+2M");    // combined offset
     * }</pre>
     *
     * @param pattern offset pattern relative to the current date;
     *                may be {@code null} or blank
     * @return formatted date string in {@code yyyy-MM-dd} format
     *
     * @throws IllegalArgumentException if:
     * <ul>
     *   <li>the pattern contains unsupported units</li>
     *   <li>a unit is used more than once</li>
     *   <li>the pattern format is invalid</li>
     * </ul>
     */
    public static String generateDate(String pattern) {
        return generateDateMethod(pattern, Clock.systemDefaultZone());
    }

    /**
     * Generates a date-time string relative to the current moment
     * ({@link LocalDateTime#now()}) based on a human-readable offset pattern.
     *
     * <p>The pattern consists of optional numeric values followed by unit
     * identifiers. Each part may have its own sign (+ or -).</p>
     *
     * <p>Supported units</p>
     * <ul>
     *   <li>{@code y} - years</li>
     *   <li>{@code M} - months</li>
     *   <li>{@code d} - days</li>
     *   <li>{@code H} - hours</li>
     *   <li>{@code m} - minutes</li>
     *   <li>{@code s} - seconds</li>
     * </ul>
     *
     * <p>Examples</p>
     * <pre>{@code
     * generateDateTime(""); // now
     * generateDateTime("-1y");
     * generateDateTime("+2M10d");
     * generateDateTime("-1y2M4d5H1m12s");
     * generateDateTime("-5H+10m");
     * generateDateTime("+1y2M3d 10H:11m:10s");
     * }</pre>
     *
     * <p>Separators such as ':' are ignored. Missing units are treated as zero.</p>
     *
     * @param pattern offset pattern relative to now; may be {@code null} or blank
     * @return formatted date-time string in {@code yyyy-MM-dd HH:mm:ss} format
     * @throws IllegalArgumentException if the pattern is invalid, contains
     *                                  unsupported units, or reuses a unit
     */
    public static String generateDateTime(String pattern) {
        return generateDateTimeMethod(pattern, Clock.systemDefaultZone());
    }

    /**
     * Generates a date-time string by combining a generated date (based on the given pattern)
     * with a provided time suffix.
     *
     * <p>The date part is produced by {@link #generateDate(String)} using the specified
     * offset pattern. The {@code time} parameter is then appended to the generated date.</p>
     *
     * <p>Formatting rules</p>
     * <ul>
     *   <li>If {@code time} starts with a digit, a space is inserted between date and time.</li>
     *   <li>If {@code time} does not start with a digit (e.g. starts with 'T'),
     *       it is appended directly without a space.</li>
     * </ul>
     *
     * <p>Examples</p>
     * <pre>{@code
     * generateDateTimeMethod("-1y", "14:00:00");
     * // Result: 2024-02-10 14:00:00
     *
     * generateDateTimeMethod("+1M", "T10:30:00-05:00");
     * // Result: 2025-03-10T10:30:00-05:00
     * }</pre>
     *
     * @param pattern date offset pattern passed to {@link #generateDate(String)};
     *                may be {@code null} or blank
     * @param time    time portion or suffix to append to the generated date;
     *                must not be {@code null} or blank
     *
     * @return combined date-time string
     *
     * @throws IllegalArgumentException if {@code time} is {@code null} or blank
     */
    public static String generateDateTime(String pattern, String time) {
        return generateDateTimeMethod(pattern, time, Clock.systemDefaultZone());
    }

}
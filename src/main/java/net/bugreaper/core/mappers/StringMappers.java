package net.bugreaper.core.mappers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class StringMappers {

    private StringMappers() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * method for merge JSON templates with Map
     * @param source String with source
     * @param params Map with params
     * @return String (mapped)
     * <p>example: stringMapper(
     * <p>"""
     * <p>{
     * <p>"name": "${name}",
     * <p>"age": ${age},
     * <p>"name2": "${name} again"
     * <p>}""",
     * <p>Map.of(
     * <p>"name", "Alex",
     * <p>"age", 42
     * <p>));
     */
    public static String stringMapper(String source, Map<String, Object> params) {
        String test = StringSubstitutor.replace(source, params, "${", "}");

        if (test.contains("${")) {
            throw new IllegalArgumentException("no data for variable: " + StringUtils.substringBetween(test, "${", "}"));
        }

        return test;
    }

    /**
     * method for merge JSON templates with Map
     * <p> Need for cases where source can contain standard prefix ${
     *
     * @param source String with source
     * @param params Map with params
     * @return String (mapped)
     */
    public static String stringMapperV2(String source, Map<String, Object> params) {
        return StringSubstitutor.replace(source, params, "$${", "}");
    }

    /**
     * method for beautify list to string(for attach)
     * <p>example: list: ["one", "{"id": 2}", "three"]
     * <p>will be
     * <p>[
     * <br>one
     *
     * <p>-----------
     *
     * <p>{"id": 2}
     *
     * <p>-----------
     *
     * <p>three
     * <br>]
     */
    public static String listToString(List<String> list) {

        if (list == null){
            return "";
        }

        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("\n\n-----------\n\n", "[\n", "\n]"));

    }

    /**
     * Converts a duration in milliseconds into a human-readable string.
     * <p>
     * The result is formatted as a combination of seconds and remaining milliseconds.
     * Only non-zero units are included in the output, except when the value is zero
     * (returns "0 milliseconds").
     * </p>
     *
     * <p><b>Rules:</b></p>
     * <ul>
     *   <li>1 second = 1000 milliseconds</li>
     *   <li>Includes remaining milliseconds after full seconds</li>
     *   <li>Uses singular/plural forms correctly ("1 second", "2 seconds")</li>
     *   <li>Omits zero-value units except when total is zero</li>
     * </ul>
     *
     * <p><b>Examples:</b></p>
     * <pre>
     * formatMilliseconds(2000) -> "2 seconds"
     * formatMilliseconds(3500) -> "3 seconds 500 milliseconds"
     * formatMilliseconds(999)  -> "999 milliseconds"
     * formatMilliseconds(0)    -> "0 milliseconds"
     * </pre>
     *
     * @param milliseconds the duration in milliseconds; must be >= 0
     * @return a human-readable string representation of the duration
     */
    public static String formatMilliseconds(long milliseconds) {
        long seconds = milliseconds / 1000;
        long remainingMs = milliseconds % 1000;

        StringBuilder result = new StringBuilder();

        if (seconds > 0) {
            result.append(seconds).append(" second");
            if (seconds > 1) result.append("s");
        }

        if (remainingMs > 0) {
            if (!result.isEmpty()) result.append(" ");
            result.append(remainingMs).append(" millisecond");
            if (remainingMs > 1) result.append("s");
        }

        if (result.isEmpty()) {
            result.append("0 milliseconds");
        }

        return result.toString();
    }

    /**
     * Converts a byte count into a human-readable string using 1024-based units.
     * <p>
     * The result is formatted as a combination of gigabytes (Gb), megabytes (Mb),
     * kilobytes (Kb), and remaining bytes. Only non-zero units are included in
     * the output, except when the value is zero (returns "0 bytes").
     * </p>
     *
     * <p><b>Rules:</b></p>
     * <ul>
     *   <li>Uses binary units (1 Kb = 1024 bytes, 1 Mb = 1024 Kb, 1 Gb = 1024 Mb)</li>
     *   <li>Does not convert beyond gigabytes (values larger than 1 Gb remain in Gb)</li>
     *   <li>Includes remaining bytes as "byte" or "bytes"</li>
     *   <li>Omits zero-value units except when total is zero</li>
     * </ul>
     *
     * <p><b>Examples:</b></p>
     * <pre>
     * formatBytes(1024)      -> "1Kb"
     * formatBytes(1049601)   -> "1Mb 1Kb 1 byte"
     * formatBytes(1049602)   -> "1Mb 1Kb 2 bytes"
     * formatBytes(0)         -> "0 bytes"
     * </pre>
     *
     * @param bytes the number of bytes to format; must be >= 0
     * @return a human-readable string representation of the byte size
     */
    public static String formatBytes(long bytes) {
        long gb = bytes / (1024L * 1024 * 1024);
        bytes %= (1024L * 1024 * 1024);

        long mb = bytes / (1024L * 1024);
        bytes %= (1024L * 1024);

        long kb = bytes / 1024;
        bytes %= 1024;

        long b = bytes;

        StringBuilder result = new StringBuilder();

        if (gb > 0) {
            result.append(gb).append("Gb");
        }
        if (mb > 0) {
            if (!result.isEmpty()) result.append(" ");
            result.append(mb).append("Mb");
        }
        if (kb > 0) {
            if (!result.isEmpty()) result.append(" ");
            result.append(kb).append("Kb");
        }
        if (b > 0 || result.isEmpty()) {
            if (!result.isEmpty()) result.append(" ");
            result.append(b).append(b == 1 ? " byte" : " bytes");
        }

        return result.toString();
    }

}

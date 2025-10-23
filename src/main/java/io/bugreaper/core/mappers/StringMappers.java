package io.bugreaper.core.mappers;

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


}

package net.bugreaper.core.assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bugreaper.core.exceptions.JsonAssertExtendedException;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

public abstract class JsonAssertsAbstract {

    private static final String EXPECTED_ARRAY = ": expected array but was ";
    private static final String BUT_WAS = "] but was [";

    protected static void validateJsonInternal(String jsonData, ObjectMapper mapper, boolean allowTrailingComma) {
        if (jsonData == null || jsonData.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON string is null or empty");
        }

        try {
            JsonNode node = mapper.readTree(jsonData);
            if (!node.isObject() && !node.isArray()) {
                throw new IllegalArgumentException("JSON must be an object or array at the root");
            }
        } catch (JsonProcessingException e) {
            String mode = allowTrailingComma ? "lenient" : "strict";
            throw new IllegalArgumentException(
                    "Invalid " + mode + " JSON/JSONArray: " + e.getOriginalMessage(), e);
        }
    }

    // JSON Subset

    protected static JsonNode getJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unparsable JSON string: " + json, e);
        }
    }

    protected static void compareJson(JsonNode expected, JsonNode actual, String path, List<String> errors) {

        if (expected.isObject()) {
            compareObject(expected, actual, path, errors);
            return;
        }

        if (expected.isArray()) {
            compareArray(expected, actual, path, errors);
            return;
        }

        compareValue(expected, actual, path, errors);
    }

    private static void compareObject(JsonNode expected, JsonNode actual, String path, List<String> errors) {
        expected.fieldNames().forEachRemaining(field -> {
            JsonNode expectedChild = expected.get(field);
            JsonNode actualChild = actual.get(field);

            if (actualChild == null) {
                errors.add(path + field + ": missing field");
            } else {
                compareJson(expectedChild, actualChild, path + field + ".", errors);
            }
        });
    }

    private static void compareArray(JsonNode expected, JsonNode actual, String path, List<String> errors) {
        if (!actual.isArray()) {
            errors.add(path + EXPECTED_ARRAY + actual.getNodeType());
            return;
        }

        for (JsonNode expectedItem : expected) {
            if (!contains(expectedItem, actual)) {
                errors.add(path + "[]: missing element " + expectedItem);
            }
        }
    }

    private static void compareValue(JsonNode expected, JsonNode actual, String path, List<String> errors) {
        if (!expected.equals(actual)) {
            errors.add(path + ": expected <" + expected + "> but was <" + actual + ">");
        }
    }

    private static boolean contains(JsonNode expectedItem, JsonNode actualArray) {
        for (JsonNode actualItem : actualArray) {
            if (matches(expectedItem, actualItem)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matches(JsonNode expected, JsonNode actual) {
        List<String> tmp = new ArrayList<>();
        compareJson(expected, actual, "", tmp);
        return tmp.isEmpty();
    }

    protected static String formatErrors(List<String> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append("JSON subset assertion failed:\n");

        for (String err : errors) {
            sb.append(" - ").append(err).append("\n");
        }

        return sb.toString();
    }

    // EXTENDED  compare


    // COMPARE


    protected static void extendedJsonCompare(String path,
                                              JsonNode expected,
                                              JsonNode actual,
                                              List<String> errors) {

        if (isExpectedNull(path, expected, actual, errors)) {
            return;
        }

        if (expected.isObject()) {
            compareObject(path, expected, actual, errors);
            return;
        }

        if (expected.isArray()) {
            compareArray(path, expected, actual, errors);
            return;
        }

        comparePrimitive(path, expected, actual, errors);
    }


    // OBJECT


    private static void compareObject(String path,
                                      JsonNode expected,
                                      JsonNode actual,
                                      List<String> errors) {

        if (actual == null || !actual.isObject()) {
            errors.add("• " + path + ": expected object but was " + type(actual));
            return;
        }

        for (Map.Entry<String, JsonNode> field : expected.properties()) {

            handleField(path, actual, field, errors);
        }
    }

    private static void handleField(String path,
                                    JsonNode actual,
                                    Map.Entry<String, JsonNode> entry,
                                    List<String> errors) {

        ParsedKey parsed = parseKey(entry.getKey());

        String fieldName = parsed.field;
        String operator = parsed.operator;

        String currentPath =
                path.isEmpty()
                        ? fieldName
                        : path + "." + fieldName;

        JsonNode expectedValue = entry.getValue();
        JsonNode actualValue = actual.get(fieldName);

        if (handleArrayOperator(
                currentPath,
                operator,
                expectedValue,
                actualValue,
                errors
        )) {
            return;
        }

        if (handleExistsOperator(
                currentPath,
                fieldName,
                operator,
                expectedValue,
                actual,
                errors
        )) {
            return;
        }

        if (actualValue == null) {
            errors.add("• " + currentPath + ": field missing");
            return;
        }

        compareValue(
                currentPath,
                operator,
                expectedValue,
                actualValue,
                errors
        );
    }


    // ARRAY


    private static void compareArray(String path,
                                     JsonNode expected,
                                     JsonNode actual,
                                     List<String> errors) {

        if (actual == null || !actual.isArray()) {
            errors.add("• " + path + EXPECTED_ARRAY + type(actual));
            return;
        }

        for (JsonNode expectedElement : expected) {

            if (!containsMatchingElement(path, expectedElement, actual)) {

                errors.add(
                        "• " + path +
                                ": expected element not found -> " +
                                expectedElement
                );
            }
        }
    }

    private static boolean containsMatchingElement(String path,
                                                   JsonNode expectedElement,
                                                   JsonNode actualArray) {

        for (JsonNode actualElement : actualArray) {

            List<String> temp = new ArrayList<>();

            extendedJsonCompare(path, expectedElement, actualElement, temp);

            if (temp.isEmpty()) {
                return true;
            }
        }

        return false;
    }


    // PRIMITIVE


    private static void comparePrimitive(String path,
                                         JsonNode expected,
                                         JsonNode actual,
                                         List<String> errors) {

        if (!primitiveEquals(expected, actual)) {

            errors.add(
                    "• " + path +
                            ": expected [" + value(expected) +
                            BUT_WAS + value(actual) + "]"
            );
        }
    }


    // VALUE COMPARE


    private static void compareValue(String path,
                                     String operator,
                                     JsonNode expected,
                                     JsonNode actual,
                                     List<String> errors) {

        if (operator == null) {
            extendedJsonCompare(path, expected, actual, errors);
            return;
        }

        switch (operator) {

            case "=" -> compareEquals(path, expected, actual, errors);

            case "!=" -> compareNotEquals(path, expected, actual, errors);

            case ">", "<", ">=", "<=" ->
                    compareNumbers(path, operator, expected, actual, errors);

            case "in" ->
                    compareIn(path, expected, actual, errors);

            case "regex" ->
                    compareRegex(path, expected, actual, errors);

            case "like" ->
                    compareLike(path, expected, actual, errors);

            default ->
                    throw new JsonAssertExtendedException(path + ": unsupported operator [" + operator + "]");
        }
    }

    private static void compareEquals(String path,
                                      JsonNode expected,
                                      JsonNode actual,
                                      List<String> errors) {

        if (!equals(expected, actual)) {
            errors.add(diff(path, "=", expected, actual));
        }
    }

    private static void compareNotEquals(String path,
                                         JsonNode expected,
                                         JsonNode actual,
                                         List<String> errors) {

        if (equals(expected, actual)) {
            errors.add(diff(path, "!=", expected, actual));
        }
    }


    // ARRAY OPERATORS


    private static boolean handleArrayOperator(String path,
                                               String operator,
                                               JsonNode expected,
                                               JsonNode actual,
                                               List<String> errors) {

        if (operator == null || !isArrayOperator(operator)) {
            return false;
        }

        compareArrayOperator(path, operator, expected, actual, errors);

        return true;
    }

    private static void compareArrayOperator(String path,
                                             String operator,
                                             JsonNode expected,
                                             JsonNode actual,
                                             List<String> errors) {

        if (!validateArrayOperatorTarget(path, actual, errors)) {
            return;
        }

        switch (operator) {

            case "size" ->
                    compareArraySize(path, expected, actual, errors);

            case "distinct" ->
                    compareArrayDistinct(path, expected, actual, errors);

            default ->

                throw new JsonAssertExtendedException(path + ": unsupported array operator [" + operator + "]");
        }
    }

    private static boolean validateArrayOperatorTarget(String path,
                                                       JsonNode actual,
                                                       List<String> errors) {

        if (actual != null && actual.isArray()) {
            return true;
        }

        errors.add("• " + path + EXPECTED_ARRAY + type(actual));

        return false;
    }

    private static void compareArraySize(String path,
                                         JsonNode expected,
                                         JsonNode actual,
                                         List<String> errors) {

        if (!expected.isNumber()) {
            errors.add("• " + path + ": size operator requires number");
            return;
        }

        int expectedSize = expected.asInt();
        int actualSize = actual.size();

        if (expectedSize != actualSize) {

            errors.add(
                    "• " + path +
                            ": array size mismatch. expected " +
                            expectedSize +
                            " but was " +
                            actualSize
            );
        }
    }

    private static void compareArrayDistinct(String path,
                                             JsonNode expected,
                                             JsonNode actual,
                                             List<String> errors) {

        if (!expected.isBoolean()) {

            throw new JsonAssertExtendedException(path + ": distinct operator requires boolean");
        }

        boolean expectedDistinct = expected.asBoolean();
        boolean actualDistinct = isDistinct(actual);

        if (expectedDistinct != actualDistinct) {

            errors.add(
                    "• " + path +
                            ": expected distinct elements [" +
                            expectedDistinct +
                            BUT_WAS +
                            actualDistinct +
                            "]"
            );
        }
    }

    private static boolean isDistinct(JsonNode actual) {

        Set<String> unique = new HashSet<>();

        for (JsonNode node : actual) {

            if (!unique.add(node.toString())) {
                return false;
            }
        }

        return true;
    }

    private static boolean isArrayOperator(String op) {
        return "size".equals(op)
                || "distinct".equals(op);
    }


    // EXISTS


    private static boolean handleExistsOperator(String path,
                                                String fieldName,
                                                String operator,
                                                JsonNode expected,
                                                JsonNode actualObject,
                                                List<String> errors) {

        if (!"exists".equals(operator)) {
            return false;
        }

        boolean shouldExist = expected.asBoolean();
        boolean exists = actualObject.has(fieldName);

        if (shouldExist && !exists) {
            errors.add("• " + path + ": field missing");
        }

        if (!shouldExist && exists) {
            errors.add("• " + path + ": unexpected field exists");
        }

        return true;
    }


    // OPERATORS


    private static void compareNumbers(String path,
                                       String operator,
                                       JsonNode expected,
                                       JsonNode actual,
                                       List<String> errors) {

        try {

            BigDecimal exp = new BigDecimal(expected.asText());
            BigDecimal act = new BigDecimal(actual.asText());

            boolean ok = switch (operator) {
                case ">" -> act.compareTo(exp) > 0;
                case "<" -> act.compareTo(exp) < 0;
                case ">=" -> act.compareTo(exp) >= 0;
                case "<=" -> act.compareTo(exp) <= 0;
                default -> false;
            };

            if (!ok) {
                errors.add(diff(path, operator, expected, actual));
            }

        } catch (Exception e) {
            throw new JsonAssertExtendedException(path + ": numeric compare failed");
        }
    }

    private static void compareIn(String path,
                                  JsonNode expected,
                                  JsonNode actual,
                                  List<String> errors) {

        if (!expected.isArray()) {
            throw new JsonAssertExtendedException(path + ": 'in' requires array");
        }

        for (JsonNode node : expected) {

            if (primitiveEquals(node, actual)) {
                return;
            }
        }

        errors.add(
                "• " + path +
                        ": expected in " +
                        expected +
                        " but was [" +
                        value(actual) + "]"
        );
    }

    private static void compareRegex(String path,
                                     JsonNode expected,
                                     JsonNode actual,
                                     List<String> errors) {

        String regex = expected.asText();

        if (!Pattern.compile(regex)
                .matcher(actual.asText())
                .find()) {

            errors.add(
                    "• " + path +
                            ": expected regex [" +
                            regex +
                            BUT_WAS +
                            value(actual) + "]"
            );
        }
    }

    private static void compareLike(String path,
                                    JsonNode expected,
                                    JsonNode actual,
                                    List<String> errors) {

        String pattern = expected.asText();

        String contains = pattern.replace("%", "");

        if (!actual.asText().contains(contains)) {

            errors.add(
                    "• " + path +
                            ": expected like [" +
                            pattern +
                            BUT_WAS +
                            value(actual) + "]"
            );
        }
    }


    // UTILS


    private static boolean isExpectedNull(String path,
                                          JsonNode expected,
                                          JsonNode actual,
                                          List<String> errors) {

        if (expected != null && !expected.isNull()) {
            return false;
        }

        if (actual != null && !actual.isNull()) {

            errors.add(
                    "• " + path +
                            ": expected null but was " +
                            value(actual)
            );
        }

        return true;
    }

    private static boolean primitiveEquals(JsonNode a, JsonNode b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.isNumber() && b.isNumber()) {

            return new BigDecimal(a.asText())
                    .compareTo(new BigDecimal(b.asText())) == 0;
        }

        return Objects.equals(a.asText(), b.asText());
    }

    private static String diff(String path,
                               String op,
                               JsonNode expected,
                               JsonNode actual) {

        return "• " + path +
                ": expected " +
                op +
                "[" + value(expected) +
                BUT_WAS +
                value(actual) + "]";
    }

    private static String value(JsonNode node) {
        return node == null || node.isNull()
                ? "null"
                : node.asText();
    }

    private static String type(JsonNode node) {
        return node == null
                ? "null"
                : node.getNodeType().name();
    }

    private static ParsedKey parseKey(String key) {

        if (!key.contains(":")) {
            return new ParsedKey(key, null);
        }

        String[] split = key.split(":", 2);

        return new ParsedKey(split[0], split[1]);
    }

    private static boolean equals(JsonNode a, JsonNode b) {
        return Objects.equals(a.asText(), b.asText());
    }
    
    // HELPER

    private record ParsedKey(String field, String operator) {}
}
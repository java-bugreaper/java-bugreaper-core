package net.bugreaper.core.assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class JsonAsserts {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonAsserts.class);

    private JsonAsserts() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper strictMapper = new ObjectMapper();

    // Lenient mapper allows trailing commas
    private static final ObjectMapper lenientMapper = new ObjectMapper()
            .configure(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true);

    /**
     * Assertion without strict array ordering
     * <p> extensible fields and elements in array will be skipped
     *
     * @param expectedPart expected part of JSON with arrays
     * @param actualJson actual JSON
     */
    public static void containsJsonSubset(String expectedPart, String actualJson) {
        JsonNode expected = getJson(expectedPart);
        JsonNode actual = getJson(actualJson);

        List<String> errors = new ArrayList<>();

        compareJson(expected, actual, "", errors);

        if (!errors.isEmpty()) {
            fail(formatErrors(errors));
        }
    }

    /**
     * Assertion without strict array ordering
     * <p> extensible fields will be skipped. But extensible elements in array cause AssertionError
     *
     * @param expectedPart expected part of JSON with arrays (can be not ordered but must have same count of elements)
     * @param actualJson actual JSON
     */
    public static void containsJson(String expectedPart, String actualJson) {
        assertJsonMethod(expectedPart, actualJson, JSONCompareMode.LENIENT);
    }

    /**
     * Assertion with strict array ordering
     * <p> extensible fields will be skipped
     *
     * @param expectedPart expected part of JSON with strict ordered arrays
     * @param actualJson actual JSON
     */
    public static void containsStrictOrderJson(String expectedPart, String actualJson) {
        assertJsonMethod(expectedPart, actualJson, JSONCompareMode.STRICT_ORDER);
    }

    /**
     * Assertion with strict array ordering
     * <p> extensible fields not expected
     *
     * @param expectedAct expected full JSON with strict ordered arrays
     * @param actualJson actual JSON
     */
    public static void assertJson(String expectedAct, String actualJson) {
        assertJsonMethod(expectedAct, actualJson, JSONCompareMode.STRICT);
    }

    /**
     * Assertion without strict array ordering
     * <p> extensible fields not expected
     *
     * @param expectedAct expected full JSON with arrays (can be not ordered)
     * @param actualJson actual JSON
     */
    public static void assertNoStrictOrderJson(String expectedAct, String actualJson) {
        assertJsonMethod(expectedAct, actualJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    public static void assertJsonNotContains(String unexpectedPart, String actualJson) {
        try {
            assertJsonNotMethod(unexpectedPart, actualJson, false);
        }catch (AssertionError e) {
            fail("Actual Json contains unexpected Json part:\n" + unexpectedPart);
        }
    }


    public static void assertJsonMethod(String expectedAct, String actualJson, JSONCompareMode compareMode) {
        try {
            JSONAssert.assertEquals(expectedAct, actualJson, compareMode);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    //if no assert error will be null
    public static String validationJsonSchemaMethod(String expectedSchema, String actualJson, JsonSchemaFactory factory, boolean returnReport) {

        JsonSchema jsonSchema = factory.getSchema(expectedSchema);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;

        try {
            jsonNode = objectMapper.readTree(actualJson);
        } catch (JsonProcessingException e) {
            String notJsonMessage = "Schema assert failed (Actual body not JSON)";
            if(returnReport){
                return notJsonMessage;
            }
            throw new AssertionError(notJsonMessage);
        }

        Set<ValidationMessage> errors =  jsonSchema.validate(jsonNode);

        if(errors.isEmpty()){
            LOGGER.debug("Schema passed");
            return null;
        }else{
            StringBuilder message = new StringBuilder();

            for (ValidationMessage oneAssert : errors) {
                message.append(oneAssert).append("\n");
            }

            if(returnReport){
                return message.toString();
            }
            throw new AssertionError(message);

        }
    }

    private static void assertJsonNotMethod(String unexpectedPart, String actualJson, Boolean strict) {
        try {
            JSONAssert.assertNotEquals(unexpectedPart, actualJson, strict);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Check is String Json/JsonArray type STRICT (even extra coma throw exception!)
     * @param jsonData String with data
     * @exception IllegalArgumentException if not Json/JsonArray type
     */
    public static void assertValidJson(String jsonData) {
        validateJsonInternal(jsonData, strictMapper, false);
    }

    /**
     * Check is String Json/JsonArray type (extra coma throw passed)
     * @param jsonData String with data
     * @exception IllegalArgumentException if not Json/JsonArray type
     */
    public static void assertLenientValidJson(String jsonData) {
        validateJsonInternal(jsonData, lenientMapper, true);
    }


    private static void validateJsonInternal(String jsonData, ObjectMapper mapper, boolean allowTrailingComma) {
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

    private static JsonNode getJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unparsable JSON string: " + json, e);
        }
    }

    private static void compareJson(JsonNode expected, JsonNode actual, String path, List<String> errors) {

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
            errors.add(path + ": expected array but was " + actual.getNodeType());
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

    private static String formatErrors(List<String> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append("JSON subset assertion failed:\n");

        for (String err : errors) {
            sb.append(" - ").append(err).append("\n");
        }

        return sb.toString();
    }


}

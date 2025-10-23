package io.bugreaper.core.assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;


public class JsonAsserts {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonAsserts.class);

    private JsonAsserts() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Assertion without strict array ordering
     * <p> extensible fields will be skipped
     *
     * @param expectedPart expected part of JSON with arrays (can be not ordered)
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
            LOGGER.warn("=====");
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
     * Check is String Json/JsonArray type
     * @param jsonData String with data
     * @exception IllegalArgumentException if not Json/JsonArray type
     */
    public static void checkJson(String jsonData) {
        try {
            new JSONObject(jsonData);
        } catch (JSONException | NullPointerException e) {
            try {
                new JSONArray(jsonData);
            } catch (JSONException | NullPointerException ex) {
                throw new IllegalArgumentException(String.format("Wrong JSON/JSONArray format:%n%s", jsonData), ex);
            }
        }
    }

}

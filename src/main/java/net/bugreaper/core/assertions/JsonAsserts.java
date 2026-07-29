package net.bugreaper.core.assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import net.bugreaper.core.exceptions.JsonAssertExtendedException;
import net.bugreaper.core.exceptions.JsonMappersException;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class JsonAsserts extends JsonAssertsAbstract{

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
            throw new AssertionError(formatErrors(errors));
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
     * <p> extensible fields and elements in arrays not expected
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

    /**
     * Asserts that actual JSON does not contains unexpected JSON part
     *
     * @param unexpectedPart unexpected part of JSON
     * @param actualJson actual JSON
     */
    public static void assertJsonNotContains(String unexpectedPart, String actualJson) {
        try {
            assertJsonNotMethod(unexpectedPart, actualJson, false);
        }catch (AssertionError e) {
            throw new AssertionError("Actual JSON contains unexpected JSON part:\n" + unexpectedPart);
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

    //used in Rest
    public static void assertJsonMethod(String expectedAct, String actualJson, JSONCompareMode compareMode) {
        try {
            JSONAssert.assertEquals(expectedAct, actualJson, compareMode);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void assertJsonNotMethod(String unexpectedPart, String actualJson, Boolean strict) {
        try {
            JSONAssert.assertNotEquals(unexpectedPart, actualJson, strict);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }


    // Extended


    /**
     * Asserts that the {@code actualJson} matches the {@code expectedJson}
     * using a flexible, non-strict comparison.
     *
     * <p><b>Main features:</b></p>
     * <ul>
     *   <li>Non-strict object comparison:
     *     extra fields in the actual JSON are ignored.</li>
     *
     *   <li>Recursive comparison of nested objects and arrays.</li>
     *
     *   <li>Default array comparison:
     *     array order and size are ignored, and only checks that all expected
     *     elements are contained in the actual array.</li>
     *
     *   <li>Supports comparison operators in expected JSON field names using
     *     the syntax {@code field:operator}.</li>
     *
     *   <li>Supports multiple checks for one field</li>
     *
     *   <li>Produces detailed and human-readable diff messages for all mismatches.</li>
     * </ul>
     *
     * <p><b>Supported operators:</b></p>
     * <table border="1">
     *   <caption>Supported comparison operators</caption>
     *   <tr>
     *     <th>Operator</th>
     *     <th>Description</th>
     *     <th>Example</th>
     *   </tr>
     *   <tr>
     *     <td>{@code =}</td>
     *     <td>Equals comparison</td>
     *     <td>{@code "age:=": 10}</td>
     *   </tr>
     *   <tr>
     *     <td>{@code !=}</td>
     *     <td>Not equals comparison</td>
     *     <td>{@code "status:!=": "ERROR"}</td>
     *   </tr>
     *   <tr>
     *     <td>{@code >, <, >=, <=}</td>
     *     <td>Numeric comparison</td>
     *     <td>{@code "age:>=": 18}</td>
     *   </tr>
     *   <tr>
     *     <td>{@code in}</td>
     *     <td>Actual value must exist in expected array</td>
     *     <td>{@code "type:in": ["A", "B"]}</td>
     *   </tr>
     *   <tr>
     *     <td>{@code regex}</td>
     *     <td>Regular expression match</td>
     *     <td>{@code "email:regex": ".*@gmail.com"}</td>
     *   </tr>
     *   <tr>
     *     <td>{@code like}</td>
     *     <td>Substring match using SQL-like '%' wildcards</td>
     *     <td>{@code "name:like": "lex"}</td>
     *   </tr>
     *   <tr>
     *     <td>{@code exists}</td>
     *     <td>Checks field existence or absence</td>
     *     <td>{@code "name:exists": true}</td>
     *   </tr>
     *   <tr>
     *     <td>{@code size}</td>
     *     <td>Checks exact array size</td>
     *     <td>{@code "users:size": 3}</td>
     *   </tr>
     *   <tr>
     *     <td>{@code distinct}</td>
     *     <td>Checks whether array contains unique elements</td>
     *     <td>{@code "users:distinct": true}</td>
     *   </tr>
     * </table>
     *
     * <p><b>Example:</b></p>
     *
     * <pre>{@code
     * assertJsonsExtended("""
     * {
     *   "user": {
     *     "name:like": "%lex%",
     *     "age:>=": 18,
     *     "roles:size": 2,
     *     "tags:distinct": true
     *   }
     * }
     * """, actualJson);
     * }</pre>
     *
     * <p><b>Example diff messages:</b></p>
     * <ul>
     *   <li>{@code • user.name: expected [Alex2] but was [Alex]}</li>
     *   <li>{@code • user.age: expected >=[18] but was [15]}</li>
     *   <li>{@code • user.email: field missing}</li>
     *   <li>{@code • users: array size mismatch. expected 3 but was 2}</li>
     * </ul>
     *
     * @param expectedJson JSON string containing the expected structure
     *                     and optional operators in field names
     * @param actualJson JSON string to validate against the expected JSON
     *
     * @throws AssertionError if JSON comparison fails
     *                        (contains detailed diff information)
     * @throws JsonMappersException if input JSON cannot be parsed
     */
    public static void assertJsonsExtended(String expectedJson, String actualJson) {
        try {

            JsonNode expected = strictMapper.readTree(expectedJson);
            JsonNode actual = strictMapper.readTree(actualJson);

            List<String> errors = new ArrayList<>();

            extendedJsonCompare("", expected, actual, errors);

            if (!errors.isEmpty()) {
                throw new AssertionError(
                        "JSON comparison failed:\n" +
                                String.join("\n", errors)
                );
            }

        } catch (AssertionError e) {
            throw new AssertionError(e.getMessage());
        } catch (JsonAssertExtendedException e) {
            throw new JsonMappersException("Invalid extend setup: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new JsonMappersException("Invalid JSON: " + e.getMessage(), e);
        }
    }

}



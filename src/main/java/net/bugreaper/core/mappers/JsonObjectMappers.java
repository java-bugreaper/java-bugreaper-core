package net.bugreaper.core.mappers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bugreaper.core.exceptions.JsonMappersException;

import java.text.MessageFormat;
import java.util.Map;


/**
 * Utility class providing helper methods for working with JSON objects using Jackson {@link JsonNode}.
 *
 * <p>This class simplifies common JSON operations such as creating, reading,
 * modifying, merging, and comparing JSON objects represented by
 * {@link JsonNode}.</p>
 *
 * <p>It is designed to simplify interaction with Jackson JSON structures
 * without requiring direct usage of {@link ObjectMapper} in test code.</p>
 *
 *
 * <p>This utility class contains only static methods and should not be instantiated.</p>
 */
public final class JsonObjectMappers {

    //  used in modules: redis

    private JsonObjectMappers() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Shared Jackson ObjectMapper instance used for JSON serialization and
     * deserialization.
     *
     * <p>The mapper is reused across all utility methods because
     * {@link ObjectMapper} is thread-safe after configuration.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Validates that the provided string contains valid JSON.
     *
     * <p>This method only performs validation and does not return the parsed
     * JSON structure.
     *
     * @param json JSON string to validate
     * @throws JsonMappersException if the provided string is invalid JSON
     */
    public static void validateJson(String json) {
        convertStringToJsonObject(json);
    }


    /**
     * Parses the provided JSON string into a Jackson {@link JsonNode} object.
     *
     * <p>This method validates that the provided string contains valid JSON and
     * returns a tree representation that can be used for JSON processing.
     *
     * @param json JSON string to parse
     * @return parsed JSON tree representation
     * @throws JsonMappersException if the provided string is invalid JSON
     */
    public static JsonNode convertStringToJsonObject(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (Exception e) {
            throw new JsonMappersException("Invalid JSON:\n" + json, e);
        }
    }

    /**
     * Converts a JSON object string into a {@code Map<String, String>}.
     *
     * @param json JSON object string to convert
     * @return map containing JSON property names and string values
     * @throws JsonMappersException if JSON cannot be converted to a map
     */
    public static Map<String, String> convertJsonToStringMap(String json) {

        try {
            return OBJECT_MAPPER.readValue(
                    json,
                    new TypeReference<>() {
                    }
            );
        } catch (Exception e) {
            throw new JsonMappersException(
                    "Failed to convert JSON to Map: " + json, e
            );
        }
    }

    /**
     * Converts a JSON object string into a {@code Map<String, Object>}.
     *
     * @param json JSON object to convert
     * @return map containing the deserialized JSON data
     * @throws JsonMappersException if the provided string is not valid JSON
     */
    public static Map<String, Object> convertJsonToMap(String json) {
        TypeReference<Map<String, Object>> type =
                new TypeReference<>() {
                };

        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new JsonMappersException(MessageFormat.format("Not valid Json for mapping\n{0}", json), e);
        }
    }

    /**
     * Converts a map of key-value pairs into a JSON string.
     *
     * @param fields map containing JSON field names and string values
     * @return JSON representation of the provided fields
     * @throws JsonMappersException if conversion fails
     */
    public static String convertStringMapToJson(Map<String, String> fields) {

        if (fields == null) {
            throw new JsonMappersException("Map must not be null.");
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            throw new JsonMappersException(
                    "Failed to convert fields to JSON",
                    e
            );
        }
    }



}

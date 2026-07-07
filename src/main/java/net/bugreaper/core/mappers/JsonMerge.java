package net.bugreaper.core.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class JsonMerge {

    private JsonMerge() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Merges two JSON objects by replacing only top-level fields.
     * <p>
     * Every field present in {@code providedJson} replaces the corresponding field
     * in {@code templateJson}. Nested objects and arrays are <b>not merged</b>;
     * they are copied entirely from {@code providedJson}.
     *
     * <p><b>Example</b></p>
     * <pre>{@code
     * template:
     * {
     *   "id": 1,
     *   "params": {
     *     "name": "Alex",
     *     "age": 30
     *   }
     * }
     *
     * provided:
     * {
     *   "id": 2,
     *   "params": {
     *     "name": "John"
     *   }
     * }
     *
     * result:
     * {
     *   "id": 2,
     *   "params": {
     *     "name": "John"
     *   }
     * }
     * }</pre>
     *
     * @param templateJson base JSON object
     * @param providedJson JSON containing values to replace
     * @return merged JSON as a formatted string
     * @throws IllegalArgumentException if either JSON is invalid or the root element
     *                                  is not a JSON object
     */
    public static String mergeJson(String templateJson, String providedJson) {
        try {
            ObjectNode template = MAPPER.readTree(templateJson).deepCopy();
            ObjectNode provided = (ObjectNode) MAPPER.readTree(providedJson);

            provided.properties().forEach(entry ->
                    template.set(entry.getKey(), entry.getValue()));

            return MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(template);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON", e);
        }
    }

    /**
     * Performs a recursive (deep) merge of two JSON objects.
     * <p>
     * Merge rules:
     * <ul>
     *     <li><b>Objects</b> are merged recursively.</li>
     *     <li><b>Arrays</b> are merged by index.
     *         <ul>
     *             <li>If an element exists in both arrays, it is merged recursively.</li>
     *             <li>If an element exists only in {@code providedJson}, it is appended.</li>
     *             <li>If an element exists only in {@code templateJson}, it is preserved.</li>
     *         </ul>
     *     </li>
     *     <li><b>Primitive values</b> (string, number, boolean, null) from
     *     {@code providedJson} replace those in {@code templateJson}.</li>
     * </ul>
     *
     * <p><b>Example</b></p>
     * <pre>{@code
     * template:
     * {
     *   "user": {
     *     "name": "Alex",
     *     "products": [
     *       {
     *         "cat": 1,
     *         "array": [
     *           {
     *             "product": "monitor",
     *             "price": 99.99
     *           }
     *         ]
     *       }
     *     ]
     *   }
     * }
     *
     * provided:
     * {
     *   "user": {
     *     "products": [
     *       {
     *         "array": [
     *           {
     *             "product": "mouse"
     *           }
     *         ]
     *       }
     *     ]
     *   }
     * }
     *
     * result:
     * {
     *   "user": {
     *     "name": "Alex",
     *     "products": [
     *       {
     *         "cat": 1,
     *         "array": [
     *           {
     *             "product": "mouse",
     *             "price": 99.99
     *           }
     *         ]
     *       }
     *     ]
     *   }
     * }
     * }</pre>
     *
     * @param templateJson base JSON object
     * @param providedJson JSON containing overriding values
     * @return recursively merged JSON as a formatted string
     * @throws IllegalArgumentException if either JSON is invalid or the root element
     *                                  is not a JSON object
     */
    public static String mergeJsonDeep(String templateJson, String providedJson) {
        try {
            JsonNode merged = deepMerge(
                    MAPPER.readTree(templateJson),
                    MAPPER.readTree(providedJson));

            return MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(merged);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON", e);
        }
    }

    private static JsonNode deepMerge(JsonNode template, JsonNode provided) {

        if (template.isObject() && provided.isObject()) {
            return mergeObjects((ObjectNode) template, (ObjectNode) provided);
        }

        if (template.isArray() && provided.isArray()) {
            return mergeArrays((ArrayNode) template, (ArrayNode) provided);
        }

        // primitive, string, number, boolean, null
        return provided.deepCopy();
    }

    private static ObjectNode mergeObjects(ObjectNode template, ObjectNode provided) {

        ObjectNode result = template.deepCopy();

        provided.fieldNames().forEachRemaining(field -> {
            JsonNode templateChild = result.get(field);
            JsonNode providedChild = provided.get(field);

            result.set(field,
                    templateChild == null
                            ? providedChild.deepCopy()
                            : deepMerge(templateChild, providedChild));
        });

        return result;
    }

    private static ArrayNode mergeArrays(ArrayNode template, ArrayNode provided) {

        // Explicitly provided empty array replaces the template array.
        if (provided.isEmpty()) {
            return provided.deepCopy();
        }

        ArrayNode result = MAPPER.createArrayNode();

        int max = Math.max(template.size(), provided.size());

        for (int i = 0; i < max; i++) {
            result.add(mergeArrayElement(template, provided, i));
        }

        return result;
    }

    private static JsonNode mergeArrayElement(ArrayNode template, ArrayNode provided, int index) {

        if (index >= template.size()) {
            return provided.get(index).deepCopy();
        }

        if (index >= provided.size()) {
            return template.get(index).deepCopy();
        }

        return deepMerge(template.get(index), provided.get(index));
    }

}

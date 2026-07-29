package net.bugreaper.core.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import net.bugreaper.core.exceptions.JsonMappersException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class JsonObjectMappersTests {


    @Test
    void shouldValidateValidJson() {
        assertDoesNotThrow(() ->
                JsonObjectMappers.validateJson("""
                        {
                          "name":"Alex",
                          "age":30
                        }
                        """));
    }

    @Test
    void shouldThrowWhenValidateInvalidJson() {

        JsonMappersException exception = assertThrows(
                JsonMappersException.class,
                () -> JsonObjectMappers.validateJson("""
                        {
                          "name":"Alex",
                        """)
        );

        assertTrue(exception.getMessage().contains("Invalid JSON"));
    }

    @Test
    void shouldConvertStringToJsonObject() {

        JsonNode actual = JsonObjectMappers.convertStringToJsonObject("""
                {
                  "name":"Alex",
                  "age":30
                }
                """);

        assertEquals("Alex", actual.get("name").asText());
        assertEquals(30, actual.get("age").asInt());
    }

    @Test
    void shouldThrowWhenConvertInvalidJsonToJsonObject() {

        JsonMappersException exception = assertThrows(
                JsonMappersException.class,
                () -> JsonObjectMappers.convertStringToJsonObject("""
                        {
                          "name":"Alex"
                        """)
        );

        assertTrue(exception.getMessage().contains("Invalid JSON"));
    }

    @Test
    void shouldConvertJsonToStringMap() {

        Map<String, String> actual =
                JsonObjectMappers.convertJsonToStringMap("""
                        {
                          "name":"Alex",
                          "age":"30"
                        }
                        """);

        assertEquals(2, actual.size());
        assertEquals("Alex", actual.get("name"));
        assertEquals("30", actual.get("age"));
    }

    @Test
    void shouldThrowWhenConvertInvalidJsonToStringMap() {

        assertThrows(
                JsonMappersException.class,
                () -> JsonObjectMappers.convertJsonToStringMap("""
                        {
                          "name":"Alex"
                        """)
        );
    }

    @Test
    void shouldConvertStringMapToJson() {

        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", "Alex");
        map.put("age", "30");

        String actual = JsonObjectMappers.convertStringMapToJson(map);

        JsonNode json = JsonObjectMappers.convertStringToJsonObject(actual);

        assertEquals("Alex", json.get("name").asText());
        assertEquals("30", json.get("age").asText());
    }

    @Test
    void shouldConvertEmptyMapToJson() {

        String actual = JsonObjectMappers.convertStringMapToJson(Map.of());

        assertEquals("{}", actual);
    }

    @Test
    void shouldThrowWhenConvertNullMapToJson() {

        assertThrows(
                JsonMappersException.class,
                () -> JsonObjectMappers.convertStringMapToJson(null)
        );
    }

    @Test
    void shouldConvertJsonToMap() {

        HashMap<String, Object> actual =
                new HashMap<>(
                        JsonObjectMappers.convertJsonToMap("""
                                {
                                  "id": 1,
                                  "name": "Alex",
                                  "active": true
                                }
                                """)
                );

        assertEquals(3, actual.size());
        assertEquals(1, actual.get("id"));
        assertEquals("Alex", actual.get("name"));
        assertEquals(true, actual.get("active"));
    }

    @Test
    void shouldConvertNestedJsonToMap() {

        HashMap<String, Object> actual =
                new HashMap<>(
                        JsonObjectMappers.convertJsonToMap("""
                                {
                                  "user": {
                                    "name": "Alex",
                                    "age": 30
                                  },
                                  "roles": [
                                    "ADMIN",
                                    "USER"
                                  ]
                                }
                                """)
                );

        @SuppressWarnings("unchecked")
        Map<String, Object> user =
                (Map<String, Object>) actual.get("user");

        @SuppressWarnings("unchecked")
        List<String> roles =
                (List<String>) actual.get("roles");

        assertEquals("Alex", user.get("name"));
        assertEquals(30, user.get("age"));

        assertEquals(2, roles.size());
        assertEquals("ADMIN", roles.get(0));
        assertEquals("USER", roles.get(1));
    }

    @Test
    void shouldConvertEmptyJsonObjectToMap() {

        HashMap<String, Object> actual =
                new HashMap<>(JsonObjectMappers.convertJsonToMap("{}"));

        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldThrowWhenJsonIsInvalid() {

        JsonMappersException exception =
                assertThrows(
                        JsonMappersException.class,
                        () -> JsonObjectMappers.convertJsonToMap("""
                                {
                                  "name":"Alex",
                                """)
                );

        assertTrue(exception.getMessage().contains("Not valid Json for mapping"));
    }

    @Test
    void shouldThrowWhenJsonIsNotObject() {

        assertThrows(
                JsonMappersException.class,
                () -> JsonObjectMappers.convertJsonToMap("""
                        [
                          1,
                          2,
                          3
                        ]
                        """)
        );
    }
}

package net.bugreaper.core.mappers;


import org.hamcrest.MatcherAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.mappers.JsonMappers.*;
import static org.hamcrest.Matchers.is;

class JsonMappersTests {

    String jsonString = """
            {
              "id": 1,
              "text": "some text"
            }""";
    String arrayString = """
            [
            {
              "id": 1,
              "text": "test_1"
            },
            {
              "id": 2,
              "text": "test_2"
            }
            ]""";


    @Test
    void testBeautifierJson() {

        JSONObject json = jsonObjectFromString("""
                {"id": 1}""");
        System.out.println(jsonToStringBeautifier(json));

        MatcherAssert.assertThat(
                "Beautify checked",
                jsonToStringBeautifier(json),
                is("""
                        {
                          "id": 1
                        }"""));

    }

    @Test
    void testBeautifierArray() {

        JSONArray jsonArray = jsonArrayFromString("""
                [{"id": 1}]""");
        System.out.println(jsonArrayToStringBeautifier(jsonArray));

        MatcherAssert.assertThat(
                "Beautify checked",
                jsonArrayToStringBeautifier(jsonArray),
                is("""
                        [
                          {
                            "id": 1
                          }
                        ]"""));

    }

    @Test
    void testFromStringToJsonAddStringDataGetStringByKey() {

        JSONObject json = jsonObjectFromString(jsonString);

        putStringToJson(json, "new_key", "new_date");

        var result = getStringFromJsonObjectByKey(json, "new_key");

        MatcherAssert.assertThat(
                "Transform & grab checked",
                result,
                is("new_date"));
    }

    @Test
    void testFromStringToJsonArray() {

        JSONArray jsonArray = jsonArrayFromString(arrayString);

        putObjectToJsonArrayByNum(jsonArray, 2, """
                {"id": "333"}""");

        JSONObject jsonGrabbed = getObjectFromJsonArrayByNum(jsonArray, 2);

        var result = getStringFromJsonObjectByKey(jsonGrabbed, "id");


        MatcherAssert.assertThat(
                "Transform & grab checked array",
                result,
                is("333"));

        MatcherAssert.assertThat(
                "old data check",
                getStringFromJsonObjectByKey(getObjectFromJsonArrayByNum(jsonArray, 1), "id"),
                is("2"));

    }

    @Test
    void testPutObjectStringToJson() {

        JSONObject json = jsonObjectFromString(jsonString);

        putObjectToJson(json, "new_key",
                """
                        {"id": "333"}""");

        JSONObject jsonGrabbed = getObjectFromJsonByKey(json, "new_key");

        var result = getStringFromJsonObjectByKey(jsonGrabbed, "id");

        MatcherAssert.assertThat(
                "Transform & grab checked",
                result,
                is("333"));
    }

    @Test
    void testPutObjectToJson() {

        JSONObject json = jsonObjectFromString(jsonString);

        JSONObject jsonNewPart = jsonObjectFromString("""
                {"id": "333"}""");

        putObjectToJson(json, "new_key", jsonNewPart);

        JSONObject jsonGrabbed = getObjectFromJsonByKey(json, "new_key");

        var result = getStringFromJsonObjectByKey(jsonGrabbed, "id");

        MatcherAssert.assertThat(
                "Transform & grab checked",
                result,
                is("333"));
    }

    @Test
    void testPutObjectStringToJsonOverride() {

        JSONObject json = jsonObjectFromString("""
                {"test": {"id": "333"}}""");

        putObjectToJson(json, "test",
                """
                        {"id": "444"}""");

        JSONObject jsonGrabbed = getObjectFromJsonByKey(json, "test");

        var result = getStringFromJsonObjectByKey(jsonGrabbed, "id");

        MatcherAssert.assertThat(
                "Add & grab override",
                result,
                is("444"));
    }
}

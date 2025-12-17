package net.bugreaper.core.mappers;


import net.bugreaper.core.exceptions.JsonMappersException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;


public final class JsonMappers {

    private JsonMappers() {
        throw new IllegalStateException("Utility class");
    }

    // transform

    public static JSONObject jsonObjectFromString(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException ex) {
            throw new JsonMappersException("Failed to validate JSON", ex);
        }
    }

    public static JSONArray jsonArrayFromString(String arrayString) {
        try {
            return new JSONArray(arrayString);
        } catch (JSONException ex) {
            throw new JsonMappersException(
                    MessageFormat.format("Failed convert string to JsonArray:\n{0}", arrayString), ex);
        }
    }

    public static String jsonToStringBeautifier(JSONObject jsonObject) {
        try {
            return jsonObject.toString(2);
        } catch (JSONException e) {
            throw new JsonMappersException(e);
        }
    }

    public static String jsonArrayToStringBeautifier(JSONArray jsonArray) {
        try {
            return jsonArray.toString(2);
        } catch (JSONException e) {
            throw new JsonMappersException(e);
        }
    }

    //put to JSON

    public static JSONObject putStringToJson(JSONObject jsonObject, String key, String value) {
        try {
            return jsonObject.put(key, value);
        } catch (JSONException e) {
            throw new JsonMappersException(e);
        }
    }

    public static JSONObject putObjectToJson(JSONObject jsonObject, String key, String jsonValue) {
        try {
            return jsonObject.put(key, jsonObjectFromString(jsonValue));
        } catch (JSONException e) {
            throw new JsonMappersException(e);
        }
    }

    public static JSONObject putObjectToJson(JSONObject jsonObject, String key, JSONObject jsonValue) {
        try {
            return jsonObject.put(key, jsonValue);
        } catch (JSONException e) {
            throw new JsonMappersException(e);
        }
    }

    public static JSONArray putObjectToJsonArrayByNum(JSONArray jsonArray, int num, String jsonValue) {
        try {
            return jsonArray.put(num, jsonObjectFromString(jsonValue));
        } catch (JSONException e) {
            throw new JsonMappersException(e);
        }
    }

    //grab from JSON

    public static JSONObject getObjectFromJsonByKey(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            throw new JsonMappersException(e);
        }
    }

    public static String getStringFromJsonObjectByKey(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException ex) {
            throw new JsonMappersException(ex);
        }
    }

    //grab from JSONArray

    public static JSONObject getObjectFromJsonArrayByNum(JSONArray jsonArray, int num) {
        try {
            return jsonArray.getJSONObject(num);
        } catch (JSONException e) {
            throw new JsonMappersException(e);
        }
    }

}

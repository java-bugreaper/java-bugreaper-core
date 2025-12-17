package net.bugreaper.core.core.mappers;


import net.bugreaper.core.exceptions.JsonMappersException;
import net.bugreaper.core.mappers.JsonMappers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static net.bugreaper.core.mappers.JsonMappers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonMappersExceptionsTests {

    String brokenJson = """
            {
              "id: 1,
            }""";
    String invalidArray = """
            [{
              "id": 2
            }""";
    String validArray = """
            [{
              "id": 1
            }]""";


    @Test
    void utilityClass() throws NoSuchMethodException {
        Constructor<JsonMappers> constructor = JsonMappers.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assert (cause instanceof IllegalStateException);
        assert ("Utility class".equals(cause.getMessage()));
    }


    @Test
    void jsonObjectFromStringException() {


        Throwable exception = assertThrows(JsonMappersException.class, () ->
                jsonObjectFromString(brokenJson));

        MatcherAssert.assertThat(
                "Exception for jsonObjectFromString wrong JSON ",
                exception.getMessage(),
                is("Failed to validate JSON"));
    }

    @Test
    void jsonArrayFromStringException() {
        Throwable exception = assertThrows(JsonMappersException.class, () ->
                jsonArrayFromString(invalidArray));

        MatcherAssert.assertThat(
                "Exception for jsonArrayFromString wrong ARRAY ",
                exception.getMessage(),
                StringContains.containsString("Failed convert string to JsonArray"));
    }


    @Test
    void getObjectFromJsonArrayByNumException() {

        JSONArray array = jsonArrayFromString(validArray);

        Throwable exception = assertThrows(JsonMappersException.class, () ->
                getObjectFromJsonArrayByNum(array, 1));

        MatcherAssert.assertThat(
                "Exception getObjectFromJsonArrayByNum element is absent",
                exception.getMessage(),
                StringContains.containsString("JSONException")); //message changed in each version
    }

}

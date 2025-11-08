package io.bugreaper.core.mappers;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import static io.bugreaper.core.mappers.StringMappers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringMappersTests {


    @Test
    void staticClass() throws NoSuchMethodException {
        Constructor<StringMappers> constructor = StringMappers.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assert (cause instanceof IllegalStateException);
        assert ("Utility class".equals(cause.getMessage()));
    }


    @Test
    void testMapJsonSuccess() {

       var template = """
                {
                  "status": ${status},
                  "statusName": "${name}"
                }
                """;

        var ar = stringMapper(
                template,
                Map.of(
                        "status", 11,
                        "name", "Something"
                ));

        var er = """
                {
                  "status": 11,
                  "statusName": "Something"
                }
                """;

        assertEquals(er, ar, "JSON generate successful");
    }

    @Test
    void testMapJsonFailed() {

        var template = """
                {
                  "status": ${status},
                  "statusName": "${name}"
                }
                """;

        Throwable exception = assertThrows(Exception.class, () ->
                stringMapper(
                        template,
                        Map.of(
                                "status", 11,
                                "wrong", "Something"
                        )));

        MatcherAssert.assertThat(
                "Miss ono of env for template",
                exception.getMessage(),
                StringContains.containsString("no data for variable: name"));
    }

    @Test
    void testListAttach(){
        ArrayList<String> list = new ArrayList<>();
        list.add("one");
        list.add("""
                {"id": 2}""");
        list.add("three");

        String ar = """
                [
                one
                
                -----------
                
                {"id": 2}
                
                -----------
                
                three
                ]""";

        assertEquals(listToString(list),
                ar,
                "List beautify for attach");
    }

    @Test
    void testListAttachEmpty(){
        ArrayList<String> list = new ArrayList<>();

        String ar = """
                [
                
                ]""";

        assertEquals(listToString(list),
                ar,
                "List(empty) beautify for attach");
    }

    @Test
    void testListAttachNull(){
        ArrayList<String> list = null;

        String ar = "";

        assertEquals(listToString(list),
                ar,
                "List null beautify for attach");
    }

    @Test
    void testFormatMillisecondsOnly(){
        assertEquals("105 milliseconds",
                formatMilliseconds(105),
                "message with milliseconds");
    }

    @Test
    void testFormatMillisecondOnly(){
        assertEquals("1 millisecond",
                formatMilliseconds(1),
                "message with millisecond");
    }

    @Test
    void testFormatMillisecondZeroOnly(){
        assertEquals("0 milliseconds",
                formatMilliseconds(0),
                "message with 0 milliseconds");
    }

    @Test
    void testFormatSecondsOnly(){
        assertEquals("5 seconds",
                formatMilliseconds(5000),
                "message with seconds");
    }

    @Test
    void testFormatSecondsAndMilliseconds(){
        assertEquals("1 second 330 milliseconds",
                formatMilliseconds(1330),
                "message with seconds");
    }

}

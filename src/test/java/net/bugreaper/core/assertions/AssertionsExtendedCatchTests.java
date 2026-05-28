package net.bugreaper.core.assertions;

import org.junit.jupiter.api.Test;

import static net.bugreaper.core.assertions.JsonAsserts.assertJsonsExtended;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SuppressWarnings({"squid:S2699", "java:S5976"})
class AssertionsExtendedCatchTests {

    String actual = """
            {
              "user": {
                "name": "Alex",
                "date": "2020-01-01 10:00:00",
                "email": "alex@gmail.com",
                "age": 15,
                "dig": 2.14,
                "private": null,
                "isActive": false,
                "array": [ "25", "26", "27", "27" ],
                "arrayJson": [
                  {
                    "id": 1,
                    "test": 5
                  },
                  {
                    "id": 2,
                    "test": 4
                  }
                ]
              }
            }""";

    @Test
    void assertJsonsExtendedNoOptionsFailedTest() {


        String expected = """
                {
                  "user": {
                    "name": "Alexey",
                    "date": "2020-01-01 10:00:00",
                    "age": 22,
                    "dig": 2.33,
                    "private": "test",
                    "isActive": true,
                    "array": [
                      "27",
                      "28"
                    ],
                    "arrayJson": [
                      {
                        "id": 1,
                        "test": 4
                      }
                    ]
                  }
                }""";

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertJsonsExtended(expected, actual));

        assertEquals(
                """
                        JSON comparison failed:
                        • user.name: expected [Alexey] but was [Alex]
                        • user.age: expected [22] but was [15]
                        • user.dig: expected [2.33] but was [2.14]
                        • user.private: expected [test] but was [null]
                        • user.isActive: expected [true] but was [false]
                        • user.array: expected element not found -> "28"
                        • user.arrayJson: expected element not found -> {"id":1,"test":4}""",
                exception.getMessage());
    }

    @Test
    void assertJsonsExtendedWithOptionsFailed1Test() {


        String expected = """
                {
                  "user": {
                    "name:like": "Ann",
                    "email:regex": ".*@ukr.net",
                    "date": null,
                    "age:>": 22,
                    "dig:>=": 2.33,
                    "private": "test",
                    "noActive:exists": true,
                    "isActive:exists": false,
                    "array:size": 1,
                    "arrayJson": [
                      {
                        "id:<": 2,
                        "test:in": [ 1, 8 ]
                      }
                    ]
                  }
                }""";
        Throwable exception = assertThrows(AssertionError.class, () ->
                assertJsonsExtended(expected, actual));

        assertEquals(
                """
                        JSON comparison failed:
                        • user.name: expected like [Ann] but was [Alex]
                        • user.email: expected regex [.*@ukr.net] but was [alex@gmail.com]
                        • user.date: expected null but was 2020-01-01 10:00:00
                        • user.age: expected >[22] but was [15]
                        • user.dig: expected >=[2.33] but was [2.14]
                        • user.private: expected [test] but was [null]
                        • user.noActive: field missing
                        • user.isActive: unexpected field exists
                        • user.array: array size mismatch. expected 1 but was 4
                        • user.arrayJson: expected element not found -> {"id:<":2,"test:in":[1,8]}""",
                exception.getMessage());
    }

    @Test
    void assertJsonsExtendedWithOptionsFailed2Test() {


        String expected = """
                {
                  "user": {
                    "age:in": [20, 21],
                    "array:distinct": true,
                    "arrayJson": [
                      {
                        "id": 1,
                        "test": 5
                      }
                    ]
                  }
                }""";
        Throwable exception = assertThrows(AssertionError.class, () ->
                assertJsonsExtended(expected, actual));

        assertEquals(
                """
                        JSON comparison failed:
                        • user.age: expected in [20,21] but was [15]
                        • user.array: expected distinct elements [true] but was [false]""",
                exception.getMessage());
    }

    @Test
    void assertJsonsExtendedWithOptionsFailed3Test() {


        String expected = """
                {
                  "user": {
                    "age:>=": 16,
                    "age:!=": 15,
                    "age:=": 77,
                    "array": [ "25", "21"]
                  }
                }""";
        Throwable exception = assertThrows(AssertionError.class, () ->
                assertJsonsExtended(expected, actual));

        assertEquals(
                """
                        JSON comparison failed:
                        • user.age: expected >=[16] but was [15]
                        • user.age: expected !=[15] but was [15]
                        • user.age: expected =[77] but was [15]
                        • user.array: expected element not found -> "21\"""",
                exception.getMessage());
    }

    @Test
    void assertJsonsExtendedWrongKeyTypeTest() {


        String expected = """
                {
                  "user": {
                    "name:size": 16
                  }
                }""";
        Throwable exception = assertThrows(AssertionError.class, () ->
                assertJsonsExtended(expected, actual));

        assertEquals(
                """
                        JSON comparison failed:
                        • user.name: expected array but was STRING""",
                exception.getMessage());
    }

}

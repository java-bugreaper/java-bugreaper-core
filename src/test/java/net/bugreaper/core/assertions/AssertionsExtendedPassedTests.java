package net.bugreaper.core.assertions;

import org.junit.jupiter.api.Test;

import static net.bugreaper.core.assertions.JsonAsserts.*;


@SuppressWarnings({"squid:S2699", "java:S5976"})
class AssertionsExtendedPassedTests {

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
                "array": [ "25", "26", "27" ],
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
    void assertJsonsExtendedNoOptionsTest() {

        assertJsonsExtended("""
                {
                  "user": {
                    "name": "Alex",
                    "date": "2020-01-01 10:00:00",
                    "age": 15,
                    "dig": 2.14,
                    "private": null,
                    "array": [
                      "27",
                      "25"
                    ],
                    "arrayJson": [
                      {
                        "id": 1,
                        "test": 5
                      }
                    ]
                  }
                }""", actual);
    }

    @Test
    void assertJsonsExtendedWithOptions1Test() {

        assertJsonsExtended("""
                {
                  "user": {
                    "name:like": "le",
                    "date:regex": "\\\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])",
                    "age:>": 10,
                    "dig:>": 2,
                    "isActive:exists": true,
                    "noActive:exists": false,
                    "array": [
                      "27",
                      "25"
                    ],
                    "arrayJson": [
                      {
                        "id:<": 2,
                        "test:in": [ 5, 6 ]
                      }
                    ]
                  }
                }""", actual);
    }


    @Test
    void assertJsonsExtendedWithOptions2Test() {

        assertJsonsExtended("""
                {
                  "user": {
                    "name:like": "Al",
                    "email:regex": ".*@gmail.com",
                    "age:<=": 15,
                    "dig:>=": 2.111,
                    "array:exists": true,
                    "arrayJson": [
                      {
                        "id:>=": 1,
                        "test:in": [ 5, 6 ]
                      }
                    ]
                  }
                }""", actual);
    }

    @Test
    void assertJsonsExtendedWithArrayOptions1Test() {

        assertJsonsExtended("""
                {
                  "user": {
                    "array:distinct": true,
                    "arrayJson:size": 2
                  }
                }""", actual);
    }

    @Test
    void assertJsonsExtendedWithArrayOptions2Test() {

        assertJsonsExtended("""
                {
                  "user": {
                    "private:like": "null",
                    "array:size": 3,
                    "arrayJson:size": 2
                  }
                }""", actual);
    }

    @Test
    void JsonArraysSupportedTest() {
        assertJsonsExtended("""
                        [{
                         "num":1
                        }]""",
                """
                        [{
                         "num":1
                        }]""");
    }


}

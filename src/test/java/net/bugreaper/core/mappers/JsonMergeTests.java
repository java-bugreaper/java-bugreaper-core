package net.bugreaper.core.mappers;


import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonMergeTests {

    String jsonTemplate = """
            {
              "id": 1,
              "user": "Alex",
              "body": {
                "test": 1,
                "tt": "tt"
              },
              "amount": 0,
              "params": {
                "data": {
                  "name": "Alex",
                  "products": [
                    {
                      "cat": 1,
                      "array": [
                        {
                          "product": "monitor",
                          "price": 99.99
                        }
                      ]
                    }
                  ]
                }
              }
            }""";

    String jsonProvided = """
            {
              "id": 2,
              "user": "Anna",
              "email": "anna@test.com",
              "amount": 5.77,
              "params": {
                "data": {
                  "products": [
                    {
                      "array": [
                        {
                          "product": "mouse",
                          "category": "PC"
                        },
                        {
                          "product": "keyboard",
                          "category": "PC"
                        }
                      ]
                    }
                  ]
                }
              }
            }""";

    String jsonProvided2 = """
            {
              "id": 2,
              "user": "John",
              "email": "hohn@test.com",
              "amount": 5.77
            }""";

    String jsonProvided3 = """
            {
              "id": 3,
              "user": "Dimon",
              "body": null,
              "email": "dimon@test.com",
              "amount": null,
              "params": {
                "data": {
                  "products": []
                }
              }
            }""";

    @Test
    void mergeJson1Test() {

        assertEquals(
                """
                        {
                          "id" : 2,
                          "user" : "Anna",
                          "body" : {
                            "test" : 1,
                            "tt" : "tt"
                          },
                          "amount" : 5.77,
                          "params" : {
                            "data" : {
                              "products" : [ {
                                "array" : [ {
                                  "product" : "mouse",
                                  "category" : "PC"
                                }, {
                                  "product" : "keyboard",
                                  "category" : "PC"
                                } ]
                              } ]
                            }
                          },
                          "email" : "anna@test.com"
                        }""",
                JsonMerge.mergeJson(jsonTemplate, jsonProvided));

    }

    @Test
    void mergeJsonDeep1Test() {

        assertEquals(
                """
                        {
                          "id" : 2,
                          "user" : "Anna",
                          "body" : {
                            "test" : 1,
                            "tt" : "tt"
                          },
                          "amount" : 5.77,
                          "params" : {
                            "data" : {
                              "name" : "Alex",
                              "products" : [ {
                                "cat" : 1,
                                "array" : [ {
                                  "product" : "mouse",
                                  "price" : 99.99,
                                  "category" : "PC"
                                }, {
                                  "product" : "keyboard",
                                  "category" : "PC"
                                } ]
                              } ]
                            }
                          },
                          "email" : "anna@test.com"
                        }""",
                JsonMerge.mergeJsonDeep(jsonTemplate, jsonProvided));

    }

    @Test
    void mergeJsonDeep1MirrorTest() {

        assertEquals(
                """
                        {
                          "id" : 1,
                          "user" : "Alex",
                          "email" : "anna@test.com",
                          "amount" : 0,
                          "params" : {
                            "data" : {
                              "products" : [ {
                                "array" : [ {
                                  "product" : "monitor",
                                  "category" : "PC",
                                  "price" : 99.99
                                }, {
                                  "product" : "keyboard",
                                  "category" : "PC"
                                } ],
                                "cat" : 1
                              } ],
                              "name" : "Alex"
                            }
                          },
                          "body" : {
                            "test" : 1,
                            "tt" : "tt"
                          }
                        }""",
                JsonMerge.mergeJsonDeep(jsonProvided, jsonTemplate));

    }

    @Test
    void mergeJson2Test() {

        assertEquals(
                """
                        {
                          "id" : 2,
                          "user" : "John",
                          "body" : {
                            "test" : 1,
                            "tt" : "tt"
                          },
                          "amount" : 5.77,
                          "params" : {
                            "data" : {
                              "name" : "Alex",
                              "products" : [ {
                                "cat" : 1,
                                "array" : [ {
                                  "product" : "monitor",
                                  "price" : 99.99
                                } ]
                              } ]
                            }
                          },
                          "email" : "hohn@test.com"
                        }""",
                JsonMerge.mergeJson(jsonTemplate, jsonProvided2));

    }

    @Test
    void mergeJsonDeep2Test() {

        assertEquals(
                """
                        {
                          "id" : 2,
                          "user" : "John",
                          "body" : {
                            "test" : 1,
                            "tt" : "tt"
                          },
                          "amount" : 5.77,
                          "params" : {
                            "data" : {
                              "name" : "Alex",
                              "products" : [ {
                                "cat" : 1,
                                "array" : [ {
                                  "product" : "monitor",
                                  "price" : 99.99
                                } ]
                              } ]
                            }
                          },
                          "email" : "hohn@test.com"
                        }""",
                JsonMerge.mergeJsonDeep(jsonTemplate, jsonProvided2));

    }

    @Test
    void mergeJson3Test() {

        assertEquals(
                """
                        {
                          "id" : 3,
                          "user" : "Dimon",
                          "body" : null,
                          "amount" : null,
                          "params" : {
                            "data" : {
                              "products" : [ ]
                            }
                          },
                          "email" : "dimon@test.com"
                        }""",
                JsonMerge.mergeJson(jsonTemplate, jsonProvided3));

    }

    @Test
    void mergeJsonDeep3Test() {

        assertEquals(
                """
                        {
                          "id" : 3,
                          "user" : "Dimon",
                          "body" : null,
                          "amount" : null,
                          "params" : {
                            "data" : {
                              "name" : "Alex",
                              "products" : [ ]
                            }
                          },
                          "email" : "dimon@test.com"
                        }""",
                JsonMerge.mergeJsonDeep(jsonTemplate, jsonProvided3));

    }

    @Test
    void mergeJsonExceptionNotJsonTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                JsonMerge.mergeJson("text", jsonProvided));

        assertEquals("Invalid JSON",
                exception.getMessage());
    }

    @Test
    void mergeJsonExceptionNullTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                JsonMerge.mergeJson(jsonTemplate, null));

        assertEquals("argument \"content\" is null",
                exception.getMessage());
    }

    @Test
    void mergeJsonExceptionArrayTest() {

        Throwable exception = assertThrows(ClassCastException.class, () ->
                JsonMerge.mergeJson(jsonTemplate, """
                        [{"id": 1}]"""));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("ArrayNode cannot be cast"));
    }

    @Test
    void mergeJsonDeepExceptionNullTemplateTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                JsonMerge.mergeJsonDeep(null, jsonProvided));

        assertEquals("argument \"content\" is null",
                exception.getMessage());
    }

    @Test
    void mergeJsonDeepExceptionNotJsonProvideTest() {

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                JsonMerge.mergeJsonDeep(jsonTemplate, "text"));

        assertEquals("Invalid JSON",
                exception.getMessage());
    }

}

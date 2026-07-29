package net.bugreaper.core.mappers;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonMerge2Tests {

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
                        },
                        {
                          "product": "processor",
                          "price": 300.99
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

    @Test
    void mergeJsonAr1Test() {

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
    void mergeJsonArDeep1Test() {

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
                                  "price" : 300.99,
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
    void mergeJsonArDeepArrayTest() {

        assertEquals(
                """
                        {
                          "user" : "Anna",
                          "list" : [ 1, 2 ]
                        }""",
                JsonMerge.mergeJsonDeep("""
                        {
                          "user" : "Anna",
                          "list" : [1,2]
                        }""",
                        """
                        {
                          "list" : [1]
                        }"""));

    }

    @Test
    void mergeJsonArDeepArray2Test() {

        assertEquals(
                """
                        {
                          "user" : "Anna",
                          "list" : [ 3, 2 ]
                        }""",
                JsonMerge.mergeJsonDeep("""
                        {
                          "user" : "Anna",
                          "list" : [1,2]
                        }""",
                        """
                        {
                          "list" : [3]
                        }"""));

    }

    @Test
    void mergeJsonArDeepArray3Test() {

        assertEquals(
                """
                        {
                          "user" : "Anna",
                          "list" : [ 3, 2, 1 ]
                        }""",
                JsonMerge.mergeJsonDeep("""
                        {
                          "user" : "Anna",
                          "list" : [1,2]
                        }""",
                        """
                        {
                          "list" : [3,2,1]
                        }"""));

    }

}

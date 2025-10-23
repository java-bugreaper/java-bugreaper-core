# BUGREAPER CORE MODULE

Core part for "BUGREAPER" family modules used for testing

### Requirements:

    JAVA >= 17
    Allure server >= 2.15 

### Logging:

    <logger name="io.bugreaper.core" level="INFO"/>


### Dependencies


| Lib                                                                                                | Version  | Description                                    |
|----------------------------------------------------------------------------------------------------|----------|------------------------------------------------|
| [Junit5](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api)                   | 5.12.1   | test framework                                 |
| [Hamcrest](https://mvnrepository.com/artifact/org.hamcrest/hamcrest)                               | 3.0      | matcher framework                              |
| [AspectJ Weaver](https://mvnrepository.com/artifact/org.aspectj/aspectjweaver)                     | 1.9.22.1 | applies aspects to Java classes (for test run) |
| [Allure Attachments](https://mvnrepository.com/artifact/io.qameta.allure/allure-attachments)       | 2.30.0   | Allure report generate                         |
| [Awaitility](https://mvnrepository.com/artifact/org.awaitility/awaitility)                         | 4.3.0    | for await in tests                             |
| [Logback Classic](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic)               | 1.5.18   | for logging                                    |
| [Apache Commons Text](https://mvnrepository.com/artifact/org.apache.commons/commons-text)          | 1.13.0   | for Strings mapping                            |
| [Jackson Databind](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind) | 2.18.3   | for JSON features                              |
| [JSONAssert](https://mvnrepository.com/artifact/org.skyscreamer/jsonassert)                        | 1.5.1    | for JSON asserts                               |
| [JSONObject](https://mvnrepository.com/artifact/org.json/json)                                     | 20250517 | for JSON parse                                 |
| [JsonSchemaValidator](https://mvnrepository.com/artifact/com.networknt/json-schema-validator)      | 1.0.76   | for JSON Schema validate                       |
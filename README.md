# BUGREAPER CORE MODULE

### Core part for "BUGREAPER" family modules used for testing

Contains packages and wrapped methods for testing with pre-prepared steps and attachments for reports


## [apiDOC](https://bug-reaper.gitlab.io/java-bugreaper-core/apidocs/index.html)

## [Project information](https://bug-reaper.gitlab.io/java-bugreaper-core)

## [Sandbox project with examples](https://gitlab.com/bug-reaper/java-bugreaper-sandbox)

## [BugReaper Family](https://gitlab.com/bug-reaper)


### Requirements:

    JAVA >= 17
    Allure server >= 2.15 (only if push reports)
    IntelliJ IDEA >= 2025.x (for run tests from IDE UI)

### Logging:

    <logger name="net.bugreaper.core" level="INFO"/>


### Dependencies


| Lib                                                                                                | Version  | Description                                    |
|----------------------------------------------------------------------------------------------------|----------|------------------------------------------------|
| [Junit5](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api)                   | 6.1.0    | test framework                                 |
| [Junit Launcher](https://mvnrepository.com/artifact/org.junit.platform/junit-platform-launcher)    | 6.1.0    | for tests launch                               |
| [Hamcrest](https://mvnrepository.com/artifact/org.hamcrest/hamcrest)                               | 3.0      | matcher framework                              |
| [AspectJ Weaver](https://mvnrepository.com/artifact/org.aspectj/aspectjweaver)                     | 1.9.22.1 | applies aspects to Java classes (for test run) |
| [Allure Attachments](https://mvnrepository.com/artifact/io.qameta.allure/allure-attachments)       | 2.27.0   | Allure report generate                         |
| [Logback Classic](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic)               | 1.5.36   | for logging                                    |
| [Awaitility](https://mvnrepository.com/artifact/org.awaitility/awaitility)                         | 4.3.0    | for await in tests                             |
| [Apache Commons Text](https://mvnrepository.com/artifact/org.apache.commons/commons-text)          | 1.15.0   | for Strings mapping                            |
| [Jackson Databind](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind) | 2.21.4   | for JSON features                              |
| [JSONAssert](https://mvnrepository.com/artifact/org.skyscreamer/jsonassert)                        | 1.5.1    | for JSON asserts                               |
| [JSONObject](https://mvnrepository.com/artifact/org.json/json)                                     | 20250517 | for JSON parse                                 |
| [JsonSchemaValidator](https://mvnrepository.com/artifact/com.networknt/json-schema-validator)      | 1.0.76   | for JSON Schema validate                       |
| [SnakeYAML](https://mvnrepository.com/artifact/org.yaml/snakeyaml)                                 | 2.2      | for YML config parse                           |
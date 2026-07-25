# BUGREAPER CORE MODULE

https://bugreaper.net/

<a href="https://bugreaper.net/core">
  <img src="https://gitlab.com/uploads/-/system/project/avatar/75490960/bugreaper-core_compr_192.png" alt="Project Logo" width="192" />
</a>

[![GitLab Build Status](https://gitlab.com/bug-reaper/java-bugreaper-core/badges/main/pipeline.svg)](https://gitlab.com/bug-reaper/java-bugreaper-core/-/pipelines?scope=all&ref=main)
[![GitLab Test Coverage](https://gitlab.com/bug-reaper/java-bugreaper-core/badges/main/coverage.svg?job=)](https://gitlab.com/bug-reaper/java-bugreaper-core)
[![License](https://img.shields.io/gitlab/license/bug-reaper/java-bugreaper-core?style=flat-square&color=green)](https://gitlab.com/bug-reaper/java-bugreaper-core/-/blob/main/LICENSE)

[![GitLab Latest Release](https://gitlab.com/bug-reaper/java-bugreaper-core/-/badges/release.svg)](https://gitlab.com/bug-reaper/java-bugreaper-core/-/releases)
[![Maven Latest Release](https://img.shields.io/maven-central/v/net.bugreaper/core)](https://mvnrepository.com/artifact/net.bugreaper/core)


Is the foundation of the BugReaper test helper framework.
It contains shared utilities, common testing packages, and wrapped methods with pre-built steps and report attachments,
providing reusable functionality that is used across all other BugReaper modules to simplify test development and reporting.


## [apiDOC](https://bug-reaper.gitlab.io/java-bugreaper-core/apidocs/index.html)

## [Project information](https://bug-reaper.gitlab.io/java-bugreaper-core)

## [Sandbox project with examples](https://gitlab.com/bug-reaper/java-bugreaper-sandbox)

## [BugReaper Family](https://gitlab.com/bug-reaper)


### Requirements:

    JAVA >= 17

### Logging:

    <logger name="net.bugreaper.core" level="INFO"/>


### Dependencies


| Lib                                                                                                | Version  | Description                                    |
|----------------------------------------------------------------------------------------------------|----------|------------------------------------------------|
| [Junit5](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api)                   | 6.1.1    | test framework                                 |
| [Junit Launcher](https://mvnrepository.com/artifact/org.junit.platform/junit-platform-launcher)    | 6.1.1    | for tests launch                               |
| [Hamcrest](https://mvnrepository.com/artifact/org.hamcrest/hamcrest)                               | 3.0      | matcher framework                              |
| [AspectJ Weaver](https://mvnrepository.com/artifact/org.aspectj/aspectjweaver)                     | 1.9.25.1 | applies aspects to Java classes (for test run) |
| [Allure Attachments](https://mvnrepository.com/artifact/io.qameta.allure/allure-attachments)       | 2.35.3   | Allure report generate                         |
| [Logback Classic](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic)               | 1.5.38   | for logging                                    |
| [Awaitility](https://mvnrepository.com/artifact/org.awaitility/awaitility)                         | 4.3.0    | for await in tests                             |
| [Apache Commons Text](https://mvnrepository.com/artifact/org.apache.commons/commons-text)          | 1.15.0   | for Strings mapping                            |
| [Jackson Databind](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind) | 2.22.1   | for JSON features                              |
| [JSONAssert](https://mvnrepository.com/artifact/org.skyscreamer/jsonassert)                        | 1.5.3    | for JSON asserts                               |
| [JSONObject](https://mvnrepository.com/artifact/org.json/json)                                     | 20260522 | for JSON parse                                 |
| [JsonSchemaValidator](https://mvnrepository.com/artifact/com.networknt/json-schema-validator)      | 1.0.76   | for JSON Schema validate                       |
| [SnakeYAML](https://mvnrepository.com/artifact/org.yaml/snakeyaml)                                 | 2.6      | for YML config parse                           |
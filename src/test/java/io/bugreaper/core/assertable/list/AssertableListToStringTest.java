package io.bugreaper.core.assertable.list;


import io.bugreaper.core.assertable.stringlist.asserters.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.*;


//this tests for Allure Step description substring
class AssertableListToStringTest {


    @Test
    void testMessageElementsCount() {
        String message =  new ElementsCount(2).toString();

        assertEquals(
                "should have count EQUAL to: 2",
                message);
    }


    @Test
    void testMessageIsJsonType() {
        String message =  new IsJsonType().toString();

        assertEquals(
                "should have value of JSON type",
                message);
    }

    @Test
    void testMessageJsonContains() {
        String message =  new JsonContains("test").toString();

        assertEquals(
                "should have JSON CONTAINS expected part:",
                message);
    }

    @Test
    void testMessageJsonContainsFromFile() {
        String message =  new JsonContainsFromFile(Path.of("/dir/file"))
                .toString();

        assertEquals(
                "should have JSON CONTAINS expected part:",
                message);
    }

    @Test
    void testMessageJsonEquals() {
        String message =  new JsonEquals("logs/test").toString();

        assertEquals(
                "should have JSON EQUAL to:",
                message);
    }

    @Test
    void testMessageJsonEqualsFromFile() {
        String message =  new JsonEqualsFromFile(Path.of("/dir/file"))
                .toString();

        assertEquals(
                "should have JSON EQUAL to:",
                message);
    }

    @Test
    void testMessageJsonSchemaCheck() {
        String message =  new JsonSchemaCheck("logs/test").toString();

        assertEquals(
                "should have JSON MATCH SCHEMA",
                message);
    }

    @Test
    void testMessageJsonSchemaCheckFile() {
        String message =  new JsonSchemaCheckFile(Path.of("/dir/file"))
                .toString();
        assertEquals(
                "should have JSON MATCH SCHEMA",
                message);
    }

    @Test
    void testMessageStringContains() {
        String message =  new StringContains("test").toString();

        assertEquals(
                "should have STRING CONTAINS: [test]",
                message);
    }

    @Test
    void testMessageStringEquals() {
        String message =  new StringEquals("logs_test").toString();

        assertEquals(
                "should have STRING EQUAL: [logs_test]",
                message);
    }

    @Test
    void testCustomMatcher() {
        String message =  new CustomMatcher((stringContainsInOrder("te", "st"))).toString();

        assertEquals(
                "should have STRING match to [a string containing \"te\", \"st\" in order]",
                message);
    }


}

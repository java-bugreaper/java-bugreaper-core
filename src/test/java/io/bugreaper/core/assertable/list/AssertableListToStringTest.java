package io.bugreaper.core.assertable.list;


import io.bugreaper.core.assertable.stringlist.asserters.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


//this tests for Allure Step description substring
class AssertableListToStringTest {


    @Test
    void testMessageElementsCount() {
        String message =  new ElementsCount(2).toString();

        assertAll(() -> assertEquals(
                "list should have count: 2",
                message));
    }


    @Test
    void testMessageIsJsonType() {
        String message =  new IsJsonType().toString();

        assertAll(() -> assertEquals(
                "list should have JSON element",
                message));
    }

    @Test
    void testMessageJsonContains() {
        String message =  new JsonContains("test").toString();

        assertAll(() -> assertEquals(
                "list should have JSON contains",
                message));
    }

    @Test
    void testMessageJsonContainsFromFile() {
        String message =  new JsonContainsFromFile(Path.of("/dir/file"))
                .toString();

        assertAll(() -> assertEquals(
                "list should have JSON contains",
                message));
    }

    @Test
    void testMessageJsonEquals() {
        String message =  new JsonEquals("test").toString();

        assertAll(() -> assertEquals(
                "list should have JSON equals",
                message));
    }

    @Test
    void testMessageJsonEqualsFromFile() {
        String message =  new JsonEqualsFromFile(Path.of("/dir/file"))
                .toString();

        assertAll(() -> assertEquals(
                "list should have JSON equals",
                message));
    }

    @Test
    void testMessageJsonSchemaCheck() {
        String message =  new JsonSchemaCheck("test").toString();

        assertAll(() -> assertEquals(
                "list should have JSON with schema",
                message));
    }

    @Test
    void testMessageJsonSchemaCheckFile() {
        String message =  new JsonSchemaCheckFile(Path.of("/dir/file"))
                .toString();

        assertAll(() -> assertEquals(
                "list should have JSON with schema",
                message));
    }

    @Test
    void testMessageStringContains() {
        String message =  new StringContains("test").toString();

        assertAll(() -> assertEquals(
                "list should have string CONTAINS [test]",
                message));
    }

    @Test
    void testMessageStringEquals() {
        String message =  new StringEquals("test").toString();

        assertAll(() -> assertEquals(
                "list should have string EQUALS [test]",
                message));
    }

}

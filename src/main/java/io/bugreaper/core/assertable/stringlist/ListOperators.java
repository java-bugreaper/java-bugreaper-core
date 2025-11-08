package io.bugreaper.core.assertable.stringlist;

import io.bugreaper.core.assertable.stringlist.asserters.*;
import io.bugreaper.core.assertable.stringlist.extractors.LastElement;
import org.hamcrest.Matcher;

import java.nio.file.Path;

public class ListOperators {

    private ListOperators() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Assert that at least one element in list equal to string
     *
     * @param expectedString expected string
     * @throws AssertionError on assert fail
     */
    public static StringEquals stringEqual(String expectedString) {
        return new StringEquals(expectedString);
    }

    public static StringContains stringContains(String expectedString) {
        return new StringContains(expectedString);
    }

    public static JsonEquals jsonEqual(String expectedJson) {
        return new JsonEquals(expectedJson);
    }

    public static JsonEqualsFromFile jsonEqual(Path path) {
        return new JsonEqualsFromFile(path);
    }

    public static JsonContains jsonContains(String expectedJsonPart) {
        return new JsonContains(expectedJsonPart);
    }

    public static JsonContainsFromFile jsonContains(Path path) {
        return new JsonContainsFromFile(path);
    }

    /**
     * Assert that at least one element in list has expected Schema
     *
     * @param expectedSchema expected Json Schema
     * @throws AssertionError on assert fail
     */
    public static JsonSchemaCheck jsonMatchesSchema(String expectedSchema) {
        return new JsonSchemaCheck(expectedSchema);
    }

    /**
     * Assert that at least one element in list has expected Schema
     *
     * @param path path to files with Schema (in resources)
     * @throws AssertionError on assert fail
     */
    public static JsonSchemaCheckFile jsonMatchesSchema(Path path) {
        return new JsonSchemaCheckFile(path);
    }

    /**
     * Assert that at least one element in list is JSON type(or arrayJson)
     *
     * @throws AssertionError on assert fail
     */
    public static IsJsonType isJsonType() {
        return new IsJsonType();
    }

    /**
     * Assert elements by custom matchers in AssertableStringList
     *
     * @param matcher for String <a href="https://hamcrest.org/JavaHamcrest/javadoc/3.0/org/hamcrest/Matchers.html">Matcher</a>
     * @return AssertableStringList
     * @throws AssertionError on assert fail
     */
    public static CustomMatcher stringMatchesCustom(Matcher<String> matcher) {
        return new CustomMatcher(matcher);
    }

    /**
     * Assert count of elements in AssertableStringList
     *
     * @param cnt expected count
     * @return AssertableStringList
     * @throws AssertionError on assert fail
     */
    public static ElementsCount hasExactCount(int cnt) {
        return new ElementsCount(cnt);
    }

    /**
     * Grab last element from AssertableStringList
     *
     * @return String with last element
     */
    public static LastElement grabLastElement() {
        return new LastElement();
    }

}
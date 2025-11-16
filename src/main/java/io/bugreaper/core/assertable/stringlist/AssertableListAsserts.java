package io.bugreaper.core.assertable.stringlist;

import io.bugreaper.core.assertable.AssertableStringList;
import org.hamcrest.Matcher;

import java.nio.file.Path;

public interface AssertableListAsserts {

    /**
     * Assert that at least one element in list contains string
     *
     * @param expectedSubString expected string part
     * @return this
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListAnyContains(String expectedSubString);

    /**
     * Assert that at least one element in list equal to string
     *
     * @param expectedString expected full string
     * @return this
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListAnyEquals(String expectedString);

    /**
     * Assert elements by custom matchers in AssertableStringList
     *
     * @param matcher for String <a href="https://hamcrest.org/JavaHamcrest/javadoc/3.0/org/hamcrest/Matchers.html">Matcher</a>
     * @return this
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListAnyMatcher(Matcher<String> matcher);

    /**
     * Assert that at least one element containsJson without strict array ordering
     * <p> extensible fields will be skipped
     *
     * @param expectedJsonPart expected part of JSON (arrays can be not ordered)
     * @return this
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyContainsJson(String expectedJsonPart);

    /**
     * Assert that at least one element contains Json without strict array ordering
     * <p> extensible fields will be skipped
     *
     * @param filePath path to file in resources with expected part of JSON  (arrays can be not ordered)
     * @return this
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyContainsJson(Path filePath);

    /**
     * Assert that at least one element equal to Json with strict array ordering
     * <p> extensible fields not expected
     *
     * @param expectedJson expected full JSON with strict ordered arrays
     * @return this
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyEqualsJson(String expectedJson);

    /**
     * Assert that at least one element equal to Json with strict array ordering
     * <p> extensible fields not expected
     *
     * @param filePath path to file in resources with expected full JSON with strict ordered arrays
     * @return this
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyEqualsJson(Path filePath);

    /**
     * Assert that at least one element in list has expected Schema
     *
     * @param expectedSchema expected Json Schema
     * @return this
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyJsonMatchSchema(String expectedSchema);

    /**
     * Assert that at least one element in list has expected Schema
     *
     * @param filePath path to files with Schema (in resources)
     * @return this
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyJsonMatchSchema(Path filePath);

    /**
     * Assert that at least one element in list is JSON type(or JsonArray)
     *
     * @return this
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListAnyJsonType();

    /**
     * Assert count of elements in AssertableStringList
     *
     * @param cnt expected count
     * @return this
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListHasExactlyCount(int cnt);

}

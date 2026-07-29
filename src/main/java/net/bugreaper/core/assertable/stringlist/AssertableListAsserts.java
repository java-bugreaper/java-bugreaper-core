package net.bugreaper.core.assertable.stringlist;

import net.bugreaper.core.assertable.AssertableStringList;
import net.bugreaper.core.assertions.JsonAsserts;
import org.hamcrest.Matcher;

import java.nio.file.Path;

public interface AssertableListAsserts {

    /**
     * Asserts that at least one element in the list contains string the specified string
     *
     * @param expectedSubString expected string part
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListAnyContains(String expectedSubString);

    /**
     * Asserts that at least one element in the list is equal to the specified string
     *
     * @param expectedString expected full string
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListAnyEquals(String expectedString);

    /**
     * Asserts list elements using custom matchers
     *
     * @param matcher for String <a href="https://hamcrest.org/JavaHamcrest/javadoc/3.0/org/hamcrest/Matchers.html">Matcher</a>
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListAnyMatcher(Matcher<String> matcher);

    /**
     * Asserts that at least one element in the list contains JSON without strict array ordering
     * <p> extensible fields and <b>elements in array</b> will be skipped
     *
     * @param expectedJsonPart expected part of JSON (arrays can be not ordered and have different count)
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyContainsJsonSubset(String expectedJsonPart);

    /**
     * Asserts that at least one element in the list contains JSON and/or optional checks
     * <p>Same behavior as {@link JsonAsserts#assertJsonsExtended(String, String)}.
     *
     * @param expectedJsonExtended expected part of JSON and/or optional checks
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyContainsExtendedJson(String expectedJsonExtended);

    /**
     * Asserts that at least one element in the list contains JSON without strict array ordering
     * <p> extensible fields will be skipped. <b>But extensible elements in array cause AssertionError</b>
     *
     * @param expectedJsonPart expected part of JSON (arrays can be not ordered but must have same count of elements)
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyContainsJson(String expectedJsonPart);

    /**
     * Asserts that at least one element in the list contains JSON without strict array ordering
     * <p> extensible fields will be skipped, <b>But extensible elements in array cause AssertionError</b>
     *
     * @param filePath path to file in resources with expected part of JSON  (arrays can be not ordered)
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyContainsJson(Path filePath);

    /**
     * Asserts that at least one element in the list is equal to JSON with strict array ordering
     * <p> extensible fields and extensible elements in array not expected
     *
     * @param expectedJson expected full JSON with strict ordered arrays
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyEqualsJson(String expectedJson);

    /**
     * Asserts that at least one element in the list is equal to JSON with strict array ordering
     * <p> extensible fields not expected
     *
     * @param filePath path to file in resources with expected full JSON with strict ordered arrays
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyEqualsJson(Path filePath);

    /**
     * Asserts that at least one element in the list matches the expected Schema
     *
     * @param expectedSchema expected JSON Schema
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyJsonMatchSchema(String expectedSchema);

    /**
     * Asserts that at least one element in the list matches the expected Schema
     *
     * @param filePath path to files with Schema (in resources)
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     * @throws IllegalArgumentException on not Json data provided
     */
    AssertableStringList seeListAnyJsonMatchSchema(Path filePath);

    /**
     * Asserts that at least one element in the list is a JSON object or JSON array
     *
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListAnyJsonType();

    /**
     * Asserts that the number of elements in the list is equal to the expected count
     *
     * @param cnt expected count
     * @return this instance for method chaining
     * @throws AssertionError on assert fail
     */
    AssertableStringList seeListHasExactlyCount(int cnt);

    /**
     * Asserts that the number of elements in the list is greater than minSize
     *
     * @param minSize    minimum size (greater will cause fail)
     * @return this instance for method chaining
     * @throws AssertionError       on assert fail
     */
    AssertableStringList seeListSizeIsGreaterThan(int minSize);

    /**
     * Asserts that the number of elements in the list is less than maxSize
     *
     * @param maxSize    maximum size (less will cause fail)
     * @return this instance for method chaining
     * @throws AssertionError       on assert fail
     */
    AssertableStringList seeListSizeIsLessThan(int maxSize);
    
}

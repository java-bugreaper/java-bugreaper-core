package net.bugreaper.core.assertable.stringlist;

import net.bugreaper.core.assertable.AssertableStringList;
import net.bugreaper.core.assertions.JsonAsserts;
import net.bugreaper.core.exceptions.FileReaderException;
import org.hamcrest.Matcher;

import java.nio.file.Path;

public interface AssertableListAsserts {

    /**
     * Asserts that at least one element in the list contains the specified string.
     *
     * @param expectedSubString expected substring
     * @return this instance for method chaining
     * @throws AssertionError if the assertion fails
     */
    AssertableStringList seeListAnyContains(String expectedSubString);

    /**
     * Asserts that the list contains at least one element equal to the specified string.
     *
     * @param expectedString expected full string
     * @return this instance for method chaining
     * @throws AssertionError if the assertion fails
     */
    AssertableStringList seeListAnyEquals(String expectedString);

    /**
     * Asserts list elements using custom matchers
     *
     * @param matcher <a href="https://hamcrest.org/JavaHamcrest/javadoc/3.0/org/hamcrest/Matchers.html">Hamcrest matcher for String values </a>
     * @return this instance for method chaining
     * @throws AssertionError if the assertion fails
     */
    AssertableStringList seeListAnyMatcher(Matcher<String> matcher);

    /**
     * Asserts that at least one element in the list contains JSON subset
     * without strict array ordering
     *
     * <p>Extra object fields and additional array elements are ignored.</p>
     *
     * @param expectedJsonPart expected JSON subset (array order is ignored and array size may differ)
     * @return this instance for method chaining
     * @throws AssertionError           if the assertion fails
     * @throws IllegalArgumentException if the provided data is not valid JSON
     */
    AssertableStringList seeListAnyContainsJsonSubset(String expectedJsonPart);

    /**
     * Asserts that at least one element in the list matches the expected JSON using contains, AND/OR,
     * and optional checks.
     *
     * <p>Same behavior as {@link JsonAsserts#assertJsonsExtended(String, String)}.
     *
     * @param expectedJsonExtended expected JSON with contains, AND/OR, and optional checks
     * @return this instance for method chaining
     * @throws AssertionError           if the assertion fails
     * @throws IllegalArgumentException if the provided data is not valid JSON
     */
    AssertableStringList seeListAnyContainsExtendedJson(String expectedJsonExtended);

    /**
     * Asserts that at least one element in the list contains the specified JSON without strict array ordering.
     *
     * <p>Extra object fields are ignored, but additional array elements cause an {@link AssertionError}.</p>
     *
     * @param expectedJsonPart expected JSON subset (array order is ignored, but the number of elements must match)
     * @return this instance for method chaining
     * @throws AssertionError           if the assertion fails
     * @throws IllegalArgumentException if the provided data is not valid JSON
     */
    AssertableStringList seeListAnyContainsJson(String expectedJsonPart);

    /**
     * Asserts that at least one element in the list contains the specified JSON without strict array ordering.
     *
     * <p>Extra object fields are ignored, but additional array elements cause an {@link AssertionError}.</p>
     *
     * @param filePath path to file in resources with expected JSON subset (array order is ignored, but the number of elements must match)
     * @return this instance for method chaining
     * @throws AssertionError      if the assertion fails
     * @throws FileReaderException if reading the file fails or the provided data is not valid JSON
     */
    AssertableStringList seeListAnyContainsJson(Path filePath);

    /**
     * Asserts that at least one element in the list is equal to the specified JSON with strict array ordering.
     *
     * <p>Extra object fields and additional array elements are not allowed.</p>
     *
     * @param expectedJson expected full JSON with strict array ordering
     * @return this instance for method chaining
     * @throws AssertionError           if the assertion fails
     * @throws IllegalArgumentException if the provided data is not valid JSON
     */
    AssertableStringList seeListAnyEqualsJson(String expectedJson);

    /**
     * Asserts that at least one element in the list is equal to the specified JSON with strict array ordering.
     *
     * <p>Extra object fields and additional array elements are not allowed.</p>
     *
     * @param filePath path to the resource file containing the expected full JSON with strict array ordering
     * @return this instance for method chaining
     * @throws AssertionError      if the assertion fails
     * @throws FileReaderException if reading the file fails or the provided data is not valid JSON
     */
    AssertableStringList seeListAnyEqualsJson(Path filePath);

    /**
     * Asserts that at least one element in the list matches the JSON schema.
     *
     * @param expectedSchema expected JSON Schema
     * @return this instance for method chaining
     * @throws AssertionError           if the assertion fails
     * @throws IllegalArgumentException if the provided data is not valid JSON
     */
    AssertableStringList seeListAnyJsonMatchSchema(String expectedSchema);

    /**
     * Asserts that at least one element in the list matches the JSON schema.
     *
     * @param filePath path to the resource file containing the JSON schema
     * @return this instance for method chaining
     * @throws AssertionError      if the assertion fails
     * @throws FileReaderException if reading the file fails or the provided data is not valid JSON
     */
    AssertableStringList seeListAnyJsonMatchSchema(Path filePath);

    /**
     * Asserts that at least one element in the list is a JSON object or JSON array
     *
     * @return this instance for method chaining
     * @throws AssertionError if the assertion fails
     */
    AssertableStringList seeListAnyJsonType();

    /**
     * Asserts that the number of elements in the list is equal to the expected count
     *
     * @param cnt expected count
     * @return this instance for method chaining
     * @throws AssertionError if the assertion fails
     */
    AssertableStringList seeListHasExactlyCount(int cnt);

    /**
     * Asserts that the number of elements in the list is greater than minSize
     *
     * @param minSize minimum size (greater will cause fail)
     * @return this instance for method chaining
     * @throws AssertionError if the assertion fails
     */
    AssertableStringList seeListSizeIsGreaterThan(int minSize);

    /**
     * Asserts that the number of elements in the list is less than maxSize
     *
     * @param maxSize maximum size (less will cause fail)
     * @return this instance for method chaining
     * @throws AssertionError if the assertion fails
     */
    AssertableStringList seeListSizeIsLessThan(int maxSize);

}

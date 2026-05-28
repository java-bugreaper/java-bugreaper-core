package net.bugreaper.core.assertions;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static net.bugreaper.core.assertions.ListAsserts.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SuppressWarnings("java:S5976")
class ListAssertionsCatchTests {

    @Test
    void testStringListCountException() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertCountElementsInList(3, actualList));

        MatcherAssert.assertThat(
                "Exception on failed assert count in list",
                exception.getMessage(),
                StringContains.containsString("Count of elements in list not equal: 3"));
    }

    @Test
    void testStringListGreaterException() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertListSizeGreaterThan(1, actualList));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("List size expected to be greater <1> bytes but got <1>"));
    }

    @Test
    void testStringListLessException() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy");
        actualList.add("dummy");
        actualList.add("dummy");

        Throwable exception = assertThrows(AssertionError.class, () ->
                assertListSizeLessThan(3, actualList));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("List size expected to be less <3> bytes but got <3>"));
    }

    @Test
    void testStringCustomMatcherFailed() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy1");

        Matcher<String> matcher = startsWithIgnoringCase("TEST");

        Throwable exception = assertThrows(AssertionError.class, () ->
                customStringMatcherInList(matcher, actualList));

        MatcherAssert.assertThat(
                "Exception on failed custom matcherString in list assertion main message",
                exception.getMessage(),
                StringContains.containsString("""
                        There is no elements in the list match to:
                        a string starting with "TEST" ignoring case"""));

        MatcherAssert.assertThat(
                "Exception on failed equal String in list assertion (first element)",
                exception.getMessage(),
                StringContains.containsString("""
                        Expected: a string starting with "TEST" ignoring case
                             but: was "dummy1\""""));
    }

    @Test
    void testStringEqualInList2Failed() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy1");
        actualList.add("dummy2");

        Throwable exception = assertThrows(AssertionError.class, () ->
                equalsStringInList("test", actualList));

        MatcherAssert.assertThat(
                "Exception on failed equal String in list assertion main message",
                exception.getMessage(),
                StringContains.containsString("""
                        There is no elements in the list equals to string:
                        test"""));

        MatcherAssert.assertThat(
                "Exception on failed equal String in list assertion (first element)",
                exception.getMessage(),
                StringContains.containsString("""
                        Expected: is "test"
                             but: was "dummy1\""""));

        MatcherAssert.assertThat(
                "Exception on failed equal String in list assertion (second element)",
                exception.getMessage(),
                StringContains.containsString("""
                        Expected: is "test"
                             but: was "dummy2\""""));
    }

    @Test
    void testStringNotEqualInListFailed() {

        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy1");
        actualList.add("test2");
        actualList.add("test");
        actualList.add("test3");

        Throwable exception = assertThrows(AssertionError.class, () ->
                notEqualsStringInList("test", actualList));

        MatcherAssert.assertThat(
                "Exception on failed equal String in list assertion main message",
                exception.getMessage(),
                StringContains.containsString("""
                        There is not expected elements in the list:
                        not "test\""""));
    }

    @Test
    void testStringEqualWithEmptyList() {
        ArrayList<String> actualList = new ArrayList<>();

        Throwable exception = assertThrows(AssertionError.class, () ->
                equalsStringInList("test", actualList));

        MatcherAssert.assertThat(
                "Exception on failed equal String in list assertion main message",
                exception.getMessage(),
                StringContains.containsString("""
                        There is no elements in the list equals to string:
                        test"""));
    }

    @Test
    void testStringContainsInList2Failed() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("dummy1");
        actualList.add("dummy2");

        Throwable exception = assertThrows(AssertionError.class, () ->
                containsStringInList("test", actualList));

        String exceptionText = exception.getMessage();

        MatcherAssert.assertThat(
                "Exception on failed contains String in list assertion main message",
                exceptionText,
                StringContains.containsString("""
                        There is no elements in the list contain substring:
                        test"""));

        MatcherAssert.assertThat(
                "Exception on failed contains String in list assertion (second element)",
                exceptionText,
                StringContains.containsString("""
                        Expected: a string containing "test"
                             but: was "dummy1\""""));
        MatcherAssert.assertThat(
                "Exception on failed contains String in list assertion (second element)",
                exceptionText,
                StringContains.containsString("""
                        Expected: a string containing "test"
                             but: was "dummy2\""""));
    }

    @Test
    void testJsonEqualsInList3FailedWithBroken() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("broken_message");
        actualList.add("""
                {"id": 2}""");
        actualList.add("""
                {"id": 3, text: "test"}""");

        Throwable exception = assertThrows(AssertionError.class, () ->
                equalsJsonInList("""
                        {"id": 3}""", actualList));

        MatcherAssert.assertThat(
                "Exception on failed contains String in list assertion main message",
                exception.getMessage(),
                StringContains.containsString("""
                        There is no elements in the list equal to JSON:
                        {"id": 3}"""));

        MatcherAssert.assertThat(
                "Exception on failed equal JSON in list assertion (first element broken)",
                exception.getMessage(),
                StringContains.containsString("Unparsable JSON string: broken_message"));

        MatcherAssert.assertThat(
                "Exception on failed equal JSON  in list assertion (second element broken)",
                exception.getMessage(),
                StringContains.containsString("""
                        id
                        Expected: 3
                             got: 2"""));

        MatcherAssert.assertThat(
                "Exception on failed equal JSON in list assertion (third elements additional key)",
                exception.getMessage(),
                StringContains.containsString("Unexpected: text"));
    }

    @Test
    void testJsonContainsInList2FailedWithBroken() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("broken_message");
        actualList.add("""
                {"id": 2}""");

        Throwable exception = assertThrows(AssertionError.class, () ->
                containsJsonInList("""
                        {"id": 3}""", actualList));

        MatcherAssert.assertThat(
                "Exception on failed contains String in list assertion main message",
                exception.getMessage(),
                StringContains.containsString("""
                        There is no elements in the list contains JSON:
                        {"id": 3}"""));

        MatcherAssert.assertThat(
                "Exception on failed contains JSON  in list assertion (first elements broken)",
                exception.getMessage(),
                StringContains.containsString("Unparsable JSON string: broken_message"));

        MatcherAssert.assertThat(
                "Exception on failed contains JSON in list assertion main message",
                exception.getMessage(),
                StringContains.containsString("""
                        id
                        Expected: 3
                             got: 2"""));
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testJsonContainsInList() {
        ArrayList<String> actualList = new ArrayList<>();

        actualList.add("""
                {"id": 2}""");
        actualList.add("""
                {"id": 2}""");
        actualList.add("""
                {"id": 1}""");

        containsJsonInList("""
                {"id": 2}""", actualList);
    }

}

package io.bugreaper.core.assertable.stringlist.asserters;

import java.util.List;

import static io.bugreaper.core.assertions.ListAsserts.equalsStringInList;

public class StringEquals implements ListCondition {

    private final String expectedString;

    public StringEquals(String expectedString) {
        this.expectedString = expectedString;
    }

    @Override
    public void test(List<String> arrayList) {
        equalsStringInList(expectedString, arrayList);
    }

    @Override
    public String toString() {
        return "should have STRING EQUAL: [" + expectedString + "]";
    }
}
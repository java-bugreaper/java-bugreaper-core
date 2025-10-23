package io.bugreaper.core.assertable.stringlist.asserters;

import java.util.List;

import static io.bugreaper.core.assertions.ListAsserts.containsStringInList;

public class StringContains implements ListCondition {

    private final String expectedString;

    public StringContains(String expectedString) {
        this.expectedString = expectedString;
    }

    @Override
    public void test(List<String> arrayList) {
        containsStringInList(expectedString, arrayList);
    }

    @Override
    public String toString() {
        return "list should have string CONTAINS [" + expectedString + "]";
    }
}
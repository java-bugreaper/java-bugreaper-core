package io.bugreaper.core.assertable.stringlist.asserters;

import org.hamcrest.Matcher;

import java.util.List;

import static io.bugreaper.core.assertions.ListAsserts.customStringMatcherInList;

public class CustomMatcher implements ListCondition {

    private final Matcher<String> matcher;

    public CustomMatcher(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void test(List<String> arrayList) {
        customStringMatcherInList(matcher, arrayList);
    }

    @Override
    public String toString() {
        return "should have STRING match to [" + matcher + "]";
    }
}
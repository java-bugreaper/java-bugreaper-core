package io.bugreaper.core.assertable.stringlist.asserters;

import java.util.List;

import static io.bugreaper.core.assertions.ListAsserts.assertCountElementsInList;


public class ElementsCount implements ListCondition {

    private final int cnt;

    public ElementsCount(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public void test(List<String> arrayList){

        assertCountElementsInList(cnt, arrayList);
    }

    @Override
    public String toString() {
        return "should have count EQUAL to: " + cnt;
    }

}
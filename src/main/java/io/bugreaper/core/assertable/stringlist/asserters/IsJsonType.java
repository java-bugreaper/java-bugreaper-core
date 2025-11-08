package io.bugreaper.core.assertable.stringlist.asserters;

import java.util.List;

import static io.bugreaper.core.assertions.ListAsserts.isJsonTypeInList;

public class IsJsonType implements ListCondition {

    @Override
    public void test(List<String> arrayList){

        isJsonTypeInList(arrayList);
    }

    @Override
    public String toString() {
        return "should have value of JSON type";
    }

}
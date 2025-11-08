package io.bugreaper.core.assertable;

import io.bugreaper.core.assertable.stringlist.asserters.ListCondition;
import io.qameta.allure.Step;
import io.bugreaper.core.assertable.stringlist.extractors.ExtractType;

import java.util.List;

public class AssertableStringList {

    private final List<String> arrayList;

    public AssertableStringList(List<String> list) {
        this.arrayList = list;
    }

    // in modules maybe reuse with override message
    @Step("(Assert): {listCondition}")
    public AssertableStringList verifyInList(ListCondition listCondition) {
        listCondition.test(arrayList);
        return this;
    }

    @Step("extract: {extractType}")
    public String extractFromList(ExtractType extractType) {
        return  extractType.extract(arrayList);
    }


}

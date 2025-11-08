package io.bugreaper.core.assertable.stringlist.asserters;

import io.qameta.allure.Allure;

import java.util.List;

import static io.bugreaper.core.assertions.ListAsserts.containsJsonInList;

public class JsonContains implements ListCondition {

    private final String expectedJsonPart;

    public JsonContains(String expectedJsonPart) {
        this.expectedJsonPart = expectedJsonPart;
    }

    @Override
    public void test(List<String> arrayList){

        Allure.addAttachment("expected json part", "application/json", expectedJsonPart);

        containsJsonInList(expectedJsonPart, arrayList);
    }

    @Override
    public String toString() {
        return "should have JSON CONTAINS expected part:";
    }

}
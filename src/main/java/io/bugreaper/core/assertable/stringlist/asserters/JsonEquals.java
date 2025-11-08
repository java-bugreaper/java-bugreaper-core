package io.bugreaper.core.assertable.stringlist.asserters;

import io.qameta.allure.Allure;

import java.util.List;

import static io.bugreaper.core.assertions.ListAsserts.*;

public class JsonEquals implements ListCondition {

    private final String expectedJson;

    public JsonEquals(String expectedJson) {
        this.expectedJson = expectedJson;
    }

    @Override
    public void test(List<String> arrayList){

        Allure.addAttachment("expected json", "application/json", expectedJson);

        equalsJsonInList(expectedJson, arrayList);
    }

    @Override
    public String toString() {
        return "should have JSON EQUAL to:";
    }

}
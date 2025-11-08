package io.bugreaper.core.assertable.stringlist.asserters;

import io.qameta.allure.Allure;

import java.util.List;

import static io.bugreaper.core.assertions.ListAsserts.jsonSchemaCheckInList;

public class JsonSchemaCheck implements ListCondition {

    private final String expectedSchema;

    public JsonSchemaCheck(String expectedSchema) {
        this.expectedSchema = expectedSchema;
    }

    @Override
    public void test(List<String> arrayList){

        Allure.addAttachment("expected json Schema", "application/json", expectedSchema);

        jsonSchemaCheckInList(expectedSchema, arrayList);
    }

    @Override
    public String toString() {
        return "should have JSON MATCH SCHEMA";
    }

}
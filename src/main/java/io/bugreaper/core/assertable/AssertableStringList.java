package io.bugreaper.core.assertable;

import io.bugreaper.core.assertable.stringlist.AssertableListAsserts;
import io.bugreaper.core.assertable.stringlist.AssertableListGrab;
import io.qameta.allure.Step;
import org.hamcrest.Matcher;

import java.nio.file.Path;
import java.util.List;

import static io.bugreaper.core.allurereporter.AllureReporter.*;
import static io.bugreaper.core.assertions.ListAsserts.*;
import static io.bugreaper.core.filereaders.FileReader.readJsonFromFile;

public class AssertableStringList implements AssertableListAsserts, AssertableListGrab {

    private final List<String> arrayList;

    public AssertableStringList(List<String> list) {
        this.arrayList = list;
    }

    @Override
    @Step("(Assert) List should have STRING CONTAINS: <{expectedSubString}>")
    public AssertableStringList seeListAnyContains(String expectedSubString) {

        containsStringInList(expectedSubString, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have STRING EQUAL: <{expectedString}>")
    public AssertableStringList seeListAnyEquals(String expectedString) {

        equalsStringInList(expectedString, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have STRING match to <{matcher}>")
    public AssertableStringList seeListAnyMatcher(Matcher<String> matcher) {

        customStringMatcherInList(matcher, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List have JSON CONTAINS part:")
    public AssertableStringList seeListAnyContainsJson(String expectedJsonPart) {

        attachJson("expected json part",  expectedJsonPart);
        containsJsonInList(expectedJsonPart, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have JSON EQUAL to:")
    public AssertableStringList seeListAnyEqualsJson(String expectedJson) {

        attachJson("expected json", expectedJson);
        equalsJsonInList(expectedJson, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List have JSON CONTAINS part:")
    public AssertableStringList seeListAnyContainsJson(Path filePath) {
        String path = String.valueOf(filePath);

        attachFromFileNoStep(path, String.valueOf(path));
        containsJsonInList(readJsonFromFile(path), arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have JSON EQUAL to:")
    public AssertableStringList seeListAnyEqualsJson(Path filePath) {
        String path = String.valueOf(filePath);

        attachFromFileNoStep(path, String.valueOf(path));
        equalsJsonInList(readJsonFromFile(path), arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have JSON MATCH SCHEMA")
    public AssertableStringList seeListAnyJsonMatchSchema(String expectedSchema) {

        attachJson("expected json", expectedSchema);
        jsonSchemaCheckInList(expectedSchema, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have JSON MATCH SCHEMA")
    public AssertableStringList seeListAnyJsonMatchSchema(Path filePath) {
        String path = String.valueOf(filePath);

        attachFromFileNoStep(path, String.valueOf(path));
        jsonSchemaCheckInList(readJsonFromFile(path), arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have count EQUAL to: <{cnt}>")
    public AssertableStringList seeListHasExactlyCount(int cnt) {
        assertCountElementsInList(cnt, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have element with JSON type")
    public AssertableStringList seeListAnyJsonType() {

        isJsonTypeInList(arrayList);
        return this;
    }


    @Override
    @Step("(Grab) Last element from list")
    public String grabLastElement() {

        if (arrayList.isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }

        String result = arrayList.get(arrayList.size() - 1);

        attachCanBeNull("Last element:", result);

        return result;
    }

}

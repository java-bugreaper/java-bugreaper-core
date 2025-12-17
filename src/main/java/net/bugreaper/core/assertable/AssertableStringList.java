package net.bugreaper.core.assertable;

import net.bugreaper.core.assertable.stringlist.AssertableListAsserts;
import net.bugreaper.core.assertable.stringlist.AssertableListGrab;
import io.qameta.allure.Step;
import net.bugreaper.core.allurereporter.AllureReporter;
import net.bugreaper.core.assertions.ListAsserts;
import net.bugreaper.core.filereaders.FileReader;
import org.hamcrest.Matcher;

import java.nio.file.Path;
import java.util.List;

public class AssertableStringList implements AssertableListAsserts, AssertableListGrab {

    private final List<String> arrayList;

    public AssertableStringList(List<String> list) {
        this.arrayList = list;
    }

    @Override
    @Step("(Assert) List should have STRING CONTAINS: <{expectedSubString}>")
    public AssertableStringList seeListAnyContains(String expectedSubString) {

        ListAsserts.containsStringInList(expectedSubString, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have STRING EQUAL to: <{expectedString}>")
    public AssertableStringList seeListAnyEquals(String expectedString) {

        ListAsserts.equalsStringInList(expectedString, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have STRING match to <{matcher}>")
    public AssertableStringList seeListAnyMatcher(Matcher<String> matcher) {

        ListAsserts.customStringMatcherInList(matcher, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List have JSON CONTAINS part:")
    public AssertableStringList seeListAnyContainsJson(String expectedJsonPart) {

        AllureReporter.attachJson("expected json part",  expectedJsonPart);
        ListAsserts.containsJsonInList(expectedJsonPart, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have JSON EQUAL to:")
    public AssertableStringList seeListAnyEqualsJson(String expectedJson) {

        AllureReporter.attachJson("expected json", expectedJson);
        ListAsserts.equalsJsonInList(expectedJson, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List have JSON CONTAINS part:")
    public AssertableStringList seeListAnyContainsJson(Path filePath) {
        String path = String.valueOf(filePath);

        AllureReporter.attachFromFileNoStep(path, String.valueOf(path));
        ListAsserts.containsJsonInList(FileReader.readJsonFromFile(path), arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have JSON EQUAL to:")
    public AssertableStringList seeListAnyEqualsJson(Path filePath) {
        String path = String.valueOf(filePath);

        AllureReporter.attachFromFileNoStep(path, String.valueOf(path));
        ListAsserts.equalsJsonInList(FileReader.readJsonFromFile(path), arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have JSON MATCH SCHEMA")
    public AssertableStringList seeListAnyJsonMatchSchema(String expectedSchema) {

        AllureReporter.attachJson("expected json", expectedSchema);
        ListAsserts.jsonSchemaCheckInList(expectedSchema, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have JSON MATCH SCHEMA")
    public AssertableStringList seeListAnyJsonMatchSchema(Path filePath) {
        String path = String.valueOf(filePath);

        AllureReporter.attachFromFileNoStep(path, String.valueOf(path));
        ListAsserts.jsonSchemaCheckInList(FileReader.readJsonFromFile(path), arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have count EQUAL to: <{cnt}>")
    public AssertableStringList seeListHasExactlyCount(int cnt) {
        ListAsserts.assertCountElementsInList(cnt, arrayList);

        return this;
    }

    @Override
    @Step("(Assert) List should have element with JSON type")
    public AssertableStringList seeListAnyJsonType() {

        ListAsserts.isJsonTypeInList(arrayList);
        return this;
    }


    @Override
    @Step("(Grab) Last element from list")
    public String grabLastElement() {

        if (arrayList.isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }

        String result = arrayList.get(arrayList.size() - 1);

        AllureReporter.attachCanBeNull("Last element:", result);

        return result;
    }

}

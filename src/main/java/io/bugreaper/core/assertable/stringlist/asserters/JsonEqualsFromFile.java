package io.bugreaper.core.assertable.stringlist.asserters;

import java.nio.file.Path;
import java.util.List;

import static io.bugreaper.core.allurereporter.AllureReporter.attachFromFileNoStep;
import static io.bugreaper.core.assertions.ListAsserts.equalsJsonInList;
import static io.bugreaper.core.filereaders.FileReader.readJsonFromFile;

public class JsonEqualsFromFile implements ListCondition {

    private final String path;

    public JsonEqualsFromFile(Path path) {
        this.path = String.valueOf(path);
    }

    @Override
    public void test(List<String> arrayList){

        attachFromFileNoStep(path, String.valueOf(path));

        equalsJsonInList(readJsonFromFile(path), arrayList);
    }

    @Override
    public String toString() {
        return "list should have JSON equals";
    }

}
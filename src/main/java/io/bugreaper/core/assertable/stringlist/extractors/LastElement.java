package io.bugreaper.core.assertable.stringlist.extractors;


import java.util.List;

import static io.bugreaper.core.allurereporter.AllureReporter.attachCanBeNull;

public class LastElement implements ExtractType {


    @Override
    public String extract(List<String> arrayList) {

        String result = arrayList.get(arrayList.size() - 1);

        attachCanBeNull("Last element:", result);

        return result;
    }

    @Override
    public String toString() {
        return "last element";
    }


}


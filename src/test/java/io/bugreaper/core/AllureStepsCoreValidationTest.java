package io.bugreaper.core;

import org.junit.jupiter.api.Test;

import static io.bugreaper.core.utils.AllureStepsValidator.validateAllSteps;

class AllureStepsCoreValidationTest {

    @Test
    void testStepsFileHelper() {
        validateAllSteps("io.bugreaper.modules.filehelper.FileHelper");
    }

    @Test
    void testStepsLogHelper() {
        validateAllSteps("io.bugreaper.modules.filehelper.LogHelper");
    }

    @Test
    void testStepsAssertableStringList() {
        validateAllSteps("io.bugreaper.core.assertable.AssertableStringList");
    }

    @Test
    void testStepsAllureReporter() {
        validateAllSteps("io.bugreaper.core.allurereporter.AllureReporter");
    }

}

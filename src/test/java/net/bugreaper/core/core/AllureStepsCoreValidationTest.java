package net.bugreaper.core.core;

import org.junit.jupiter.api.Test;

import static net.bugreaper.core.utils.AllureStepsValidator.validateAllSteps;

class AllureStepsCoreValidationTest {

    @Test
    void testStepsFileHelper() {
        validateAllSteps("net.bugreaper.modules.filehelper.FileHelper");
    }

    @Test
    void testStepsLogHelper() {
        validateAllSteps("net.bugreaper.modules.filehelper.LogHelper");
    }

    @Test
    void testStepsAssertableStringList() {
        validateAllSteps("net.bugreaper.core.assertable.AssertableStringList");
    }

    @Test
    void testStepsAllureReporter() {
        validateAllSteps("net.bugreaper.core.allurereporter.AllureReporter");
    }

}

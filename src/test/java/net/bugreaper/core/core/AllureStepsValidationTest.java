package net.bugreaper.core.core;

import net.bugreaper.core.exceptions.AllureValidatorException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static net.bugreaper.core.utils.AllureStepsValidator.validateAllSteps;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AllureStepsValidationTest {


    @Test
    void testStepsAbsentEnvCatch() {

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                validateAllSteps("net.bugreaper.core.core.TestStepNoEnv"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        Method stepNoEnv
                        in class net.bugreaper.core.core.TestStepNoEnv
                        has @Step placeholder {} with no name. All placeholders must have a valid parameter name."""));

    }

    @Test
    void testStepsWrongEnvCatch() {

        Throwable exception = assertThrows(AssertionFailedError.class, () ->
                validateAllSteps("net.bugreaper.core.core.TestStepWrongEnv"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        Method stepWrongEnv
                        in class net.bugreaper.core.core.TestStepWrongEnv
                        has @Step placeholder {"data2"} which does not match any method parameter:[data]"""));

    }

    @Test
    void testStepsNoClassFound() {

        Throwable exception = assertThrows(AllureValidatorException.class, () ->
                validateAllSteps("net.bugreaper.core.AbsentClass"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("Class not found: net.bugreaper.core.AbsentClass"));

    }

}

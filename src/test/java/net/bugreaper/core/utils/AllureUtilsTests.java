package net.bugreaper.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import net.bugreaper.core.exceptions.AllureValidatorException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("java:S2699")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AllureUtilsTests {


    @Test
    void utilityAllureStepsValidatorClass() throws NoSuchMethodException {
        Constructor<AllureStepsValidator> constructor = AllureStepsValidator.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assert (cause instanceof IllegalStateException);
        assert ("Utility class".equals(cause.getMessage()));
    }

    @Test
    void utilityAllureResultLoaderClass() throws NoSuchMethodException {
        Constructor<AllureResultLoader> constructor = AllureResultLoader.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assert (cause instanceof IllegalStateException);
        assert ("Utility class".equals(cause.getMessage()));
    }

    @Test
    void resultNotFoundTest() {

        Throwable exception = assertThrows(AllureValidatorException.class, () ->
                AllureResultLoader.loadByTestName("notExistTest"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("No value present"));
    }


    @Test
    @Order(1)
    void createResultTest() {
        step();
    }

    private void step(){
        Allure.step("Step1");
    }

    @Test
    @Order(2)
    void checkResultTest() {
        JsonNode result = AllureResultLoader.loadByTestName("createResultTest");

        AllureAssert.assertThat(result)
                .hasStep("Step1");

        var test1 =AllureAssert.assertThat(result);
        Throwable exception = assertThrows(AssertionError.class, () ->
                test1.hasStep("Step2"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        Step not found: 'Step2'
                        Available steps:
                        - Step1"""));

        var test2 =AllureAssert.assertThat(result)
                .hasStep("Step1");

        Throwable exception2 = assertThrows(AssertionError.class, () ->
                test2.hasAttachment("test"));

        MatcherAssert.assertThat(
                exception2.getMessage(),
                StringContains.containsString("Attachment not found: test"));
    }

    @Test
    @Order(3)
    void createResultTest2() {
        stepInStep();
    }

    @Step("Global step")
    private void stepInStep(){
        Allure.step("SubStep1");
    }

    @Test
    @Order(4)
    void checkResultTest2() {
        JsonNode result = AllureResultLoader.loadByTestName("createResultTest2");

        AllureAssert.assertThat(result)
                .hasStep("Global step")
                .hasSubStep("SubStep1");

        var test1 = AllureAssert.assertThat(result)
                .hasStep("Global step");

        Throwable exception = assertThrows(AssertionError.class, () ->
                test1.hasSubStep("SubStep2"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        Sub-step not found: 'SubStep2'
                        Available sub-steps:
                        - SubStep1"""));

    }

}

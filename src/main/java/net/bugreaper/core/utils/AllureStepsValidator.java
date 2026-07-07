package net.bugreaper.core.utils;

import net.bugreaper.core.exceptions.AllureValidatorException;
import io.qameta.allure.Step;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("squid:S5960")
public class AllureStepsValidator {

    private AllureStepsValidator() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check that allure @Step() contains same envs as parameters in method
     *
     * @param className String with full class name example: "net.bugreaper.modules.minio.Minio"
     * @throws AllureValidatorException if Class not fount
     * @throws AssertionError     if step parameters not same as in method
     */
    public static void validateAllSteps(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new AllureValidatorException("Class not found: " + e.getMessage());
        }

        Method[] methods = clazz.getDeclaredMethods();

        // Matches all {param} placeholders
        Pattern placeholderPattern = Pattern.compile("\\{(\\w*)\\}");

        for (Method method : methods) {
            Step step = method.getAnnotation(Step.class);
            if (step != null) {
                Matcher matcher = placeholderPattern.matcher(step.value());
                Set<String> placeholders = new HashSet<>();
                while (matcher.find()) {
                    placeholders.add(matcher.group(1));
                }

                methodsCheck(placeholders,method, clazz);

            }
        }
    }

    private static void methodsCheck(Set<String> placeholders, Method method, Class<?> clazz) {

        Set<String> paramNames = new HashSet<>();
        for (var param : method.getParameters()) {
            paramNames.add(param.getName());
        }

        for (String placeholder : placeholders) {
            if (placeholder.isEmpty()) {
                throw new AssertionError(String.format("""
                                        Method %s
                                        in class %s
                                        has @Step placeholder {} with no name. All placeholders must have a valid parameter name.""",
                        method.getName(), clazz.getName()));
            } else if (!paramNames.contains(placeholder)) {

                throw new AssertionError(String.format("""
                                        Method %s
                                        in class %s
                                        has @Step placeholder {"%s"} which does not match any method parameter:%s""",
                        method.getName(), clazz.getName(), placeholder, paramNames));
            }
        }
    }

}

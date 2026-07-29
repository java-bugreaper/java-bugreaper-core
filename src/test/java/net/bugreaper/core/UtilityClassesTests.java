package net.bugreaper.core;

import net.bugreaper.core.allurereporter.AllureBuilder;
import net.bugreaper.core.allurereporter.AllureReporter;
import net.bugreaper.core.assertions.Asserts;
import net.bugreaper.core.assertions.JsonAsserts;
import net.bugreaper.core.assertions.ListAsserts;
import net.bugreaper.core.config.ConfigLoader;
import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.core.filereaders.FileReader;
import net.bugreaper.core.filereaders.ResourcesFileReader;
import net.bugreaper.core.filereaders.pathfinder.ProjectPaths;
import net.bugreaper.core.generators.DataGenerator;
import net.bugreaper.core.generators.DateTimeGenerator;
import net.bugreaper.core.mappers.JsonMappers;
import net.bugreaper.core.mappers.JsonMerge;
import net.bugreaper.core.mappers.JsonObjectMappers;
import net.bugreaper.core.mappers.StringMappers;
import net.bugreaper.core.url.BaseUrl;
import net.bugreaper.core.utils.AllureResultLoader;
import net.bugreaper.core.utils.AllureStepsValidator;
import net.bugreaper.core.utils.AwaitUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class UtilityClassesTests {


    @Test
    void shouldBeUtilityConfigLoader() throws NoSuchMethodException {
        assertUtilityClass(
                ConfigLoader.class,
                IllegalStateException.class,
                "Utility class"
        );
    }
    @Test
    void shouldBeUtilityResourcesFileReader() throws NoSuchMethodException {
        assertUtilityClass(
                ResourcesFileReader.class,
                IllegalStateException.class,
                "Utility class"
        );
    }
    @Test
    void shouldBeUtilityProjectPaths() throws NoSuchMethodException {
        assertUtilityClass(
                ProjectPaths.class,
                IllegalStateException.class,
                "Utility class"
        );
    }
    @Test
    void shouldBeUtilityDataGenerator() throws NoSuchMethodException {
        assertUtilityClass(
                DataGenerator.class,
                IllegalStateException.class,
                "Utility class"
        );
    }
    @Test
    void shouldBeUtilityDateTimeGenerator() throws NoSuchMethodException {
        assertUtilityClass(
                DateTimeGenerator.class,
                IllegalStateException.class,
                "Utility class"
        );
    }



    @Test
    void shouldBeUtilityAsserts() throws NoSuchMethodException {
        assertUtilityClass(
                Asserts.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityListAsserts() throws NoSuchMethodException {
        assertUtilityClass(
                ListAsserts.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityYamlUtils() throws NoSuchMethodException {
        assertUtilityClass(
                YamlUtils.class,
                IllegalStateException.class,
                "Utility class"
        );
    }


    @Test
    void shouldBeUtilityAllureReporter() throws NoSuchMethodException {
        assertUtilityClass(
                AllureReporter.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityFileReader() throws NoSuchMethodException {
        assertUtilityClass(
                FileReader.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityAllureBuilder() throws NoSuchMethodException {
        assertUtilityClass(
                AllureBuilder.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityBaseUrl() throws NoSuchMethodException {
        assertUtilityClass(
                BaseUrl.class,
                IllegalStateException.class,
                "Utility class"
        );
    }


    @Test
    void shouldBeUtilityJsonAsserts() throws NoSuchMethodException {
        assertUtilityClass(
                JsonAsserts.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityStringMappers() throws NoSuchMethodException {
        assertUtilityClass(
                StringMappers.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityAllureResultLoader() throws NoSuchMethodException {
        assertUtilityClass(
                AllureResultLoader.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityAllureStepsValidator() throws NoSuchMethodException {
        assertUtilityClass(
                AllureStepsValidator.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityJsonObjectMappers() throws NoSuchMethodException {
        assertUtilityClass(
                JsonObjectMappers.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityJsonMappers() throws NoSuchMethodException {
        assertUtilityClass(
                JsonMappers.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityAwaitUtils() throws NoSuchMethodException {
        assertUtilityClass(
                AwaitUtils.class,
                IllegalStateException.class,
                "Utility class"
        );
    }

    @Test
    void shouldBeUtilityJsonMerge() throws NoSuchMethodException {
        assertUtilityClass(
                JsonMerge.class,
                IllegalStateException.class,
                "Utility class"
        );
    }


    private static void assertUtilityClass(
            Class<?> utilityClass,
            Class<? extends Throwable> expectedException,
            String expectedMessage)
            throws NoSuchMethodException {

        Constructor<?> constructor = utilityClass.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        Throwable cause = thrown.getCause();

        assertInstanceOf(expectedException, cause);
        assertEquals(expectedMessage, cause.getMessage());
    }

}

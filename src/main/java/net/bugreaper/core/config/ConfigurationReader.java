package net.bugreaper.core.config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConfigurationReader {

    private ConfigurationReader() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns JavaBean properties of the specified configuration object.
     *
     * <p>Only properties with an available getter are included. The
     * {@link Object} properties inherited from the base class are excluded.
     *
     * @param config configuration object to print
     * @return configuration properties formatted as {@code name=value} pairs,
     * separated by the system line separator
     * @throws IllegalArgumentException if {@code config} is {@code null}
     * @throws IllegalStateException    if the configuration properties cannot be inspected
     */
    public static String formatConfiguration(Object config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuration must not be null");
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(
                    config.getClass(),
                    Object.class
            );

            return Arrays.stream(beanInfo.getPropertyDescriptors())
                    .filter(property -> property.getReadMethod() != null)
                    .map(property -> {
                        try {
                            return property.getName() + "="
                                    + property.getReadMethod().invoke(config);
                        } catch (ReflectiveOperationException e) {
                            throw new IllegalStateException(
                                    "Unable to read configuration property: "
                                            + property.getName(),
                                    e
                            );
                        }
                    })
                    .collect(Collectors.joining(System.lineSeparator()));

        } catch (IntrospectionException e) {
            throw new IllegalStateException(
                    "Unable to inspect configuration: " + config.getClass().getName(),
                    e
            );
        }
    }
}

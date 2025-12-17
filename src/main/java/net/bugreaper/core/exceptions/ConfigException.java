package net.bugreaper.core.exceptions;

/**
 * Exceptions that can be thrown during load config file and parse keys
 */
public class ConfigException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message
     */
    public ConfigException(String message) {
        super(message);
    }

}

package net.bugreaper.core.exceptions;


/**
 *  Exception forJSON Mappers
 */
public class JsonMappersException extends RuntimeException {

    /**
     * Basic exception for JSON Mappers
     *
     * @param message A descriptive message explaining the error
     */
    public JsonMappersException(String message) {
        super(message);
    }

    /**
     * Basic exception for JSON Mappers
     *
     * @param cause The underlying cause that triggered this exception
     */
    public JsonMappersException(Throwable cause) {
        super(cause);
    }

    /**
     * Basic exception for JSON Mappers
     *
     * @param message String with message
     * @param cause the cause
     */
    public JsonMappersException(String message, Throwable cause) {
        super(message, cause);
    }

}

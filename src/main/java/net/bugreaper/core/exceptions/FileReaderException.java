package net.bugreaper.core.exceptions;


/**
 *  Exception for FileReader
 */
public class FileReaderException extends RuntimeException {

    /**
     * Basic exception for FileReader
     *
     * @param message String with message
     * @param cause the cause
     */
    public FileReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Basic exception for FileReader
     *
     * @param message A descriptive message explaining the error
     */
    public FileReaderException(String message) {
        super(message);
    }

}

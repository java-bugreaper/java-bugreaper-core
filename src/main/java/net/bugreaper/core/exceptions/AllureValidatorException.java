package net.bugreaper.core.exceptions;

/**
 *  Exception for Allure helper
 */
public class AllureValidatorException extends RuntimeException {

    /**
     * Basic exception for Allure helper
     *
     * @param message A descriptive message explaining the error
     */
    public AllureValidatorException(String message) {
        super(message);
    }

    /**
     * Basic exception for Allure helper
     *
     * @param cause The underlying cause that triggered this exception
     */
    public AllureValidatorException(Throwable cause) {
        super(cause);
    }

    /**
     * Basic exception for Allure helper
     *
     * @param message String with message
     * @param cause the cause
     */
    public AllureValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

}

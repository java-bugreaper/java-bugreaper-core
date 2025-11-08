package io.bugreaper.core.exceptions;

import org.awaitility.core.ConditionTimeoutException;

public class AssertionWithAwaitFailedError extends ConditionTimeoutException {

    public AssertionWithAwaitFailedError(String message) {
        super(message);
    }

}

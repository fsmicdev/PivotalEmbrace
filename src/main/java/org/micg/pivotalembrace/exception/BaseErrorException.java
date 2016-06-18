package org.micg.pivotalembrace.exception;

import org.micg.pivotalembrace.model.apirest.ErrorCode;

import static org.micg.pivotalembrace.model.apirest.ErrorCode.SERVER_ERROR;

public class BaseErrorException extends Exception {

    private ErrorCode errorCode;

    public BaseErrorException(final ErrorCode errorCode) {
        super(errorCode.getErrorMessage());

        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode cannot be null");
        }

        this.errorCode = errorCode;
    }

    public BaseErrorException(final BaseErrorException baseErrorException) {
        super(BaseErrorException.determineExceptionMsg(baseErrorException));

        this.errorCode = SERVER_ERROR;
    }

    public BaseErrorException(final Exception exception) {
        super(exception.getMessage());

        this.errorCode = SERVER_ERROR;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    private static String determineExceptionMsg(final BaseErrorException baseErrorException) {
        if (baseErrorException.getErrorCode() != null) {
            return baseErrorException.getErrorCode().getErrorMessage();
        } else {
            return "";
        }
    }

}

package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.exception.BaseErrorException;
import org.micg.pivotalembrace.model.apirest.ErrorCode;

/**
 * Abstraction for encapsulating exceptions thrown in the Service layer.
 *
 * @author fsmicdev
 */
public class ServiceException extends BaseErrorException {

    public ServiceException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public ServiceException(final BaseErrorException baseErrorException) {
        super(baseErrorException);
    }

    public ServiceException(final Exception exception) {
        super(exception);
    }
}

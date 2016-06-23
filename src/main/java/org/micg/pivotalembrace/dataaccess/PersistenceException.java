package org.micg.pivotalembrace.dataaccess;

import org.micg.pivotalembrace.exception.BaseErrorException;
import org.micg.pivotalembrace.model.apirest.ErrorCode;

/**
 * Abstraction for encapsulating exceptions thrown in the Data Access/Persistence layer.
 *
 * @author fsmicdev
 */
public class PersistenceException extends BaseErrorException {

    public PersistenceException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public PersistenceException(final BaseErrorException baseErrorException) {
        super(baseErrorException);
    }
}

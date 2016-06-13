package org.micg.pivotalembrace.dataaccess.sequence;

import org.micg.pivotalembrace.exception.SequenceException;

public interface SequenceDao {

    Long getNextSequenceId(String key) throws SequenceException;
}

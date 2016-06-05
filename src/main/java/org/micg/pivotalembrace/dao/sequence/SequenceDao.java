package org.micg.pivotalembrace.dao.sequence;

import org.micg.pivotalembrace.exception.SequenceException;

public interface SequenceDao {

    Long getNextSequenceId(String key) throws SequenceException;
}

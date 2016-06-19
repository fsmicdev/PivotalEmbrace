package org.micg.pivotalembrace.dataaccess.sequence;

/**
 *
 *
 * @author fsmicdev
 */
public interface SequenceDao {

    Long getNextSequenceId(String key) throws SequenceException;
}

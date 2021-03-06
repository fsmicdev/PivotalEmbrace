package org.micg.pivotalembrace.dataaccess.repository;

import org.micg.pivotalembrace.model.document.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author fsmicdev
 */
@Repository("goalsRepository")
public interface GoalRepository extends MongoRepository<Goal, Long> {

    List<Goal> findAll();

    Goal save(final Goal goal);

    void delete(final Long id);
}

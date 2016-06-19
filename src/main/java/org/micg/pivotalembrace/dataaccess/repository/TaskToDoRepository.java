package org.micg.pivotalembrace.dataaccess.repository;

import org.micg.pivotalembrace.model.document.TaskToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author fsmicdev
 */
@Repository("taskToDoRepository")
public interface TaskToDoRepository extends MongoRepository<TaskToDo, Long> {

    List<TaskToDo> findAll();

    TaskToDo save(final TaskToDo quotes);

    void delete(final Long id);
}

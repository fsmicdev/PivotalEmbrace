package org.micg.pivotalembrace.dao.repository;

import org.micg.pivotalembrace.model.TaskToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("taskToDoRepository")
public interface TaskToDoRepository extends MongoRepository<TaskToDo, Long> {

    List<TaskToDo> findAll();

    TaskToDo findOne();

    TaskToDo save(final TaskToDo quotes);
}

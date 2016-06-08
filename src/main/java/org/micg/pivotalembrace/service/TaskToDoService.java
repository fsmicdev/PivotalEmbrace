package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.model.TaskToDo;

import java.util.List;

public interface TaskToDoService {

    List<TaskToDo> getAllTaskToDos(final boolean outstandingOnly) throws ServiceException;
}

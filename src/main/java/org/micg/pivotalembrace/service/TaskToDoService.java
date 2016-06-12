package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.model.TaskPriority;
import org.micg.pivotalembrace.model.TaskToDo;

import java.util.Date;
import java.util.List;

public interface TaskToDoService {

    List<TaskToDo> getAllTaskToDos(final boolean outstandingOnly) throws ServiceException;

    TaskToDo save(final String taskToDoItemText, final TaskPriority taskPriority, final Date taskDueDate) throws ServiceException;
}

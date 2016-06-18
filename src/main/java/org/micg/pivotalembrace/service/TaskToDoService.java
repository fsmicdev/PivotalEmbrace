package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.TaskToDo;

import java.util.Date;
import java.util.List;

public interface TaskToDoService {

    List<TaskToDo> getAllTaskToDos(final boolean outstandingOnly) throws ServiceException;

    TaskToDo getTaskToDo(final Long id) throws ServiceException;

    TaskToDo save(final String taskToDoItemText, final PriorityToAttain priorityToAttain,
                  final Date taskDueDate) throws ServiceException;

    TaskToDo update(final Long id, final String taskToDoItemText, final PriorityToAttain priorityToAttain,
                    final Date taskDueDate, final Boolean completedFlag) throws ServiceException;
}

package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.Quotes;
import org.micg.pivotalembrace.model.document.TaskToDo;

import java.util.Date;
import java.util.List;

/**
 * Service interface for [TaskToDo]s in Pivotal Embrace.
 *
 * @author fsmicdev
 */
public interface TaskToDoService {

    List<TaskToDo> getAllTaskToDos(final boolean outstandingOnly) throws ServiceException;

    TaskToDo getTaskToDo(final Long id) throws ServiceException;

    TaskToDo save(final String taskToDoItemText, final PriorityToAttain priorityToAttain,
                  final Date taskDueDate) throws ServiceException;

    TaskToDo update(final Long id, final String taskToDoItemText, final PriorityToAttain priorityToAttain,
                    final Date taskDueDate, final boolean completedFlag) throws ServiceException;

    boolean delete(final TaskToDo preExistingTaskToDo) throws ServiceException;
}

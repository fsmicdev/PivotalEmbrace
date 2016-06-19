package org.micg.pivotalembrace.service.impl;

import org.micg.pivotalembrace.dataaccess.repository.TaskToDoRepository;
import org.micg.pivotalembrace.dataaccess.sequence.SequenceDao;
import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.TaskToDo;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.service.TaskToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author fsmicdev
 */
@Service("taskToDoService")
public class TaskToDoServiceImpl implements TaskToDoService {

    private static final String TASK_TO_DO_SEQ_KEY = "tasktodoid";

    private Logger logger = LoggerFactory.getLogger(TaskToDoServiceImpl.class);

    @Autowired
    private TaskToDoRepository taskToDoRepository;

    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public List<TaskToDo> getAllTaskToDos(final boolean outstandingOnly) throws ServiceException {
        try {
            return taskToDoRepository.findAll();
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TaskToDo getTaskToDo(final Long id) throws ServiceException {
        return taskToDoRepository.findOne(id);
    }

    @Override
    public TaskToDo save(final String taskToDoItemText, final PriorityToAttain priorityToAttain, final Date taskDueDate) throws ServiceException {
        final TaskToDo taskToDo = new TaskToDo();

        try {
            final Long nextIdVal = sequenceDao.getNextSequenceId(TASK_TO_DO_SEQ_KEY);
            logger.info("##### nextIdVal: " + nextIdVal);

            taskToDo.setId(nextIdVal);
            taskToDo.setTask(taskToDoItemText);
            taskToDo.setPriorityToAttain(priorityToAttain);
            taskToDo.setToDoByDate(taskDueDate);
            taskToDo.setOutstandingTask(true);

            return taskToDoRepository.save(taskToDo);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TaskToDo update(final Long id, final String taskToDoItemText, final PriorityToAttain priorityToAttain,
                           final Date taskDueDate, final boolean completedFlag) throws ServiceException {
        final TaskToDo taskToDo = new TaskToDo();

        taskToDo.setId(id);
        taskToDo.setTask(taskToDoItemText);
        taskToDo.setPriorityToAttain(priorityToAttain);
        taskToDo.setToDoByDate(taskDueDate);
        taskToDo.setOutstandingTask( !completedFlag );
        taskToDo.setOutstandingTask(true);

        try {
            return taskToDoRepository.save(taskToDo);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(final TaskToDo preExistingTaskToDo) throws ServiceException {
        try {
            if (preExistingTaskToDo == null) {
                return false;
            } else {
                taskToDoRepository.delete(preExistingTaskToDo);

                return true;
            }
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }
}

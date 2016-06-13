package org.micg.pivotalembrace.service.impl;

import org.micg.pivotalembrace.dataaccess.repository.TaskToDoRepository;
import org.micg.pivotalembrace.dataaccess.sequence.SequenceDao;
import org.micg.pivotalembrace.model.TaskPriority;
import org.micg.pivotalembrace.model.TaskToDo;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.service.TaskToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public TaskToDo save(final String taskToDoItemText, final TaskPriority taskPriority, final Date taskDueDate) throws ServiceException {
        final TaskToDo taskToDo = new TaskToDo();

        try {
            final Long nextIdVal = sequenceDao.getNextSequenceId(TASK_TO_DO_SEQ_KEY);
            logger.info("##### nextIdVal: " + nextIdVal);

            taskToDo.setId(nextIdVal);
            taskToDo.setTask(taskToDoItemText);
            taskToDo.setTaskPriority(taskPriority);
            taskToDo.setToDoByDate(taskDueDate);
            taskToDo.setOutstandingTask(true);

            return taskToDoRepository.save(taskToDo);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TaskToDo update(Long id, String taskToDoItemText, TaskPriority taskPriority, Date taskDueDate, Boolean completedFlag) throws ServiceException {
        final TaskToDo taskToDo = new TaskToDo();

        taskToDo.setId(id);
        taskToDo.setTask(taskToDoItemText);
        taskToDo.setTaskPriority(taskPriority);
        taskToDo.setToDoByDate(taskDueDate);
        taskToDo.setOutstandingTask(true);

        try {
            return taskToDoRepository.save(taskToDo);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }
}

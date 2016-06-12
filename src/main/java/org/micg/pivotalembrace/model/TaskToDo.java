package org.micg.pivotalembrace.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "task_to_do")
public class TaskToDo {

    @Id
    private Long _id;

    private String task;
    private TaskPriority taskPriority;
    private Date toDoByDate;
    private boolean outstandingTask;

    public TaskToDo() {
    }

    public TaskToDo(final String task, final TaskPriority taskPriority, final Date toDoByDate, final boolean outstandingTask) {
        this.task = task;
        this.taskPriority = taskPriority;
        this.toDoByDate = toDoByDate;
        this.outstandingTask = outstandingTask;
    }

    @Override
    public String toString() {
        return String.format(
                "TaskToDos[id=%s, task=%s, taskPriority='%s', toDoByDate='%s', outstandingTask='%s']",
                _id, task, taskPriority, toDoByDate, outstandingTask);
    }

    public Long getId() {
        return _id;
    }

    public void setId(final Long _id) {
        this._id = _id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(final String task) {
        this.task = task;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(final TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public Date getToDoByDate() {
        return toDoByDate;
    }

    public void setToDoByDate(final Date toDoByDate) {
        this.toDoByDate = toDoByDate;
    }

    public boolean isOutstandingTask() {
        return outstandingTask;
    }

    public void setOutstandingTask(final boolean outstandingTask) {
        this.outstandingTask = outstandingTask;
    }
}

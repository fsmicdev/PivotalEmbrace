package org.micg.pivotalembrace.model.document;

import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Model abstraction for [TaskToDo]s in Pivotal Embrace.
 *
 * @author fsmicdev
 */
@Document(collection = "task_to_do")
public class TaskToDo {

    @Id
    private Long _id;

    private String task;
    private PriorityToAttain priorityToAttain;
    private Date toDoByDate;
    private boolean outstandingTask;

    public TaskToDo() {
    }

    public TaskToDo(final String task, final PriorityToAttain priorityToAttain, final Date toDoByDate, final boolean outstandingTask) {
        this.task = task;
        this.priorityToAttain = priorityToAttain;
        this.toDoByDate = toDoByDate;
        this.outstandingTask = outstandingTask;
    }

    @Override
    public String toString() {
        return String.format(
                "TaskToDos[id=%s, task=%s, priorityToAttain='%s', toDoByDate='%s', outstandingTask='%s']",
                _id, task, priorityToAttain, toDoByDate, outstandingTask);
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

    public PriorityToAttain getPriorityToAttain() {
        return priorityToAttain;
    }

    public void setPriorityToAttain(final PriorityToAttain priorityToAttain) {
        this.priorityToAttain = priorityToAttain;
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

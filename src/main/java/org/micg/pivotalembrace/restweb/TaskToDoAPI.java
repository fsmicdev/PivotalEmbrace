package org.micg.pivotalembrace.restweb;

import io.swagger.annotations.*;
import org.micg.pivotalembrace.model.ErrorRespBody;
import org.micg.pivotalembrace.model.TaskPriority;
import org.micg.pivotalembrace.model.TaskToDo;
import org.micg.pivotalembrace.model.filters.LocalDateParam;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.service.TaskToDoService;
import org.micg.pivotalembrace.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("task_todos")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "task_todos",
        description = "API to create, modify, delete and retrieve Task ToDo Items.")
public class TaskToDoAPI {

    @Autowired
    private TaskToDoService taskToDoService;

    @GET
    @ApiOperation("Get all task to dos.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned all task to dos."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getAllTaskToDos() {
        try {
            return Response.ok(taskToDoService.getAllTaskToDos(true)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @GET
    @Path("/todo/{id}")
    @ApiOperation("Get a task to do by its unique id.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned task to do via unique id."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getQuote(@ApiParam(value = "Unique id of task to do.", required = true)
                             @QueryParam("id") final Long id) {
        try {
            return Response.ok(taskToDoService.getTaskToDo(id)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @POST
    @Path("/todo")
    @ApiOperation("Create and save a new To Do item into Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New Task ToDo Item was created."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response saveNewToDo(
                                @ApiParam(value = "Task to item text.", required = true)
                                @QueryParam("taskToDoItemText") final String taskToDoItemText,
                                @ApiParam(value = "Task priority.", required = true)
                                @QueryParam("taskPriority") final TaskPriority taskPriority,
                                @ApiParam(value = "Task Due Date.", required = true)
                                @QueryParam("taskDueDate") LocalDateParam taskDueDate
                               ) {
        try {
            final TaskToDo savedToDoItem = taskToDoService.save(taskToDoItemText, taskPriority, DateUtils.asDate(taskDueDate.getLocalDate()));

            if ((savedToDoItem != null) && (savedToDoItem.getId() != null)) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @PUT
    @Path("/todo/{id}")
    @ApiOperation("Update an existing To Do item in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task ToDo Item was updated."),
            @ApiResponse(code = 404, message = "The specific ToDo Item was not found."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response updateToDo(
            @ApiParam(value = "Existing id of to item to update.", required = true)
            @PathParam(value = "id") final Long id,
            @ApiParam(value = "Task to item text.", required = true)
            @QueryParam("taskToDoItemText") final String taskToDoItemText,
            @ApiParam(value = "Task priority.", required = true)
            @QueryParam("taskPriority") final TaskPriority taskPriority,
            @ApiParam(value = "Task Due Date.", required = true)
            @QueryParam("taskDueDate") final LocalDateParam taskDueDate,
            @ApiParam(value = "Has the to do item been completed?", required = true)
            @QueryParam("completedFlag") final Boolean completedFlag
    ) {
        try {
            if (taskToDoService.getTaskToDo(id) == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                final TaskToDo savedToDoItem =
                        taskToDoService.update(id, taskToDoItemText, taskPriority, DateUtils.asDate(taskDueDate.getLocalDate()), completedFlag);

                if ((savedToDoItem != null) && (savedToDoItem.getId() != null)) {
                    return Response.status(Response.Status.OK).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }
}

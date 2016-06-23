package org.micg.pivotalembrace.restweb;

import io.swagger.annotations.*;
import org.micg.pivotalembrace.model.apirest.ErrorRespBody;
import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.TaskToDo;
import org.micg.pivotalembrace.model.filters.LocalDateParam;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.service.TaskToDoService;

import org.micg.pivotalembrace.util.DatesUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST API for [TaskToDo]s in Pivotal Embrace.
 *
 * @author fsmicdev
 */
@Path("task_todos")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "task_todos",
        description = "API to create, modify, delete and retrieve Task ToDo Items.")
public class TaskToDoAPI {

    @Autowired
    private TaskToDoService taskToDoService;

    @GET
    @ApiOperation("Get all Task To Dos.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned all Task To Dos."),
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
    @ApiOperation("Get a Task To Do by its unique id.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned Task To Do via unique id."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getTaskToDo(@ApiParam(value = "Unique id of task to do.", required = true)
                                @PathParam("id") final Long id) {
        try {
            return Response.ok(taskToDoService.getTaskToDo(id)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @POST
    @Path("/todo")
    @ApiOperation("Create and save a new Task To Do item into Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New Task To Do item was created."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response saveNewTaskToDo(
            @ApiParam(value = "Task To Do item text.", required = true)
            @FormParam("taskToDoItemText") final String taskToDoItemText,
            @ApiParam(value = "Task To Do priority.", required = true)
            @FormParam("priorityToAttain") final PriorityToAttain priorityToAttain,
            @ApiParam(value = "Task Due Date.", required = true) // 2016-06-28 10:00:00.000
            @FormParam("taskDueDate") LocalDateParam taskDueDate
           ) {
        try {
            final TaskToDo savedToDoItem = taskToDoService.save(taskToDoItemText, priorityToAttain,
                    DatesUtility.asDate(taskDueDate.getLocalDate()));

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
    @ApiOperation("Update an existing Task To Do item in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task To Do item was updated."),
            @ApiResponse(code = 404, message = "The specific Task To Do item was not found."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response updateTaskToDo(
            @ApiParam(value = "Existing id of To Do item to update.", required = true)
            @PathParam(value = "id") final Long id,
            @ApiParam(value = "Task To Do item text.", required = true)
            @FormParam("taskToDoItemText") final String taskToDoItemText,
            @ApiParam(value = "Task To Do priority.", required = true)
            @FormParam("priorityToAttain") final PriorityToAttain priorityToAttain,
            @ApiParam(value = "Task Due Date.", required = true)
            @FormParam("taskDueDate") final LocalDateParam taskDueDate,
            @ApiParam(value = "Has the To Do item been completed?", required = true)
            @FormParam("completedFlag") final Boolean completedFlag
    ) {
        try {
            if (taskToDoService.getTaskToDo(id) == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                final TaskToDo savedToDoItem =
                        taskToDoService.update(id, taskToDoItemText, priorityToAttain,
                                DatesUtility.asDate(taskDueDate.getLocalDate()), completedFlag);

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

    @DELETE
    @Path("/todo/{id}")
    @ApiOperation("Delete an existing Task To Do in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task To Do was deleted."),
            @ApiResponse(code = 404, message = "The specific Task To Do was not found."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response deleteQuote(
            @ApiParam(value = "Existing id of Task To Do to delete.", required = true)
            @PathParam(value = "id") final Long id) {
        try {
            final TaskToDo preExistingTaskToDo = taskToDoService.getTaskToDo(id);

            if (preExistingTaskToDo == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                if (taskToDoService.delete(preExistingTaskToDo)) {
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

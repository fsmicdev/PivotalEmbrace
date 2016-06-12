package org.micg.pivotalembrace.restweb;

import io.swagger.annotations.*;
import org.micg.pivotalembrace.model.ErrorRespBody;
import org.micg.pivotalembrace.model.TaskPriority;
import org.micg.pivotalembrace.model.TaskToDo;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.service.TaskToDoService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("task_todosß")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "quotes",
        description = "API to create, modify, delete and retrieve Task ToDo Items.")
public class TaskToDoAPI {

    @Autowired
    private TaskToDoService taskToDoService;

    @GET
    @ApiOperation("Get all task to dos.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned all quotes."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getAllTaskToDos() {
        try {
            return Response.ok(taskToDoService.getAllTaskToDos(true)).build();
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
                                @QueryParam("taskDueDate") final Date taskDueDate
                               ) {
        try {
            final TaskToDo savedToDoItem = taskToDoService.save(taskToDoItemText, taskPriority, taskDueDate);

            if ((savedToDoItem != null) && (savedToDoItem.getId() != null)) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }
}

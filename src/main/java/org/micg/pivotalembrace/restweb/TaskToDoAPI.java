package org.micg.pivotalembrace.restweb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.micg.pivotalembrace.model.ErrorRespBody;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.service.TaskToDoService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("task_todo")
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
}

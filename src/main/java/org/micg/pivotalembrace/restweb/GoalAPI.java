package org.micg.pivotalembrace.restweb;

import io.swagger.annotations.*;
import org.micg.pivotalembrace.model.apirest.ErrorRespBody;
import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.Goal;
import org.micg.pivotalembrace.model.filters.LocalDateParam;
import org.micg.pivotalembrace.service.GoalService;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("goals")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "goals",
        description = "API to create, modify, delete and retrieve Goal Items.")
public class GoalAPI {

    @Autowired
    private GoalService goalService;

    @GET
    @ApiOperation("Get all Goals.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned all Goals."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getAllGoals() {
        try {
            return Response.ok(goalService.getAllGoals(true)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @GET
    @Path("/goal/{id}")
    @ApiOperation("Get a Goal by its unique id.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned Goal via unique id."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getGoal(@ApiParam(value = "Unique id of Goal.", required = true)
                            @QueryParam("id") final Long id) {
        try {
            return Response.ok(goalService.getGoal(id)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @POST
    @Path("/goal")
    @ApiOperation("Create and save a new Goal item into Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New Goal item was created."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response saveNewGoal(
            @ApiParam(value = "Goal title.", required = true)
            @QueryParam("goalTitle") final String goalTitle,
            @ApiParam(value = "Goal description.", required = true)
            @QueryParam("goalDescription") final String goalDescription,
            @ApiParam(value = "Priority to attain Goal.", required = true)
            @QueryParam("priorityToAttain") final PriorityToAttain priorityToAttain,
            @ApiParam(value = "Goal achieve by target date.", required = true)
            @QueryParam("toAchieveByTargetDate") final LocalDateParam toAchieveByTargetDate,
            @ApiParam(value = "Goal percentage complete [0-100; defaults to 0]", required = false)
            @QueryParam("percentageComplete") final BigDecimal percentageComplete) {
        try {
            final Goal savedGoal = goalService.save(goalTitle, goalDescription, priorityToAttain,
                    DateUtils.asDate(toAchieveByTargetDate.getLocalDate()), percentageComplete);

            if ((savedGoal != null) && (savedGoal.getId() != null)) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @PUT
    @Path("/goal/{id}")
    @ApiOperation("Update an existing Goal item in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Goal item was updated."),
            @ApiResponse(code = 404, message = "The specific Goal item was not found."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response updateGoal(
            @ApiParam(value = "Existing id of Goal to update.", required = true)
            @PathParam(value = "id") final Long id,
            @ApiParam(value = "Goal title.", required = true)
            @QueryParam("goalTitle") final String goalTitle,
            @ApiParam(value = "Goal description.", required = true)
            @QueryParam("goalDescription") final String goalDescription,
            @ApiParam(value = "Priority to attain Goal.", required = true)
            @QueryParam("priorityToAttain") final PriorityToAttain priorityToAttain,
            @ApiParam(value = "Goal achieve by target date.", required = true)
            @QueryParam("toAchieveTargetDate") final LocalDateParam toAchieveTargetDate,
            @ApiParam(value = "Goal percentage complete [0-100]", required = true)
            @QueryParam("percentageComplete") final BigDecimal percentageComplete) {
        try {
            if (goalService.getGoal(id) == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                final Goal savedGoal =
                        goalService.update(id, goalTitle, goalDescription, priorityToAttain,
                                DateUtils.asDate(toAchieveTargetDate.getLocalDate()), percentageComplete);

                if ((savedGoal != null) && (savedGoal.getId() != null)) {
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

package org.micg.pivotalembrace.restweb;

import io.swagger.annotations.*;
import org.micg.pivotalembrace.model.apirest.ErrorRespBody;
import org.micg.pivotalembrace.model.auxiliary.DiaryNote;
import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.Goal;
import org.micg.pivotalembrace.model.filters.LocalDateParam;
import org.micg.pivotalembrace.service.GoalService;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.util.DatesUtility;
import org.micg.pivotalembrace.validators.GeoCoordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

/**
 * REST API for [Goal]s in Pivotal Embrace.
 *
 * @author fsmicdev
 */
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
            return Response.ok(goalService.getAllGoals()).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @GET
    @Path("/goal/nonattained")
    @ApiOperation("Get all Goals not fully achieved, with the option of specifying a filtering Priority to Attain.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned all matching non-attained Goals (i.e. not " +
                                               "100% completed, and matching Priority to Attain - if supplied)."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getGoalsNotFullyAchieved(@ApiParam(value = "Optional Goal Priority to Attain.", required = false)
                                             @QueryParam("priorityToAttain") final PriorityToAttain priorityToAttain) {
        try {
            if (priorityToAttain != null) {
                return Response.ok(goalService.getGoalsNotFullyAchievedWithPriorityToAttain(priorityToAttain)).build();
            } else {
                return Response.ok(goalService.getAllGoalsNotFullyAchieved()).build();
            }
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
            @ApiResponse(code = 400, message = "A bad request arising from Invalid Parameters."),
            @ApiResponse(code = 404, message = "The specific Goal item was not found."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getGoal(@ApiParam(value = "Unique id of Goal.", required = true)
                            @PathParam("id") final Long id) {
        try {
            return Response.ok(goalService.getGoal(id)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @POST
    @Path("/goal")
    @ApiOperation("Create and save a new Goal in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New Goal item was created."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response saveNewGoal(
            @ApiParam(value = "Goal title.", required = true)
            @FormParam("goalTitle") final String goalTitle,
            @ApiParam(value = "Goal description.", required = true)
            @FormParam("goalDescription") final String goalDescription,
            @ApiParam(value = "Priority to attain Goal.", required = true)
            @FormParam("priorityToAttain") final PriorityToAttain priorityToAttain,
            @ApiParam(value = "Goal achieve by target date (format e.g.  2016-06-28 10:00:00.000).", required = true)
            @FormParam("toAchieveByTargetDate") final LocalDateParam toAchieveByTargetDate,
            @ApiParam(value = "Goal percentage complete [0-100; defaults to 0]", required = false)
            @FormParam("percentageComplete") final BigDecimal percentageComplete) {
        try {
            final Goal savedGoal = goalService.save(goalTitle, goalDescription, priorityToAttain,
                    DatesUtility.asDate(toAchieveByTargetDate.getLocalDate()), percentageComplete);

            if ((savedGoal != null) && (savedGoal.getId() != null)) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @POST
    @Path("/goal/{id}/note")
    @ApiOperation("Add a Diary Note to an existing Goal in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New Diary Note was created for the target Goal."),
            @ApiResponse(code = 400, message = "A bad request arising from Invalid Parameters."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response saveNewGoalDiaryNote(
            @ApiParam(value = "Existing id of Goal to add the diary note to.", required = true)
            @PathParam(value = "id") final Long id,
            @ApiParam(value = "New Diary Note text for Goal.", required = true)
            @FormParam("diaryNote") final String diaryNoteText,
            @ApiParam(value = "Date of Diary Note (format e.g.  2016-06-28 10:00:00.000).", required = true)
            @FormParam("diaryNoteDate") final LocalDateParam diaryNoteDate,
            @ApiParam(value = "Keep Diary Note private?", required = true)
            @FormParam("privateDiaryNoteFlag") final boolean privateDiaryNoteFlag,
            @ApiParam(value = "Latitude location where Diary Note was made.", required = false)
            @FormParam("diaryNoteLatitudeLocation") final Double diaryNoteLatitudeLocation,
            @ApiParam(value = "Longitude location where Diary Note was made.", required = false)
            @FormParam("diaryNoteLongitudeLocation") final Double diaryNoteLongitudeLocation) {
        try {
            final Goal existingGoal = goalService.getGoal(id);

            final DiaryNote diaryNote = new DiaryNote();
            diaryNote.setDiaryNoteText(diaryNoteText);
            diaryNote.setDiaryDate(diaryNoteDate.getLocalDate());
            diaryNote.setDiaryTime(diaryNoteDate.getLocalDateTime().toLocalTime());
            diaryNote.setKeepPrivate(privateDiaryNoteFlag);

            // Validate OPTIONAL geo-coordinate location
            if ((diaryNoteLatitudeLocation != null) && (diaryNoteLongitudeLocation != null)) {
                if (GeoCoordValidator.isValidGeoCoordPair(diaryNoteLatitudeLocation, diaryNoteLongitudeLocation)) {
                    diaryNote.setLocationGeoJsonPoint(new GeoJsonPoint(diaryNoteLatitudeLocation, diaryNoteLongitudeLocation));
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }

            existingGoal.getDiaryNotes().add(diaryNote);

            final Goal updatedGoal = goalService.update(existingGoal);

            if ((updatedGoal != null) && (updatedGoal.getId() != null)) {
                return Response.ok(updatedGoal).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @PUT
    @Path("/goal/{id}")
    @ApiOperation("Update an existing Goal in Pivotal Embrace.")
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
            @FormParam("goalTitle") final String goalTitle,
            @ApiParam(value = "Goal description.", required = true)
            @FormParam("goalDescription") final String goalDescription,
            @ApiParam(value = "Priority to attain Goal.", required = true)
            @FormParam("priorityToAttain") final PriorityToAttain priorityToAttain,
            @ApiParam(value = "Goal achieve by target date (format e.g.  2016-06-28 10:00:00.000).", required = true)
            @FormParam("toAchieveTargetDate") final LocalDateParam toAchieveTargetDate,
            @ApiParam(value = "Goal percentage complete [0-100]", required = true)
            @FormParam("percentageComplete") final BigDecimal percentageComplete) {
        try {
            Goal preExistingGoal = null;

            try {
                preExistingGoal = goalService.getGoal(id);
            } catch (final ServiceException se) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (preExistingGoal == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                final Goal savedGoal =
                        goalService.update(id, goalTitle, goalDescription, priorityToAttain,
                                DatesUtility.asDate(toAchieveTargetDate.getLocalDate()), percentageComplete);

                if ((savedGoal != null) && (savedGoal.getId() != null)) {
                    return Response.ok(savedGoal).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @DELETE
    @Path("/goal/{id}")
    @ApiOperation("Delete an existing Goal in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Goal was deleted."),
            @ApiResponse(code = 404, message = "The specific Goal was not found."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response deleteGoal(
            @ApiParam(value = "Existing id of Goal to delete.", required = true)
            @PathParam(value = "id") final Long id) {
        try {
            Goal preExistingGoal = null;

            try {
                preExistingGoal = goalService.getGoal(id);
            } catch (final ServiceException se) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (preExistingGoal == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                if (goalService.delete(preExistingGoal)) {
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

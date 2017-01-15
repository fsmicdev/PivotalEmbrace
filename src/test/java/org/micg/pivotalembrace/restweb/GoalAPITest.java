package org.micg.pivotalembrace.restweb;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.micg.pivotalembrace.model.auxiliary.DiaryNote;
import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.Goal;
import org.micg.pivotalembrace.model.filters.LocalDateParam;
import org.micg.pivotalembrace.service.GoalService;
import org.micg.pivotalembrace.service.ServiceException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.micg.pivotalembrace.model.apirest.ErrorCode.INVALID_PARAMS;
import static org.micg.pivotalembrace.model.apirest.ErrorCode.NOT_FOUND;
import static org.micg.pivotalembrace.model.apirest.ErrorCode.SERVER_ERROR;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.Matchers.any;

/**
 * Unit test class for the <code>GoalAPI</code> class.
 *
 * @author fsmicdev
 */
@RunWith(MockitoJUnitRunner.class)
public class GoalAPITest {

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalAPI goalAPI;

    private List<Goal> goals;
    private List<Goal> goalsNotFullyAchieved;

    private Goal goalOne;
    private Goal goalTwo;
    private Goal goalThree;

    private String goalTitle;

    private Long existingId;
    private String goalDescription;
    private String preExistingGoalTitle;
    private String updatedGoalTitle;

    private Goal savedGoal;
    private Goal updatedGoal;

    private Long nonExistingId;

    private LocalDate localDate = LocalDate.now();

    private Date dateNow = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    private LocalDateParam localDateParam = new LocalDateParam(dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        goals = new ArrayList<>();
        goalsNotFullyAchieved = new ArrayList<>();

        goalOne = new Goal();
        goalOne.setId(1L);
        goalOne.setGoalTitle("Run Gold Coast Marathon 2017");
        goalOne.setDescription("Without stopping to rest or walk, run the Gold Coast Marathon (i.e. 42Km) in July 2017");
        goalOne.setPercentageAchieved(BigDecimal.ZERO);
        goalOne.setDiaryNotes(new ArrayList<>());

        goalTwo = new Goal();
        goalTwo.setId(2L);
        goalTwo.setGoalTitle("Eat more fresh fruit and vegetables");
        goalTwo.setDescription("Increase my consumption of fruit and vegetables - at least 3 or 4 servings/day.");
        goalTwo.setPercentageAchieved(BigDecimal.TEN.multiply(BigDecimal.TEN));
        goalTwo.setDiaryNotes(new ArrayList<>());

        goalThree = new Goal();
        goalThree.setId(3L);
        goalThree.setGoalTitle("Travel more");
        goalThree.setDescription("Try to make more time and investment in holidays overseas; at least once every couple of years.");
        goalThree.setPercentageAchieved(BigDecimal.TEN);
        goalThree.setDiaryNotes(new ArrayList<>());

        goals.add(goalOne);
        goals.add(goalTwo);

        goalsNotFullyAchieved.add(goalOne);
        goalsNotFullyAchieved.add(goalThree);

        goalTitle = "Run Gold Coast Marathon 2017";

        existingId = 159L;
        goalDescription = "Without stopping to rest or walk, run the Gold Coast Marathon (i.e. 42Km) in July 2017";
        preExistingGoalTitle = goalTitle;
        updatedGoalTitle = preExistingGoalTitle + " (updated)";

        savedGoal = new Goal();
        savedGoal.setGoalTitle(goalTitle);
        savedGoal.setDescription(goalDescription);
        savedGoal.setId(existingId);

        updatedGoal = new Goal();
        updatedGoal.setGoalTitle(updatedGoalTitle);
        updatedGoal.setDescription(goalDescription);
        updatedGoal.setId(existingId);

        nonExistingId = 1000000123L;
    }

    @Test
    public void getAllGoals_problemQueryingAllGoals_500StatusAndInternalServerMsg() {
        try {
            // Expectations
            when(goalService.getAllGoals()).thenThrow(new ServiceException(SERVER_ERROR));

            // Call the actual method under test
            Response response = goalAPI.getAllGoals();

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getAllGoals();

            assertThat(response.getStatus(), is(equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void getAllGoals_success_200StatusAndAllGoalsReturned() {
        try {
            // Expectations
            when(goalService.getAllGoals()).thenReturn(goals);

            // Call the actual method under test
            Response response = goalAPI.getAllGoals();

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getAllGoals();

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            List<Goal> goalsList = (List<Goal>)respEntity;

            assertThat(respEntity, is(notNullValue()));
            assertThat(goalsList, hasSize(2));
            assertThat(goalsList, equalTo(goals));

            Goal goalOneReturned = goalsList.get(0);
            Goal goalTwoReturned = goalsList.get(1);

            assertThat(goalOneReturned.getId(), is(equalTo(1L)));
            assertThat(goalOneReturned.getGoalTitle(), is(equalTo("Run Gold Coast Marathon 2017")));
            assertThat(goalOneReturned.getDescription(), is(equalTo("Without stopping to rest or walk, run the Gold Coast Marathon (i.e. 42Km) in July 2017")));

            assertThat(goalTwoReturned.getId(), is(equalTo(2L)));
            assertThat(goalTwoReturned.getGoalTitle(), is(equalTo("Eat more fresh fruit and vegetables")));
            assertThat(goalTwoReturned.getDescription(), is(equalTo("Increase my consumption of fruit and vegetables - at least 3 or 4 servings/day.")));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void getGoalsNotFullyAchieved_problemQueryingForGoals_500StatusAndInternalServerMsg() {
        try {
            // Expectations
            when(goalService.getAllGoalsNotFullyAchieved()).thenThrow(new ServiceException(SERVER_ERROR));

            // Call the actual method under test
            Response response = goalAPI.getGoalsNotFullyAchieved(null);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getAllGoalsNotFullyAchieved();

            assertThat(response.getStatus(), is(equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void getGoalsNotFullyAchieved_success_200StatusAndAllGoalsNotFullyAchievedReturned() {
        try {
            // Expectations
            when(goalService.getAllGoalsNotFullyAchieved()).thenReturn(goalsNotFullyAchieved);

            // Call the actual method under test
            Response response = goalAPI.getGoalsNotFullyAchieved(null);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getAllGoalsNotFullyAchieved();

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            List<Goal> goalsNotFullAchievedList = (List<Goal>)respEntity;

            assertThat(respEntity, is(notNullValue()));
            assertThat(goalsNotFullAchievedList, hasSize(2));
            assertThat(goalsNotFullAchievedList, equalTo(goalsNotFullAchievedList));

            Goal goalNotFullyAchievedOneReturned = goalsNotFullAchievedList.get(0);
            Goal goalNotFullAchievedTwoReturned = goalsNotFullAchievedList.get(1);

            assertThat(goalNotFullyAchievedOneReturned.getId(), is(equalTo(1L)));
            assertThat(goalNotFullyAchievedOneReturned.getGoalTitle(), is(equalTo("Run Gold Coast Marathon 2017")));
            assertThat(goalNotFullyAchievedOneReturned.getDescription(), is(equalTo("Without stopping to rest or walk, run the Gold Coast Marathon (i.e. 42Km) in July 2017")));

            assertThat(goalNotFullAchievedTwoReturned.getId(), is(equalTo(3L)));
            assertThat(goalNotFullAchievedTwoReturned.getGoalTitle(), is(equalTo("Travel more")));
            assertThat(goalNotFullAchievedTwoReturned.getDescription(), is(equalTo("Try to make more time and investment in holidays overseas; at least once every couple of years.")));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void getGoal_invalidTooLowId_400BadRequestResponse() throws ServiceException {
        // Expectations
        when(goalService.getGoal(-1L)).thenThrow(new ServiceException(INVALID_PARAMS));

        // Call the actual method under test
        Response response = goalAPI.getGoal(-1L);

        // Verify (and Validation)
        assertThat(response, is(notNullValue()));

        verify(goalService).getGoal(-1L);

        Object respEntity = response.getEntity();

        assertThat(respEntity, is(nullValue()));

        assertThat(response.getStatus(), is(equalTo(INVALID_PARAMS.getHttpStatusErrorCode())));
        assertThat(response.getStatusInfo().getReasonPhrase(), is(equalTo("Bad Request")));
    }

    @Test
    public void getGoal_plausibleIdButNonExistingGoal_404NotFoundResponse() throws ServiceException {
        // Expectations
        when(goalService.getGoal(1000000L)).thenThrow(new ServiceException(NOT_FOUND));

        // Call the actual method under test
        Response response = goalAPI.getGoal(1000000L);

        // Verify (and Validation)
        assertThat(response, is(notNullValue()));

        verify(goalService).getGoal(1000000L);

        Object respEntity = response.getEntity();

        assertThat(respEntity, is(nullValue()));

        assertThat(response.getStatus(), is(equalTo(NOT_FOUND.getHttpStatusErrorCode())));
    }

    @Test
    public void getGoal_problemQueryingGoal_500StatusAndInternalServerMsg() throws ServiceException {
        // Expectations
        when(goalService.getGoal(existingId)).thenThrow(new ServiceException(SERVER_ERROR));

        // Call the actual method under test
        Response response = goalAPI.getGoal(existingId);

        // Verify (and Validation)
        assertThat(response, is(notNullValue()));

        verify(goalService).getGoal(existingId);

        assertThat(response.getStatus(), is(equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())));

        Object respEntity = response.getEntity();

        assertThat(respEntity, is(nullValue()));
    }

    @Test
    public void getGoal_plausibleId_200StatusAndGoalReturned() {
        try {
            // Expectations
            when(goalService.getGoal(existingId)).thenReturn(goalOne);

            // Call the actual method under test
            Response response = goalAPI.getGoal(existingId);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(existingId);

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(notNullValue()));

            Goal goalEntityReturned = (Goal)respEntity;

            assertThat(goalEntityReturned.getId(), is(equalTo(1L)));
            assertThat(goalEntityReturned.getGoalTitle(), is(equalTo("Run Gold Coast Marathon 2017")));
            assertThat(goalEntityReturned.getDescription(), is(equalTo("Without stopping to rest or walk, run the Gold Coast Marathon (i.e. 42Km) in July 2017")));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void saveNewGoal_problemPersistingGoal_500StatusAndInternalServerMsg() {
        try {
            // Expectations
            when(goalService.save(any(), any(), any(), any(), any())).thenThrow(new ServiceException(SERVER_ERROR));

            // Call the actual method under test
            Response response = goalAPI.saveNewGoal(goalTitle, goalDescription, PriorityToAttain.HIGH, new LocalDateParam(LocalDate.now()), BigDecimal.ZERO);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).save(any(), any(), any(), any(), any());

            assertThat(response.getStatus(), is(equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void saveNewGoal_newGoal_201StatusAndCreatedMsg() {
        try {
            // Expectations
            when(goalService.save(goalTitle, goalDescription, PriorityToAttain.HIGH, dateNow, BigDecimal.ZERO)).thenReturn(savedGoal);

            // Call the actual method under test
            Response response = goalAPI.saveNewGoal(goalTitle, goalDescription, PriorityToAttain.HIGH, localDateParam, BigDecimal.ZERO);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).save(goalTitle, goalDescription, PriorityToAttain.HIGH, dateNow, BigDecimal.ZERO);

            assertThat(response.getStatus(), is(equalTo(Response.Status.CREATED.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void saveNewGoalDiaryNote_invalidGeoLocation_400BadRequestResponse() throws ServiceException {
        try {
            String goalDiaryNoteText = "New Goal Diary Note";

            // Expectations
            when(goalService.getGoal(goalOne.getId())).thenReturn(goalOne);

            // Call the actual method under test
            Response response = goalAPI.saveNewGoalDiaryNote(goalOne.getId(),
                    goalDiaryNoteText,
                    new LocalDateParam(LocalDate.now()),
                    true,
                    -10.0,
                    200.0);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(goalOne.getId());

            assertThat(response.getStatus(), is(equalTo(Response.Status.BAD_REQUEST.getStatusCode())));
            assertThat(response.getStatusInfo().getReasonPhrase(), is(equalTo("Bad Request")));
            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void saveNewGoalDiaryNote_problemPersistingGoal_500StatusAndInternalServerMsg() {
        try {
            String goalDiaryNoteText = "New Goal Diary Note";

            // Expectations
            when(goalService.getGoal(goalOne.getId())).thenReturn(goalOne);
            when(goalService.update(any())).thenThrow(new ServiceException(SERVER_ERROR));

            // Call the actual method under test
            Response response = goalAPI.saveNewGoalDiaryNote(goalOne.getId(),
                                                             goalDiaryNoteText,
                                                             new LocalDateParam(LocalDate.now()),
                                                             true,
                                                              null,
                                                              null);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(goalOne.getId());
            verify(goalService).update(any());

            assertThat(response.getStatus(), is(equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void saveNewGoalDiaryNote_newGoalDiaryNote_201StatusAndCreatedMsg() {
        try {
            String goalDiaryNoteText = "New Goal Diary Note";

            goalOne.getDiaryNotes().add(new DiaryNote());

            // Expectations
            when(goalService.getGoal(goalOne.getId())).thenReturn(goalOne);
            when(goalService.update(any())).thenReturn(goalOne);

            // Call the actual method under test
            Response response = goalAPI.saveNewGoalDiaryNote(goalOne.getId(),
                    goalDiaryNoteText,
                    new LocalDateParam(LocalDate.now()),
                    true,
                    null,
                    null);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(goalOne.getId());
            verify(goalService).update(any());

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(notNullValue()));

            Goal returnedUpdatedGoal = (Goal)respEntity;

            assertThat(returnedUpdatedGoal.getDiaryNotes().size(), is(equalTo(2)));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void updateGoal_nonPreExistingGoal_404StatusAndNotFoundMsgReturned() {
        try {
            // Expectations
            when(goalService.getGoal(nonExistingId)).thenReturn(null);

            // Call the actual method under test
            Response response = goalAPI.updateGoal(nonExistingId, goalTitle, goalDescription, PriorityToAttain.HIGH, localDateParam, BigDecimal.ZERO);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(nonExistingId);

            assertThat(response.getStatus(), is(equalTo(Response.Status.NOT_FOUND.getStatusCode())));
            assertThat(response.getStatusInfo().getReasonPhrase(), is(equalTo("Not Found")));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void updateGoal_problemUpdatingGoalInDB_500StatusAndInternalServerMsg() {
        try {
            // Expectations
            when(goalService.getGoal(existingId)).thenReturn(savedGoal);
            when(goalService.update(existingId, goalTitle, goalDescription, PriorityToAttain.HIGH, dateNow, BigDecimal.ZERO)).thenThrow(new ServiceException(SERVER_ERROR));

            // Call the actual method under test
            Response response = goalAPI.updateGoal(existingId, goalTitle, goalDescription, PriorityToAttain.HIGH, localDateParam, BigDecimal.ZERO);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(existingId);
            verify(goalService).update(existingId, goalTitle, goalDescription, PriorityToAttain.HIGH, dateNow, BigDecimal.ZERO);

            assertThat(response.getStatus(), is(equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void updateGoal_preExistingGoal_200StatusAndUpdatedGoalReturned() {
        try {
            // Expectations
            when(goalService.getGoal(existingId)).thenReturn(savedGoal);
            when(goalService.update(existingId, goalTitle, goalDescription, PriorityToAttain.HIGH, dateNow, BigDecimal.ZERO)).thenReturn(updatedGoal);

            // Call the actual method under test
            Response response = goalAPI.updateGoal(existingId, goalTitle, goalDescription, PriorityToAttain.HIGH, localDateParam, BigDecimal.ZERO);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(existingId);
            verify(goalService).update(existingId, goalTitle, goalDescription, PriorityToAttain.HIGH, dateNow, BigDecimal.ZERO);

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(notNullValue()));

            Goal updatedGoalReturned = (Goal)respEntity;

            assertThat(updatedGoalReturned, is(notNullValue()));
            assertThat(updatedGoalReturned.getId(), is(equalTo(existingId)));
            assertThat(updatedGoalReturned.getGoalTitle(), is(equalTo(updatedGoalTitle)));
            assertThat(updatedGoalReturned.getDescription(), is(equalTo(goalDescription)));

            assertThat(updatedGoalReturned, is(equalTo(updatedGoal)));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void deleteGoal_nonPreExistingGoal_404StatusAndNotFoundMsgReturned() {
        try {
            // Expectations
            when(goalService.getGoal(nonExistingId)).thenReturn(null);

            // Call the actual method under test
            Response response = goalAPI.deleteGoal(nonExistingId);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(nonExistingId);

            assertThat(response.getStatus(), is(equalTo(Response.Status.NOT_FOUND.getStatusCode())));
            assertThat(response.getStatusInfo().getReasonPhrase(), is(equalTo("Not Found")));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void deleteGoal_problemDeletingGoalInDB_500StatusAndInternalServerMsg() {
        try {
            // Expectations
            when(goalService.getGoal(existingId)).thenReturn(savedGoal);
            when(goalService.delete(savedGoal)).thenThrow(new ServiceException(SERVER_ERROR));

            // Call the actual method under test
            Response response = goalAPI.deleteGoal(existingId);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(existingId);
            verify(goalService).delete(savedGoal);

            assertThat(response.getStatus(), is(equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void deleteGoal_preExistingGoal_200Status() {
        try {
            // Expectations
            when(goalService.getGoal(existingId)).thenReturn(savedGoal);
            when(goalService.delete(savedGoal)).thenReturn(true);

            // Call the actual method under test
            Response response = goalAPI.deleteGoal(existingId);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(goalService).getGoal(existingId);
            verify(goalService).delete(savedGoal);

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(nullValue()));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }
}

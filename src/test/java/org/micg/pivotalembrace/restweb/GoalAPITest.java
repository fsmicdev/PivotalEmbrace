package org.micg.pivotalembrace.restweb;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.micg.pivotalembrace.model.apirest.ErrorRespBody;
import org.micg.pivotalembrace.model.auxiliary.DiaryNote;
import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.Goal;
import org.micg.pivotalembrace.model.filters.LocalDateParam;
import org.micg.pivotalembrace.service.GoalService;
import org.micg.pivotalembrace.service.ServiceException;
import org.micg.pivotalembrace.util.DatesUtility;
import org.micg.pivotalembrace.validators.GeoCoordValidator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.micg.pivotalembrace.model.apirest.ErrorCode.SERVER_ERROR;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        goals = new ArrayList<>();
        goalsNotFullyAchieved = new ArrayList<>();

        goalOne = new Goal();
        goalOne.setId(1L);
        goalOne.setGoalTitle("Run Gold Coast Marathon 2017");
        goalOne.setDescription("Without stopping to rest or walk, run the Gold Coast Marathon (i.e. 42Km) in July 2017");
        goalOne.setPercentageAchieved(new BigDecimal(0));

        goalTwo = new Goal();
        goalTwo.setId(2L);
        goalTwo.setGoalTitle("Eat more fresh fruit and vegetables");
        goalTwo.setDescription("Increase my consumption of fruit and vegetables - at least 3 or 4 servings/day.");
        goalTwo.setPercentageAchieved(new BigDecimal(100));

        goalThree = new Goal();
        goalThree.setId(3L);
        goalThree.setGoalTitle("Travel more");
        goalThree.setDescription("Try to make more time and investment in holidays overseas; at least once every couple of years.");
        goalThree.setPercentageAchieved(new BigDecimal(10));

        goals.add(goalOne);
        goals.add(goalTwo);

        goalsNotFullyAchieved.add(goalOne);
        goalsNotFullyAchieved.add(goalThree);
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

    // getGoalsNotFullyAchieved()

    @Test
    public void getGoalsNotFullyAchieved_problemQueryingAllGoals_500StatusAndInternalServerMsg() {
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
    public void getGoalsNotFullyAchieved_success_200StatusAndAllGoalsReturned() {
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

    // getGoal()

    // saveNewGoal()

    // saveNewGoalDiaryNote()

    // updateGoal()

    // deleteGoal()

}

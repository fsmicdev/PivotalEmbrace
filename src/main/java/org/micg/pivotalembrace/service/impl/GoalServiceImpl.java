package org.micg.pivotalembrace.service.impl;

import org.micg.pivotalembrace.dataaccess.PersistenceException;
import org.micg.pivotalembrace.dataaccess.repository.GoalRepository;
import org.micg.pivotalembrace.dataaccess.sequence.SequenceDao;
import org.micg.pivotalembrace.dataaccess.template.GoalTemplate;
import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.Goal;
import org.micg.pivotalembrace.service.GoalService;
import org.micg.pivotalembrace.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author fsmicdev
 */
@Service("goalService")
public class GoalServiceImpl implements GoalService {

    private static final String GOAL_SEQ_KEY = "goalid";

    private Logger logger = LoggerFactory.getLogger(GoalServiceImpl.class);

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalTemplate goalTemplate;

    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public List<Goal> getAllGoals() throws ServiceException {
        try {
            return goalRepository.findAll();
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Goal> getAllGoalsNotFullyAchieved() throws ServiceException {
        try {
            return goalTemplate.getAllGoalsNotFullyAchieved();
        } catch (final PersistenceException pe) {
            throw new ServiceException(pe);
        }
    }

    @Override
    public Goal getGoal(final Long id) throws ServiceException {
        return goalRepository.findOne(id);
    }

    @Override
    public Goal save(final String goalTitle, final String goalDescription, final PriorityToAttain priorityToAttain,
                     final Date toAchieveByDate, final BigDecimal percentageComplete) throws ServiceException {
        final Goal goal = new Goal();

        try {
            final Long nextIdVal = sequenceDao.getNextSequenceId(GOAL_SEQ_KEY);
            logger.info("##### nextIdVal: " + nextIdVal);

            goal.setId(nextIdVal);
            goal.setGoalTitle(goalTitle);
            goal.setDescription(goalDescription);
            goal.setPriorityToAttain(priorityToAttain);
            goal.setToAchieveByTargetDate(toAchieveByDate);
            goal.setPercentageAchieved(percentageComplete);

            return goalRepository.save(goal);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Goal update(final Long id, final String goalTitle, final String goalDescription,
                       final PriorityToAttain priorityToAttain, final Date toAchieveByDate,
                       final BigDecimal percentageComplete) throws ServiceException {
        final Goal goal = new Goal();

        goal.setId(id);
        goal.setGoalTitle(goalTitle);
        goal.setDescription(goalDescription);
        goal.setPriorityToAttain(priorityToAttain);
        goal.setToAchieveByTargetDate(toAchieveByDate);
        goal.setPercentageAchieved(percentageComplete);

        try {
            return goalRepository.save(goal);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(final Goal preExistingGoal) throws ServiceException {
        try {
            if (preExistingGoal == null) {
                return false;
            } else {
                goalRepository.delete(preExistingGoal);

                return true;
            }
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }
}

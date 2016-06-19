package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.Goal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author fsmicdev
 */
public interface GoalService {

    List<Goal> getAllGoals() throws ServiceException;

    List<Goal> getAllGoalsNotFullyAchieved() throws ServiceException;

    Goal getGoal(final Long id) throws ServiceException;

    Goal save(final String goalTitle, final String goalDescription, final PriorityToAttain priorityToAttain,
              final Date toAchieveByDate, final BigDecimal percentageComplete) throws ServiceException;

    Goal update(final Long id, final String goalTitle, final String goalDescription, final PriorityToAttain priorityToAttain,
                final Date toAchieveByDate, final BigDecimal percentageComplete) throws ServiceException;

    boolean delete(final Goal preExistingGoal) throws ServiceException;
}

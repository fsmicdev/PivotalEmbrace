package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.micg.pivotalembrace.model.document.Goal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface GoalService {

    List<Goal> getAllGoals(final boolean totallyCompletedGoalsOnly) throws ServiceException;

    Goal getGoal(final Long id) throws ServiceException;

    Goal save(final String goalTitle, final String goalDescription, final PriorityToAttain priorityToAttain,
              final Date toAchieveByDate, final BigDecimal percentageComplete) throws ServiceException;

    Goal update(final Long id, final String goalTitle, final String goalDescription, final PriorityToAttain priorityToAttain,
                final Date toAchieveByDate, final BigDecimal percentageComplete) throws ServiceException;
}

package org.micg.pivotalembrace.dataaccess.template;

import org.micg.pivotalembrace.dataaccess.PersistenceException;
import org.micg.pivotalembrace.model.document.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 *
 * @author fsmicdev
 */
@Repository
public class GoalTemplate {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Goal> getAllGoalsNotFullyAchieved() throws PersistenceException {
        final Query query = new Query();
        query.addCriteria(Criteria.where("percentageAchieved").lt(new BigDecimal(100)));

        return mongoTemplate.find(query, Goal.class);
    }

}

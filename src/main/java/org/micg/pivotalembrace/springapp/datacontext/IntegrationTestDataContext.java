package org.micg.pivotalembrace.springapp.datacontext;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 *
 * @author fsmicdev
 */
@Profile("integration-test")
public class IntegrationTestDataContext extends BaseDataContext {

    @Autowired
    private Environment environment;

    @Override
    protected String getDatabaseName() {
        return "pivotalembrace"; // @TODO: For this INTEGRATION TESTING, this will be a different database name
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("localhost" , 27017);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }

}

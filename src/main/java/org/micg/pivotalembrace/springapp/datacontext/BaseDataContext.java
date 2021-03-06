package org.micg.pivotalembrace.springapp.datacontext;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * Base class for Spring-based configuration of environment specific data contexts.
 *
 * @author fsmicdev
 */
public abstract class BaseDataContext extends AbstractMongoConfiguration {

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

}

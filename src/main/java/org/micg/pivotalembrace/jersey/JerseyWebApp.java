package org.micg.pivotalembrace.jersey;

import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class JerseyWebApp extends ResourceConfig {

    public JerseyWebApp() {
        final BeanConfig swaggerBeanConfig = new BeanConfig();
        swaggerBeanConfig.setResourcePackage("org.micg.pivotalembrace.restweb");
        swaggerBeanConfig.setBasePath("/pivotalembrace/rest");
        swaggerBeanConfig.setTitle("Pivotal Embrace REST API");
        swaggerBeanConfig.setDescription("REST API for the Pivotal Embrace WebApp");
        swaggerBeanConfig.setScan(true);

        packages("org.micg.pivotalembrace.restweb",
                 "org.micg.pivotalembrace.jersey.provider",
                 "io.swagger.jaxrs.listing",
                 "org.micg.pivotalembrace.jersey.filters");
    }
}

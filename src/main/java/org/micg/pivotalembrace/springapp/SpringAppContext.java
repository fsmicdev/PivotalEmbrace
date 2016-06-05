package org.micg.pivotalembrace.springapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.core.CacheControl;

@Configuration
@ComponentScan(basePackages = {"org.micg.pivotalembrace"})
public class SpringAppContext {

    @Bean
    public CacheControl cacheControl() {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);
        cacheControl.setNoStore(false);
        cacheControl.setMaxAge(60); // one minute

        return cacheControl;
    }
}

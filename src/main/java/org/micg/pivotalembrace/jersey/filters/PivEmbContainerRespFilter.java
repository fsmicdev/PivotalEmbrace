package org.micg.pivotalembrace.jersey.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class PivEmbContainerRespFilter implements ContainerResponseFilter {

    @Override
    public void filter(final ContainerRequestContext request,
                       final ContainerResponseContext response) throws IOException {
        final MultivaluedMap<String, Object> respHeaders = response.getHeaders();

        respHeaders.add("Access-Control-Allow-Origin", "*");
        respHeaders.add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        respHeaders.add("Access-Control-Allow-Credentials", "true");
        respHeaders.add("Access-Control-Allow-Methods", "GET, POST");
    }

}

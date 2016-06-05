package org.micg.pivotalembrace.jersey.provider;

import org.micg.pivotalembrace.model.ErrorRespBody;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static org.micg.pivotalembrace.model.ErrorCode.INVALID_PARAMS;

@Provider
public class PivEmbWebAppExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(final WebApplicationException webApplicationException) {
        return Response.status(INVALID_PARAMS.getHttpStatusErrorCode())
                .entity(new ErrorRespBody(INVALID_PARAMS.name(), INVALID_PARAMS.getErrorMessage()))
                .build();
    }

}

package org.micg.pivotalembrace.jersey.provider;

import org.micg.pivotalembrace.model.apirest.ErrorRespBody;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static org.micg.pivotalembrace.model.apirest.ErrorCode.INVALID_PARAMS;

/**
 *
 *
 * @author fsmicdev
 */
@Provider
public class PivEmbWebAppExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(final WebApplicationException webApplicationException) {
        return Response.status(INVALID_PARAMS.getHttpStatusErrorCode())
                .entity(new ErrorRespBody(INVALID_PARAMS.name(), INVALID_PARAMS.getErrorMessage()))
                .build();
    }

}

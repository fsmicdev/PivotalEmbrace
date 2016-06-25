package org.micg.pivotalembrace.model.apirest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * Enum encapsulating pre-initialised error messages for HTTP status error codes.
 *
 * @author fsmicdev
 */
@ApiModel("errorCode")
public enum ErrorCode {

    INVALID_PARAMS("Invalid parameters supplied.", SC_BAD_REQUEST),
    NOT_FOUND("The resource entity was not found.", SC_NOT_FOUND),
    SERVER_ERROR("The server experienced an unexpected application error.", SC_INTERNAL_SERVER_ERROR);

    private String errorMessage;

    @ApiModelProperty(hidden = true)
    private Integer httpStatusErrorCode;

    ErrorCode(final String errorMessage, final Integer httpStatusErrorCode){
        this.errorMessage = errorMessage;
        this.httpStatusErrorCode = httpStatusErrorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Integer getHttpStatusErrorCode() {
        return this.httpStatusErrorCode;
    }
}

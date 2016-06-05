package org.micg.pivotalembrace.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@ApiModel("errorCode")
public enum ErrorCode {

    INVALID_PARAMS("Invalid parameters supplied",SC_BAD_REQUEST),
    SERVER_ERROR("The server experienced an unexpected application error", SC_INTERNAL_SERVER_ERROR);

    private String errorMessage;

    @ApiModelProperty(hidden = true)
    private Integer httpStatusErrorCode;

    private ErrorCode(final String errorMessage,final Integer httpStatusErrorCode){
        this.errorMessage=errorMessage;
        this.httpStatusErrorCode=httpStatusErrorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Integer getHttpStatusErrorCode() {
        return this.httpStatusErrorCode;
    }
}

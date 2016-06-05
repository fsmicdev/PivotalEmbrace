package org.micg.pivotalembrace.model;

import io.swagger.annotations.ApiModel;

@ApiModel
public class ErrorRespBody {

    private String code;
    private String message;

    public ErrorRespBody(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}

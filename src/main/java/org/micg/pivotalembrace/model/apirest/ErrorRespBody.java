package org.micg.pivotalembrace.model.apirest;

import io.swagger.annotations.ApiModel;

/**
 *
 *
 * @author fsmicdev
 */
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

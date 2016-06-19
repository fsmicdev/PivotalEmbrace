package org.micg.pivotalembrace.dataaccess.sequence;

/**
 *
 *
 * @author fsmicdev
 */
public class SequenceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errCode;
    private String errMsg;

    public SequenceException(final String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(final String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(final String errMsg) {
        this.errMsg = errMsg;
    }
}

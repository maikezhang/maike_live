package cn.idongjia.live.api.live.pojo.response;

import lombok.ToString;

import java.io.Serializable;

/**
 * 响应
 *
 * @author lc
 * @create at 2018/7/6.
 */

public class Response  implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isSuccess;

    private int errCode;

    private String errMessage;

    public boolean isSuccess() {
        return isSuccess;
    }


    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }


    public int getErrCode() {
        return errCode;
    }


    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }


    public String getErrMessage() {
        return errMessage;
    }


    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }


    @Override
    public String toString() {
        return "Response [isSuccess=" + isSuccess + ", errCode=" + errCode + ", errMessage=" + errMessage + "]";
    }

    public static Response buildFailure(int errCode, String errMessage) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static Response buildSuccess(){
        Response response = new Response();
        response.setSuccess(true);
        return response;
    }
}

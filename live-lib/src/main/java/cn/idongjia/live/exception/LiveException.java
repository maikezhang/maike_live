package cn.idongjia.live.exception;

import cn.idongjia.exception.ApiException;
import cn.idongjia.exception.ResultCode;

import javax.ws.rs.core.Response;

public class LiveException extends ApiException {

    public LiveException(Response response, String log) {
        super(response, log);
    }

    public LiveException(String log, String msg, Integer status) {
        super(log, msg, status);
    }

    public LiveException(String msg, Integer status) {
        super(msg, status);
    }

    public LiveException(String msg) {
        super(-12138, msg);
    }

    public LiveException(Integer code, String msg) {
        super(code, msg);
    }


    public LiveException(Integer code, String msg, String message) {
        super(code, msg, message);
    }

    public LiveException(ResultCode resultCode, Object... variables) {
        super(resultCode, variables);
    }

    public LiveException(String log, String msg, Integer status, Integer code) {
        super(log, msg, status, code);
    }

    public static LiveException failure(String msg){
        return new LiveException(-12138, msg);
    }

    public static LiveException failure(int code, String msg) {
        return new LiveException(code, msg);
    }
}

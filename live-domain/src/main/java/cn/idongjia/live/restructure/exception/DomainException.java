package cn.idongjia.live.restructure.exception;

import cn.idongjia.exception.ApiException;

/**
 * 内容对外异常类
 *
 * @version 1.0
 */
public class DomainException extends ApiException {

    public static final int ERROR_CODE = -42;



    public DomainException(Integer code, String msg) {
        super(code, msg);
    }

    public static DomainException failure(String msg) {
        return new DomainException(ERROR_CODE, msg);
    }

}

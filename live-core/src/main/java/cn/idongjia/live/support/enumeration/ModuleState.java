package cn.idongjia.live.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

public enum ModuleState implements BaseEnum {
    NOT_BEGIN(0, "未开始"), IN_PROGRESS(1, "进行中"), EXPIRED(2, "已过期");
    private Integer code;
    private String msg;

    ModuleState(int code, String msg){
        this.code = code;
        this.msg = msg;
    };

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

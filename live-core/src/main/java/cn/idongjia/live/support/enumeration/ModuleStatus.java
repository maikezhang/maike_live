package cn.idongjia.live.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

public enum ModuleStatus implements BaseEnum{
    DEL(-1, "删除"), OFF(0, "下架"), ON(1, "上架");

    private Integer code;
    private String msg;

    ModuleStatus(int code, String msg){
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

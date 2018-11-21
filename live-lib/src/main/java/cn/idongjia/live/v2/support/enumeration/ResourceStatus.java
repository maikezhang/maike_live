package cn.idongjia.live.v2.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

/**
 * 直播资源状态
 * @author zhang created on 2018/1/17 下午11:18
 * @version 1.0
 */
public enum ResourceStatus implements BaseEnum{

    /**
     * 正常状态
     */
    NORMAL(1, "正常"),
    /**
     * 删除状态
     */
    DEL(0, "删除");

    private int code;
    private String msg;

    ResourceStatus(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}

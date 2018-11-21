package cn.idongjia.live.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

/**
 * 直播时间策略状态
 * @author zhang created on 2018/1/17 下午1:37
 * @version 1.0
 */
public enum  TimeStrategyStatus implements BaseEnum{
    /**
     * 正常状态
     */
    NORMAL(0, "正常"),
    /**
     * 删除状态
     */
    DEL(-1, "删除");

    private int code;
    private String msg;

    TimeStrategyStatus(int code, String msg){
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

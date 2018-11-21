package cn.idongjia.live.restructure.v2.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

/**
 * 直播时间策略类型
 * @author zhang created on 2018/1/17 下午1:33
 * @version 1.0
 */
public enum LivePlayType implements BaseEnum {
    /**
     * 单次播放类型
     */
    ONCE(0, "单次播放"),
    /**
     * 每周播放一次
     */
    WEEKLY(1, "周播放"),
    /**
     * 每日播放一次
     */
    DAILY(2, "日播放");

    private int code;
    private String msg;

    LivePlayType(int code, String msg){
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

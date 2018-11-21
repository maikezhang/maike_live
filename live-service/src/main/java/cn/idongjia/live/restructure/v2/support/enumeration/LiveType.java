package cn.idongjia.live.restructure.v2.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

/**
 * 直播类型枚举
 * @author zhang created on 2018/1/17 下午1:44
 * @version 1.0
 */
public enum  LiveType implements BaseEnum {
    /**
     * 普通直播
     */
    NORMAL_LIVE(1, "纯直播"),
    /**
     * 拍卖直播
     */
    AUCTION_LIVE(2, "拍卖直播");

    private int code;
    private String msg;

    LiveType(int code, String msg){
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

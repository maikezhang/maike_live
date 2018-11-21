package cn.idongjia.live.restructure.v2.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

/**
 * 直播房间状态
 * @author zhang created on 2018/1/17 下午2:00
 * @version 1.0
 */
public enum  LiveRoomStatus implements BaseEnum{
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

    LiveRoomStatus(int code, String msg){
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

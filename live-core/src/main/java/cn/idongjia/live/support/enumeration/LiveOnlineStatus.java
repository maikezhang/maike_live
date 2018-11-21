package cn.idongjia.live.support.enumeration;


import cn.idongjia.live.support.BaseEnum;

/**
 * 直播上下线状态枚举类
 * @author zhang created on 2018/1/17 下午1:27
 * @version 1.0
 */
public enum LiveOnlineStatus implements BaseEnum {
    /**
     * 直播上线状态
     */
    ON_LINE(1, "上线"),
    /**
     * 直播下线状态
     */
    OFF_LINE(0, "下线");

    private int code;
    private String msg;

    LiveOnlineStatus(int code, String msg){
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

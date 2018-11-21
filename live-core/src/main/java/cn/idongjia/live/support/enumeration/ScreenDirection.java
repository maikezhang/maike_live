package cn.idongjia.live.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

/**
 * @author zhang created on 2018/1/18 上午9:33
 * @version 1.0
 */
public enum ScreenDirection implements BaseEnum{
    /**
     * 竖屏
     */
    VERTICAL(0, "竖屏"),
    /**
     * 横屏
     */
    HORIZONTAL(1, "横屏");

    private int code;
    private String msg;

    ScreenDirection(int code, String msg){
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

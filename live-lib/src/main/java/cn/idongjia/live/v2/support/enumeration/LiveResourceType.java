package cn.idongjia.live.v2.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

/**
 * 直播资源类型
 * @author zhang created on 2018/1/18 下午3:25
 * @version 1.0
 */
public enum LiveResourceType implements BaseEnum {
    /**
     * 超级模版
     */
    TEMPLATE(0, "超级模版"),
    /**
     * 商品
     */
    ITEM(1, "商品"),
    /**
     * 拍品
     */
    AUCTION(2, "拍品");

    private int code;
    private String msg;

    LiveResourceType(int code, String msg){
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

package cn.idongjia.live.support.enumeration;

import cn.idongjia.live.support.BaseEnum;

/**
 * 直播云类型
 * @author zhang created on 2018/1/17 下午1:55
 * @version 1.0
 */
public enum LiveCloudType implements BaseEnum{
    /**
     * 腾讯云直播
     */
    Q_CLOUD(1, "腾讯云"),
    /**
     * 网易云直播
     */
    V_CLOUD(2, "网易云"),
    /**
     * 自定义直播
     */
    D_CLOUD(3, "自定义云");

    private int code;
    private String msg;

    LiveCloudType(int code, String msg){
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

package cn.idongjia.live.v2.support;

/**
 * 直播基本功能
 * @author zhang created on 2018/1/17 下午1:20
 * @version 1.0
 */
public interface Show {

    /**
     * 开始直播
     */
    void startShow();

    /**
     * 结束直播
     */
    void stopShow();

    /**
     * 暂停直播
     */
    void pauseShow();

    /**
     * 恢复直播
     */
    void resumeShow();

    /**
     * 调试直播
     */
    void debugShow();
}

package cn.idongjia.live.support;

import java.util.concurrent.TimeUnit;

/**
 * redis相关常量
 *
 * @author zhang created on 2017/9/26 上午12:29
 * @version 1.0
 */
public final class RedisConst {

    private RedisConst() {
    }

    public static final int EXPIRE_TIME = Long.valueOf(TimeUnit.MINUTES.toSeconds(2)).intValue();
    public static final String OK = "OK";
    public static final String UNDER_LINE = "_";
    public static final String WORK_SPACE = "live.";
    public static final String DOT = ".";
    public static final String CACHE_TAG_POST    = "CTP.";
    public static final String BLOCK_SUFFIX = ".block";
    public static final String LIVE_URL_PRE = "live.url";


}

package cn.idongjia.live.restructure.exception;

import cn.idongjia.exception.ApiException;

/**
 * Created by dongjia_lj on 17/3/10.
 *  * * 错误码规则: x xx xx xx xx
 * 产品1  东家
 * 模块 05
 * 子模块 01 banner
 *       02 tag
 *       03 book
 *       05 resource
 *
 * 错误类型 xxxx
 */
public final class PureLiveException {
    public static final ApiException LIVE_BANNER_PRIMARY_MISS = new ApiException(104010102
            , "直播banner主键丢失");
    public static final ApiException LIVE_BOOK_UID_MISS = new ApiException(104030001
            , "直播订阅用户ID缺失");
    public static final ApiException LIVE_BOOK_LID_MISS = new ApiException(104030001
            , "直播订阅直播ID缺失");
    public static final ApiException LIVE_BOOK_CUID_MISS = new ApiException(104030002
            , "直播订阅匠人ID缺失");
    public static final ApiException LIVE_TAG_NAME_MISS = new ApiException(104020001
            , "直播标签名字缺失");
    public static final ApiException LIVE_TAG_TYPE_MISS = new ApiException(104020002
            , "直播标签类型缺失");


    public static final ApiException LIVE_UID_MISS = new ApiException(10404001, "纯直播主播ID确实");

    public static final ApiException LIVE_NOT_FOUND = new ApiException(10404002, "没有对应的直播");
    public static final ApiException RESOURCE_NOT_FOUND = new ApiException(104050001, "没有对应的资源");

    public static final ApiException LIVE_ANCHOR_NOT_FOUND = new ApiException(10404002, "没有对应的直播");
}

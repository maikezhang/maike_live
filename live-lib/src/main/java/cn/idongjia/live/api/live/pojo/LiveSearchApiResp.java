package cn.idongjia.live.api.live.pojo;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 搜索api响应数据
 * @author dongjia_lj
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveSearchApiResp extends Base {
    /**
     * 直播id
     */
    private Long id;

    /**
     * 是否订阅 0：否 1:是
     */
    private int bookState;

    /**
     * 封面图
     */
    private String cover;
    /**
     * 匠人头像
     */
    private String craftsmanAvatar;
    /**
     * 匠人名称
     */
    private String craftsmanName;
    /**
     * 热度
     */
    private int hot;
    /**
     * 预计结束时间
     */
    private Long planEndTime;
    /**
     * 预计开始时间
     */
    private Long planStartTime;
    /**
     * 直播进程
     * 1未开始2已开始3已结束
     */
    private int state;
    /**
     * 直播标题
     */
    private String title;
    /**
     * 直播类型
     * 直播类型 1：纯直播 2：直播拍 3：匠购 4：探宝 5：匠说
     */
    private int type;

    /**
     * 直播拍id
     */
    private Long asid;
}

package cn.idongjia.live.v2.pojo.query;

import cn.idongjia.common.query.BaseSearch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveAnchorQuery extends BaseSearch {
    /**
     * 主播的用户id，用户表的uid
     */
    private Long    userId;
    /**
     * 主播类型；0=匠人
     */
    private Integer anchorType;
    /**
     * 当前主播状态；1=可以直播，2=禁播
     */
    private Integer anchorState;
    /**
     * 修改时间，起
     */
    private Long    updateTimeFrom;
    /**
     * 修改时间，止
     */
    private Long    updateTimeEnd;

}

package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/8.
 */
@Getter
@Setter
public class PageTabLivePO extends BasePO {
    /**
     * id
     */
    private Long id;
    /**
     * 直播id
     */
    private Long liveId;

    /**
     * tab id
     */
    private Long tabId;


    /**
     * 显示状态 冗余设计 0 不显示 1显示
     */

    private Integer showStatus;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
}

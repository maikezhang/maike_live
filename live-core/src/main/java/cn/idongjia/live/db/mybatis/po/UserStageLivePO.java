package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
public class UserStageLivePO extends BasePO {
    /**
     * id
     */
    private Long id;
    /**
     * stage类型 0新用户 1老用户
     */
    private int stage;
    /**
     * 关联id
     */
    private long liveId;

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

    /**
     * 数据有效状态 0已失效 1生效中
     */
    private Integer status;

    /**
     * 显示状态
     */
    private Integer showStatus;
}

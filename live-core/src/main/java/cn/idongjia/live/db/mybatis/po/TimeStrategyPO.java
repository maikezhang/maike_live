package cn.idongjia.live.db.mybatis.po;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * 直播时间策略数据类
 * @author zhang
 */
@Setter
@Getter
@ToString
public class TimeStrategyPO extends BasePO{

    /**
     * 唯一ID
     */
    private Long id;
    /**
     * 直播方式类型
     */
    private Integer type;
    /**
     * 直播范围开始时间
     */
    private Timestamp periodStartTime;
    /**
     * 直播范围结束时间
     */
    private Timestamp periodEndTime;
    /**
     * 策略状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后修改时间
     */
    private Timestamp modifiedTime;
}

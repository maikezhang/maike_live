package cn.idongjia.live.pojo.purelive;

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
public class TimeStrategyDO extends Base{

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
    private Timestamp periodStartTm;
    /**
     * 直播范围结束时间
     */
    private Timestamp periodEndTm;
    /**
     * 策略状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Timestamp createTm;
    /**
     * 最后修改时间
     */
    private Timestamp modifiedTm;
}

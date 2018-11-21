package cn.idongjia.live.v2.domain.show;

import cn.idongjia.live.v2.support.Entity;
import cn.idongjia.live.v2.support.enumeration.TimeStrategyStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 直播时间策略
 * 可以使用时间策略来创建对应重复播出等
 * @author zhang created on 2018/1/17 下午1:31
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class TimeStrategy extends Entity {

    /**
     * 时间策略id
     */
    private Long id;
    /**
     * 直播时间范围开始
     */
    private Long periodStartTime;
    /**
     * 直播时间范围结束
     */
    private Long periodEndTime;
    /**
     * 直播时间策略状态
     */
    private TimeStrategyStatus status;

}

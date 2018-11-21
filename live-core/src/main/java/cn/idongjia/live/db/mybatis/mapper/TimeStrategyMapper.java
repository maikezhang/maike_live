package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.TimeStrategyPO;
import cn.idongjia.live.pojo.purelive.TimeStrategyDO;
import cn.idongjia.live.pojo.purelive.TimeStrategyDO;

/**
 * 时间策略mapper
 * @author zhang
 */
public interface TimeStrategyMapper {

    /**
     * 插入时间策略
     * @param strategyDO 时间策略
     * @return 插入条数
     */
    int insertStrategy(TimeStrategyDO strategyDO);

    /**
     * 更新时间策略
     * @param strategyDO 时间策略
     * @return 更新条数
     */
    int updateStrategy(TimeStrategyDO strategyDO);

    /**
     * 根据id查询时间策略
     * @param sid 策略id
     * @return 时间策略
     */
    TimeStrategyPO getStrategy(Long sid);

}

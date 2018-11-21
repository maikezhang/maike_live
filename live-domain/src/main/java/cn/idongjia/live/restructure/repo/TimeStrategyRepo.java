package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.TimeStrategyMapper;
import cn.idongjia.live.db.mybatis.po.TimeStrategyPO;
import cn.idongjia.live.pojo.purelive.TimeStrategyDO;
import cn.idongjia.live.restructure.dto.TimeStrategyDTO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static cn.idongjia.live.support.DateTimeUtil.millis2Timestamp;
import static cn.idongjia.live.support.DateTimeUtil.timestamp2Millis;

@Component
public class TimeStrategyRepo {

    @Resource
    private TimeStrategyMapper strategyMapper;

    private static final Log LOGGER = LogFactory.getLog(TimeStrategyRepo.class);

    /**
     * 增加时间策略
     *
     * @param strategyDO 策略信息
     * @return 策略ID
     */
    public Long addStrategy(TimeStrategyDO strategyDO) {
        LOGGER.info("增加时间策略内容为: " + strategyDO);
        if(strategyDO.getType() == null) {
            strategyDO.setType(LiveConst.TYPE_STRATEGY_ONCE);
        }
        LOGGER.info("增加新的时间策略: " + strategyDO);
        strategyDO.setStatus(LiveConst.STATUS_STRATEGY_NORMAL);
        strategyDO.setCreateTm(millis2Timestamp(Utils.getCurrentMillis()));
        strategyMapper.insertStrategy(strategyDO);
        LOGGER.info("增加新的时间策略成功: " + strategyDO.getId());
        return strategyDO.getId();
    }

    /**
     * 删除时间策略
     *
     * @param sid 策略ID
     * @return 是否成功
     */
    boolean deleteStrategy(Long sid) {
        TimeStrategyDO strategyDO = new TimeStrategyDO();
        LOGGER.info("删除时间策略: " + sid);
        strategyDO.setId(sid);
        strategyDO.setStatus(LiveConst.STATUS_STRATEGY_DEL);
        boolean isSuccess = updateStrategy(sid, strategyDO);
        if(isSuccess) {
            LOGGER.info("删除时间策略成功ID: " + sid);
        } else {
            LOGGER.info("删除时间策略失败ID: " + sid);
        }
        return isSuccess;
    }

    /**
     * 更新时间策略
     *
     * @param sid        策略ID
     * @param strategyDO 策略实例
     * @return 是否成功
     */
    boolean updateStrategy(Long sid, TimeStrategyDO strategyDO) {
        strategyDO.setModifiedTm(millis2Timestamp(Utils.getCurrentMillis()));
        strategyDO.setId(sid);
        LOGGER.info("更新时间策略内容为: " + strategyDO);
        boolean isSuccess = strategyMapper.updateStrategy(strategyDO) > 0;
        if(isSuccess) {
            LOGGER.info("更新时间策略成功ID: " + sid);
        } else {
            LOGGER.info("更新时间策略失败ID: " + sid);
        }
        return isSuccess;
    }

    /**
     * 获取时间策略实例
     *
     * @param sid 策略ID
     * @return 策略信息
     */
    public TimeStrategyDTO getStrategy(Long sid) {
        TimeStrategyPO timeStrategyPO = strategyMapper.getStrategy(sid);
        if(timeStrategyPO == null) {
            throw LiveException.failure("获取时间策略失败");
        }
        return new TimeStrategyDTO(timeStrategyPO);
    }

    /**
     * 根据时间策略计算下次预计开始时间
     *
     * @param time     起始时间
     * @param strategy 时间策略
     */
    Timestamp calculateNextTime(TimeStrategyDO strategy, Timestamp time) {
        Long nextPreStartTmLong;
        //根据时间策略来计算下次时间
        switch(strategy.getType()) {
            case LiveConst.TYPE_STRATEGY_DAILY: {
                //日播
                nextPreStartTmLong = timestamp2Millis(time) + TimeUnit.SECONDS.toMillis(LiveConst.ONE_DAY_SECONDS);
                break;
            }
            case LiveConst.TYPE_STRATEGY_WEEKLY: {
                //周播
                nextPreStartTmLong = timestamp2Millis(time) + TimeUnit.SECONDS.toMillis(LiveConst.ONE_WEEK_SECONDS);
                break;
            }
            default:
                return null;
        }
        Timestamp nextPreStartTm = millis2Timestamp(nextPreStartTmLong);
        Timestamp periodEndTm    = new Timestamp(timestamp2Millis(strategy.getPeriodEndTm()) + TimeUnit.SECONDS.toMillis(LiveConst.ONE_DAY_SECONDS));
        LOGGER.info("计算初试时间为: " + time + "计算后时间为: " + nextPreStartTm);
        if(nextPreStartTm.after(periodEndTm)) {
            return null;
        }
        return nextPreStartTm;
    }

}

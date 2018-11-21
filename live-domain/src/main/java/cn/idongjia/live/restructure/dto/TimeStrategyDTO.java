package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveRoomPO;
import cn.idongjia.live.db.mybatis.po.TimeStrategyPO;
import cn.idongjia.live.restructure.domain.entity.room.LiveRoom;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/7.
 */
public class TimeStrategyDTO extends BaseDTO<TimeStrategyPO> {
    public TimeStrategyDTO(TimeStrategyPO timeStrategyPO) {
        super(timeStrategyPO);
    }



    public Long getId() {
        return entity.getId();
    }

    public Integer getType() {
        return entity.getType();
    }

    public Long getPeriodStartTime() {
        return assembleTime(entity.getPeriodStartTime());
    }

    public Long getPeriodEndTime() {
        return assembleTime(entity.getPeriodEndTime());
    }

    public Integer getStatus() {
        return entity.getStatus();
    }

    public Long getCreateTime() {
        return assembleTime(entity.getCreateTime());
    }

    public Long getModifiedTime() {
        return assembleTime(entity.getModifiedTime());
    }



}

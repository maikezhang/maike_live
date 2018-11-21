package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveRoomPO;
import cn.idongjia.live.restructure.domain.entity.room.LiveRoom;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/7.
 */
public class LiveRoomDTO extends BaseDTO<LiveRoomPO> {
    public LiveRoomDTO(LiveRoomPO liveRoomPO) {
        super(liveRoomPO);
    }

    public void buildFromReq(LiveRoom liveRoom) {
        entity = new LiveRoomPO();
        entity.setCloudId(liveRoom.getCloudId());
        entity.setCloudType(liveRoom.getCloudType());
        entity.setId(liveRoom.getId());
        entity.setStatus(liveRoom.getStatus());
        entity.setUserId(liveRoom.getUid());
    }

    public int getCloudType() {
        return entity.getCloudType();
    }

    public String getCloudId() {
        return entity.getCloudId();
    }

    public Long getId() {
        return entity.getId();
    }

    public Long getUserId() {
        return entity.getUserId();
    }

    public Long getCreateTime() {
        return assembleTime(entity.getCreateTime());
    }

    public Long getModifiedTime() {
        return assembleTime(entity.getModifiedTime());
    }


    public Integer getStatus() {
        return entity.getStatus();
    }
}

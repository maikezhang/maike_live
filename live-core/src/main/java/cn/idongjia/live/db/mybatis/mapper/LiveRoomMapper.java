package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveRoomPO;
import cn.idongjia.live.db.mybatis.query.DBLiveRoomQuery;

import java.util.List;

public interface LiveRoomMapper {

    int insert(LiveRoomPO liveRoomPO);

    List<LiveRoomPO> list(DBLiveRoomQuery dbLiveRoomQuery);

    LiveRoomPO get(Long id);
}

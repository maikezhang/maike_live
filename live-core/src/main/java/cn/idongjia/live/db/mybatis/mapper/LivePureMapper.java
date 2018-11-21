package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LivePurePO;
import cn.idongjia.live.db.mybatis.query.DBLivePureQuery;

import java.util.List;

public interface LivePureMapper {
    int insert(LivePurePO livePurePO);

    int update(LivePurePO livePurePO);

    int count(DBLivePureQuery dbLivePureQuery);

    List<LivePurePO> list(DBLivePureQuery dbLivePureQuery);

    LivePurePO get(Long liveId);

    int countOpeningPureLives(Long anchorId);

    List<Long> searchOverPureLives(Long anchorId);
}

package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveAuctionSessionPO;
import cn.idongjia.live.db.mybatis.po.LiveShow4IndexPO;
import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface LiveShowMapper {

    void insert(LiveShowPO liveShowPO);

    long update(@Param("po") LiveShowPO liveShowPO, @Param("newUpdateTime") Long newUpdateTime);


    LiveShowPO get(long id);

    List<LiveShowPO> countByUpdateTime(DBLiveShowQuery dbLiveShowQuery);

    List<LiveShowPO> list(DBLiveShowQuery dbLiveShowQuery);

    long restRealTime(Long id);

    LiveShowPO getByUidEndAfterNow(Long uid, Timestamp timestamp);

    List<LiveShowPO> countByTime(Timestamp preViewTime, Timestamp preEndTm, Long uid);

    List<LiveShow4IndexPO> listForIndex(@Param("states")List<Integer> states, @Param("ids") List<Long> ids,@Param("updateTime") Long updateTime, @Param("offset") Integer offset, @Param("limit") Integer limit);




    Long countForIndex(List<Long> ids, Long updateTime);


    LiveShowPO getLiveShowByUidValid(Long uid, Timestamp timestamp);


    Integer count(DBLiveShowQuery dbLiveShowQuery);

    /**
     * 获取时间冲突直播列表
     * @param preViewTime 预展时间
     * @param preEndTm 预计结束时间
     * @param uid 主播id
     * @return 直播列表
     */
    List<LiveShowPO> countOverlapByPreViewTime(Long preViewTime, Long preEndTm, Long uid);

    List<LiveShowPO> countOverlapByPreStartTime(@Param("preStartTime")Long preStartTime, @Param("preEndTm")Long preEndTm, @Param("uid")Long uid,@Param("liveId")Long liveId);

    long updateForIndexUV(@Param("modifyTime") Timestamp timestamp,@Param("state") int code);

    int resetStartTimeAndEndTime(Long liveId);

    List<LiveAuctionSessionPO> getLiveSessionInfo(@Param("limit") Integer limit,@Param("offset") Integer offset);
}

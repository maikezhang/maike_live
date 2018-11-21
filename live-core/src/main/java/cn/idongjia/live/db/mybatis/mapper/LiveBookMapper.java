package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveBookCountPO;
import cn.idongjia.live.db.mybatis.po.LiveBookPO;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiveBookMapper {

    int insert(LiveBookPO pureLiveBookDO);

    int update(@Param("po") LiveBookPO pureLiveBookDO, @Param("newUpdateTime") long newUpdateTime);

    List<LiveBookPO> list(DBLiveBookQuery dbLiveBookQuery);

    Integer count(DBLiveBookQuery dbLiveBookQuery);

    List<Long> searchPureLives(DBLiveBookQuery dbLiveBookQuery);

    int batchAddLiveBook(@Param("pos") List<LiveBookPO> pos);

    int batchDeletePureLiveBook(List<LiveBookPO> pos,@Param("newUpdateTime") long newUpdateTime);

    List<LiveBookCountPO> countGroup(@Param("liveIds") List<Long> liveIds);
}

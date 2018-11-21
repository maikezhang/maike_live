package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveTagPO;
import cn.idongjia.live.db.mybatis.query.DBLiveTagQuery;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiveTagMapper {

    int insert(LiveTagPO liveTagPO);
    int update(@Param("po") LiveTagPO liveTagPO, @Param("newUpdateTime") long newUpdateTime);

    List<LiveTagPO> list(DBLiveTagQuery dbPureLiveTagQuery);

    int count(DBLiveTagQuery dbPureLiveTagQuery);

    LiveTagPO getById(Long tagId);
}

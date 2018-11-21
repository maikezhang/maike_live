package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveTagRelPO;
import cn.idongjia.live.db.mybatis.query.DBLiveTagQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagRelQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiveTagRelMapper {
    int insert(LiveTagRelPO liveTagRelPO);
    int update(@Param("po") LiveTagRelPO liveTagRelPO, @Param("newUpdateTime") long newUpdateTime);
    List<LiveTagRelPO> list(DBLiveTagRelQuery dbLiveTagRelQuery);
    int count(DBLiveTagRelQuery dbLiveTagRelQuery);

}

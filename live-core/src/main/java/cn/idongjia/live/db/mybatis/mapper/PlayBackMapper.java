package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.PlayBackPO;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayBackMapper {

    int insert(PlayBackPO playBackPO);

    int update(@Param("po") PlayBackPO playBackPO, @Param("newUpdateTime") long newUpdateTime);

    List<PlayBackPO> list(DBPlayBackQuery dbPlayBackCoverQuery);

    int count(DBPlayBackQuery dbPlayBackQuery);
}

package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.VideoCoverPO;
import cn.idongjia.live.db.mybatis.query.DBLiveVideoCoverQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 岳晓东 on 2017/12/22.
 */
public interface VideoCoverMapper {

    void add(VideoCoverPO videoCoverPO);

    void update(@Param("po") VideoCoverPO videoCoverPO, @Param("newUpdateTime") long newUpdateTime);

    List<VideoCoverPO> getByLiveId(long liveId);

    List<VideoCoverPO> list(DBLiveVideoCoverQuery dbLiveVideoCoverQuery);

    void updateVideoCoverLiveId(@Param("po") VideoCoverPO po,@Param("newUpdateTime") long newUpdateTime) ;

    void delete(Long liveId);

    VideoCoverPO get(Long id);
}

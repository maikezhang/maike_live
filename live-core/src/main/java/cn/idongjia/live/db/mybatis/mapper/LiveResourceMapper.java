package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveResourceCountPO;
import cn.idongjia.live.db.mybatis.po.LiveResourcePO;
import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiveResourceMapper {


    /**
     * 批量添加直播资源
     * @param liveResourcePOS 资源数据
     * @return insert 条数
     */
    int batchInsert(@Param("liveResources") List<LiveResourcePO> liveResourcePOS);

    int insert(LiveResourcePO liveResourcePO);

    /**
     * 批量删除
     * @param po
     * @return
     */
    int deleteResource(@Param("po") LiveResourcePO po,@Param("newUpdateTime") long newUpdateTime);

    int updateWeight(@Param("po") LiveResourcePO po,@Param("newUpdateTime") long newUpdateTime);


    /**
     * 更新直播资源
     * @param liveResourcePO 直播资源
     * @return 更新数量
     */
    int update(LiveResourcePO liveResourcePO,@Param("newUpdateTime") long newUpdateTime);


    List<LiveResourcePO> list(DBLiveResourceQuery dbLiveResourceQuery);

    /**
     * @param dbLiveResourceQuery
     * @return
     */
    List<LiveResourceCountPO> countGroup(DBLiveResourceQuery dbLiveResourceQuery);

    int deleteAllByLiveId(@Param("liveId") Long liveId,@Param("newUpdateTime") long newUpdateTime);



}

package cn.idongjia.live.db.mybatis.mapper;


import cn.idongjia.live.db.mybatis.po.DLiveCloudPO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Frank
 */
public interface DLiveCloudMapper {

    int insert(DLiveCloudPO dLiveCloudPO);

    DLiveCloudPO getByUid(Long uid);

    int update(DLiveCloudPO dLiveCloudDO, @Param("newModifyDate") Long newModifyDate);

    DLiveCloudPO getById(Long id);

}

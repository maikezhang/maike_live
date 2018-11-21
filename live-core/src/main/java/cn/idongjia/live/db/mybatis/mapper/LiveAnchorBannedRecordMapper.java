package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.AnchorBannedRecordPO;
import cn.idongjia.live.v2.pojo.query.LiveAnchorBanRecordQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：主播禁播记录表 db接口
 *
 * @author YueXiaodong, yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
public interface LiveAnchorBannedRecordMapper {

    int insert(AnchorBannedRecordPO anchorBannedRecordPO);


    int deleteByPrimaryKey(@Param("id") Long id);


    AnchorBannedRecordPO getByPrimaryKey(@Param("id") Long id);


    Integer count(LiveAnchorBanRecordQuery query);


    List<AnchorBannedRecordPO> select(LiveAnchorBanRecordQuery query);

    /**
     * 连表查询
     *
     * @param query
     * @return
     */
    List<AnchorBannedRecordPO> selectWithAnchor(LiveAnchorBanRecordQuery query);

    /**
     * @param query
     * @return
     */
    Integer countWithAnchor(LiveAnchorBanRecordQuery query);
}

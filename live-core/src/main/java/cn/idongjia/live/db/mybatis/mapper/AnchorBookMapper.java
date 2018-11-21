package cn.idongjia.live.db.mybatis.mapper;


import cn.idongjia.live.db.mybatis.po.AnchorBookPO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * 订阅mapper
 * @author Frank
 */
public interface AnchorBookMapper {
    /**
     * 添加定义记录
     * @param anchorsBookDO
     */
    void insert(AnchorBookPO anchorsBookDO);

    /**
     * 修改定义记录
     * @param anchorBookPO
     * @return
     */
    int update(@Param("po") AnchorBookPO anchorBookPO, @Param("newModifyTime") Long newModifyTime);

    /**
     * 查询订阅
     * @param craftsBookSearch
     * @return
     */
    List<AnchorBookPO> list(DBAnchorBookQuery craftsBookSearch);

    /**
     * 修改订阅
     * @param craftsBookSearch
     * @return
     */
    int count(DBAnchorBookQuery craftsBookSearch);

    /**
     * 查询一条订阅记录
     * @param uid 用户id
     * @param anchorId 主播id
     * @return 主播订阅数据
     */
    AnchorBookPO getByUidAnchorId(Long uid, Long anchorId);
}

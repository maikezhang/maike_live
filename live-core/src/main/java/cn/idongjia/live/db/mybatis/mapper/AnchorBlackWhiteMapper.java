package cn.idongjia.live.db.mybatis.mapper;


import cn.idongjia.live.db.mybatis.po.AnchorBlackWhitePO;
import cn.idongjia.live.db.mybatis.po.AnchorBookPO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBlackWhiteQuery;
import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * 订阅mapper
 * @author Frank
 */
public interface AnchorBlackWhiteMapper {
    /**
     * 添加
     * @param anchorBlackWhitePO
     */
    void insert(AnchorBlackWhitePO anchorBlackWhitePO);


    /**
     * 批量添加
     * @param pos
     */
    void batchInsert(@Param("pos") List<AnchorBlackWhitePO> pos);

    /**
     * 更新
     * @param anchorBlackWhitePO
     * @param newModifyTime
     * @return
     */
    int update(@Param("po") AnchorBlackWhitePO anchorBlackWhitePO, @Param("newModifyTime") Long newModifyTime);

    List<AnchorBlackWhitePO> list(DBAnchorBlackWhiteQuery query);


    int delete(Long id);

    AnchorBlackWhitePO get(long anchorId);

}

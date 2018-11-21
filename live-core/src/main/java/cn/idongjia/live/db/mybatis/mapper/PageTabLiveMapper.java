package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.PageTabLivePO;
import cn.idongjia.live.db.mybatis.po.PageTabPO;
import cn.idongjia.live.db.mybatis.query.DBPageTabLiveQuery;
import cn.idongjia.live.db.mybatis.query.DBPageTabQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 页面tabmapper
 *
 * @author lc
 * @create at 2018/7/6.
 */
public interface PageTabLiveMapper {
    /**
     * 插入页面tab
     * @param pageTabLivePOS
     * @return
     */
    Integer batchInsert(@Param("pageTabLivePOS") List<PageTabLivePO> pageTabLivePOS);

    /**
     * 修改页面tab
     */
    Integer update(@Param("po") PageTabLivePO pageTabLivePO, @Param("newUpdateTime") Long updateTime);

    /**
     * 批量查询
     * @return
     */
    List<PageTabLivePO> list(DBPageTabLiveQuery dbPageTabQuery);


    Integer count(DBPageTabLiveQuery dbPageTabQuery);

    PageTabLivePO get(long id);

    int delete(Long id);

    List<PageTabLivePO> getPageTabLives(DBPageTabLiveQuery dbPageTabQuery);
}

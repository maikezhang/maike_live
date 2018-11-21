package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.PageTabPO;
import cn.idongjia.live.db.mybatis.query.DBPageTabQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 页面tabmapper
 *
 * @author lc
 * @create at 2018/7/6.
 */
public interface PageTabMapper {
    /**
     * 插入页面tab
     * @param pageTabPO
     * @return
     */
    Integer insert(PageTabPO pageTabPO);

    /**
     * 修改页面tab
     */
    Integer update(@Param("po") PageTabPO  pageTabPO,@Param("newUpdateTime") Long updateTime);

    /**
     * 批量查询
     * @return
     */
    List<PageTabPO> list(DBPageTabQuery dbPageTabQuery);


    Integer count(DBPageTabQuery dbPageTabQuery);

    PageTabPO get(long id);

}

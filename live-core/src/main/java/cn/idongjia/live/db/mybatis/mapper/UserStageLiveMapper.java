package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.UserStageLivePO;
import cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lc on 2018/7/7.
 * @class cn.idongjia.live.db.mybatis.mapper.UserStageLiveMapper
 */
public interface UserStageLiveMapper {
    /**
     * 添加新老用户强运营数据
     *
     * @param userStageLivePO 运营数据
     * @return 更新成功条数
     */
    Integer insert(UserStageLivePO userStageLivePO);

    /**
     * 更新运营数据
     *
     * @param userStageLivePO 运营数据
     * @param updateTime      更新时间
     * @return 更新成功条数
     */
    Integer update(@Param("po") UserStageLivePO userStageLivePO, @Param("newUpdateTime") Long updateTime);


    /**
     * 新老用户直播强运营数据查询
     *
     * @param dbUserStageLiveQuery 查询条件
     * @return 强运营数据列表
     * @see cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery
     */
    List<UserStageLivePO> list(DBUserStageLiveQuery dbUserStageLiveQuery);

    /**
     * 新老用户直播强运营数据统计
     */
    Integer count(DBUserStageLiveQuery dbUserStageLiveQuery);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    UserStageLivePO get(long id);

    Integer batchInsert(@Param("userStageLivePOS") List<UserStageLivePO> userStageLivePOS);
}

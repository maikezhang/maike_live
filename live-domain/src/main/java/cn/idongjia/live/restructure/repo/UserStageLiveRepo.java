package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.UserStageLiveMapper;
import cn.idongjia.live.db.mybatis.po.UserStageLivePO;
import cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery;
import cn.idongjia.live.restructure.convert.UserStageLiveConvertor;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 直播新老用户强运营数据
 *
 * @author lc
 * @create at 2018/7/7.
 */
@Repository
public class UserStageLiveRepo {

    @Resource
    private UserStageLiveMapper userStageLiveMapper;


    @Resource
    private UserStageLiveConvertor userStageLiveConvertor;

    /**
     * 添加新老用户强运营直播数据
     *
     * @param userStageLiveE
     * @return
     */
    public Integer add(UserStageLiveE userStageLiveE) {
        UserStageLivePO userStageLivePO = userStageLiveConvertor.entityToData(userStageLiveE);
        if (userStageLivePO == null) {
            return 0;
        }
        Integer insert = userStageLiveMapper.insert(userStageLivePO);
        if (insert > 0) {
            userStageLiveE.setId(userStageLivePO.getId());
        }
        return insert;
    }

    public Integer update(UserStageLiveE userStageLiveE) {
        UserStageLivePO userStageLivePO = userStageLiveConvertor.entityToData(userStageLiveE);
        return userStageLiveMapper.update(userStageLivePO, Utils.getCurrentMillis());
    }

    public List<UserStageLiveE> list(DBUserStageLiveQuery dbUserStageLiveQuery) {
        List<UserStageLivePO> userStageLivePOS = userStageLiveMapper.list(dbUserStageLiveQuery);
        if (Utils.isEmpty(userStageLivePOS)) {
            return Collections.emptyList();
        }
        return userStageLivePOS.stream().map(userStageLivePO -> {
            return userStageLiveConvertor.dataToEntity(userStageLivePO);
        }).collect(Collectors.toList());
    }

    public Optional<UserStageLiveE> get(long id) {
        UserStageLivePO userStageLivePO = userStageLiveMapper.get(id);
        UserStageLiveE userStageLiveE = userStageLiveConvertor.dataToEntity(userStageLivePO);
        return Optional.ofNullable(userStageLiveE);
    }

    public Integer count(DBUserStageLiveQuery dbUserStageLiveQuery) {
        return userStageLiveMapper.count(dbUserStageLiveQuery);
    }

    public int batchAdd(List<UserStageLivePO> userStageLivePOS) {
        return userStageLiveMapper.batchInsert(userStageLivePOS);

    }
}

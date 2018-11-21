package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveModulePO;
import cn.idongjia.live.db.mybatis.query.DBLiveModuleQuery;

import java.util.List;

public interface LiveModuleMapper {
    int insert(LiveModulePO liveModulePO);

    int update(LiveModulePO liveModulePO);

    int delete(Long id);

    List<LiveModulePO> search(DBLiveModuleQuery query);

    int countModule(DBLiveModuleQuery query);
}

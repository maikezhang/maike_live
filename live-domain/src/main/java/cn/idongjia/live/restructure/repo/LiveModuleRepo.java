package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.LiveModuleMapper;
import cn.idongjia.live.db.mybatis.po.LiveModulePO;
import cn.idongjia.live.db.mybatis.query.DBLiveModuleQuery;
import cn.idongjia.live.restructure.dto.LiveModuleDTO;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LiveModuleRepo {
    private static final Log LOG= LogFactory.getLog(LiveModuleRepo.class);
    @Autowired
    private LiveModuleMapper liveModuleMapper;

    public Long saveModule(LiveModuleDTO liveModuleDTO){
        liveModuleMapper.insert(liveModuleDTO.toDO());
        return liveModuleDTO.getId();
    }

    public boolean updateModule(LiveModuleDTO liveModuleDTO){
       return liveModuleMapper.update(liveModuleDTO.toDO())>0;
    }

    public boolean deleteModule(Long mid){
       return liveModuleMapper.delete(mid)>0;
    }

    public int count(DBLiveModuleQuery dbLiveModuleQuery){
        return liveModuleMapper.countModule(dbLiveModuleQuery);
    }

    public List<LiveModuleDTO> modules(DBLiveModuleQuery dbLiveModuleQuery){
        List<LiveModulePO> modulePOS=liveModuleMapper.search(dbLiveModuleQuery);
        return modulePOS.stream().map(LiveModuleDTO::new).collect(Collectors.toList());
    }
}

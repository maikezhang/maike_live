package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBLiveModuleQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.query.live.LiveModuleSearch;
import cn.idongjia.live.restructure.dto.LiveModuleDTO;
import cn.idongjia.live.restructure.repo.LiveModuleRepo;
import cn.idongjia.live.support.enumeration.ModuleState;
import cn.idongjia.live.support.enumeration.ModuleStatus;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class LiveModuleQueryHander {
    private static final Log LOGGER = LogFactory.getLog(LiveModuleQueryHander.class);
    @Autowired
    private LiveModuleRepo liveModuleRepo;

    @Async
    public Future<List<LiveModuleDTO>> list(DBLiveModuleQuery dbLiveModuleQuery) {
        List<LiveModuleDTO> liveModuleDTOS = liveModuleRepo.modules(dbLiveModuleQuery);
        return new AsyncResult<>(liveModuleDTOS);

    }

    public Integer count(DBLiveModuleQuery dbLiveModuleQuery) {
        return liveModuleRepo.count(dbLiveModuleQuery);
    }

    public List<LiveModule> getOnShelfModule(){
        LiveModuleSearch search=new LiveModuleSearch();
        search.setState(ModuleState.IN_PROGRESS.getCode());
        search.setStatus(ModuleStatus.ON.getCode());
        return search(search);
    }

    public List<LiveModule> search(LiveModuleSearch search){
        if(Utils.isEmpty(search.getOrderBy())){
            search.setOrderBy("create_time desc");
        }
        DBLiveModuleQuery dbLiveModuleQuery= QueryFactory.getInstance(search);
        try {
            List<LiveModuleDTO> liveModuleDTOS= list(dbLiveModuleQuery).get();
            return liveModuleDTOS.stream().map(b -> {
                return b.po2LiveModule(b.toDO());
            }).collect(Collectors.toList());
        }  catch (Exception e) {
            LOGGER.error("查询banner失败{}", e);
            throw LiveException.failure("查询banner失败");
        }

    }

    public List<LiveModule> getModuleByPosition(Integer position,LiveModuleSearch search){
        search.setPosition(position);
        return search(search);
    }

    public List<LiveModule> getModuleByStyle(Integer style){
        if(style==0){
            return Collections.EMPTY_LIST;
        }
        LiveModuleSearch search=new LiveModuleSearch();
        List<Integer> positions=new ArrayList<>();
        for(int i=1; i<=style;i++){
            positions.add(i);
        }
        search.setPositions(positions);
        search.setState(ModuleState.IN_PROGRESS.getCode());
        List<LiveModule> liveModules=search(search);
        if(!Utils.isEmpty(liveModules)){
            liveModules.sort(Comparator.comparingInt(LiveModule::getPosition));
        }
        return liveModules;
    }

    public LiveModule getModuleById(Long lmid){
        LiveModuleSearch search=new LiveModuleSearch();
        search.setId(lmid);
        List<LiveModule> moduleList=search(search);
        if(!Utils.isEmpty(moduleList)){
            return moduleList.get(0);
        }
        return null;
    }


    public LiveModule getPositionOnModule(LiveModuleSearch search){
        List<LiveModule> liveModules=search(search);
        if(!Utils.isEmpty(liveModules)){
            return liveModules.get(0);
        }
        return null;
    }

    public boolean isExistStarttime(Integer position,Long startTime){
        return isExistStarttime(null,position,startTime);
    }

    public boolean isExistStarttime(Long lmid,Integer position,Long startTime){
        LiveModuleSearch search=new LiveModuleSearch();
        search.setPosition(position);
        search.setStartTime(startTime);
        List<LiveModule> liveModules=search(search);
        Set<Long> idSet=liveModules.stream().filter(r->(null==lmid) || (null !=lmid && r.getId() !=lmid)).map(LiveModule::getId).collect(Collectors.toSet());
        return !Utils.isEmpty(idSet);
    }


}

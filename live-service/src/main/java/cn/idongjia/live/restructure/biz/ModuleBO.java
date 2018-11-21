package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.db.mybatis.po.LiveModulePO;
import cn.idongjia.live.db.mybatis.query.DBLiveModuleQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.query.live.LiveModuleSearch;
import cn.idongjia.live.restructure.cache.liveHomePage.HPModuleCache;
import cn.idongjia.live.restructure.dto.LiveModuleDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.TaskManager;
import cn.idongjia.live.restructure.query.LiveModuleQueryHander;
import cn.idongjia.live.restructure.query.QueryFactory;
import cn.idongjia.live.restructure.repo.LiveModuleRepo;
import cn.idongjia.live.support.enumeration.ModuleState;
import cn.idongjia.live.support.enumeration.ModuleStatus;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.apache.avro.generic.GenericData;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModuleBO {
    private static final Log LOGGER= LogFactory.getLog(ModuleBO.class);

    @Resource
    private LiveModuleRepo liveModuleRepo;

    @Resource
    private LiveModuleQueryHander liveModuleQueryHander;

    @Resource
    private TaskManager taskManager;

    @Resource
    private HPModuleCache hpModuleCache;

    public Long addModule(LiveModule liveModule){
        if(liveModuleQueryHander.isExistStarttime(liveModule.getPosition(),liveModule.getStartTime())){
            throw new LiveException("已有相同生效时间的资源了");
        }
        LiveModuleDTO liveModuleDTO=new LiveModuleDTO(new LiveModulePO());
        liveModule.setState(ModuleState.NOT_BEGIN.getCode());
        liveModule.setStatus(ModuleStatus.OFF.getCode());
        liveModule.setWeight(0);
        liveModuleDTO.buildFromReq(liveModule);
        Long id= liveModuleRepo.saveModule(liveModuleDTO);
        try {
            taskManager.releaseLiveModuleTask(id, liveModule.getPosition(), liveModule.getStartTime());
        }catch (Exception e){
            LOGGER.error("创建模块定时任务失败",e);
        }
        return id;
    }

    public boolean updateModule(Long lmid,LiveModule liveModule){
        liveModule.setId(lmid);
        LiveModule oldModule=liveModuleQueryHander.getModuleById(lmid);
        if(oldModule.getState()==ModuleState.IN_PROGRESS.getCode()){
            throw new LiveException("进行中的资源不能更新");
        }
        if(liveModuleQueryHander.isExistStarttime(lmid,liveModule.getPosition(),liveModule.getStartTime())){
            throw new LiveException("已有相同生效时间的资源了");
        }
       boolean result= updateModule(liveModule);
       taskManager.updateLiveModuleTask(lmid,liveModule.getPosition(),liveModule.getStartTime());
        try {
            Boolean isVanishRedis=hpModuleCache.vanishRedis();
            if(isVanishRedis){
                LOGGER.info("删除首页缓存的模块数据成功");
            }
        }catch (Exception e){
            LOGGER.warn("删除首页缓存的模块数据失败，exception:{}",e);
        }

       return result;
    }

    public boolean updateModule(LiveModule liveModule){
        LiveModuleDTO liveModuleDTO=new LiveModuleDTO(new LiveModulePO());
        liveModuleDTO.buildFromReq(liveModule);
        boolean b = liveModuleRepo.updateModule(liveModuleDTO);

        try {
            Boolean isVanishRedis=hpModuleCache.vanishRedis();
            if(isVanishRedis){
                LOGGER.info("删除首页缓存的模块数据成功");
            }
        }catch (Exception e){
            LOGGER.warn("删除首页缓存的模块数据失败，exception:{}",e);
        }

        return b;
    }

    public boolean deleteModule(Long lmid){

        LiveModuleSearch search=new LiveModuleSearch();
        search.setId(lmid);
        search.setState(ModuleState.IN_PROGRESS.getCode());
        List<LiveModule> liveModules=liveModuleQueryHander.search(search);
        if(!Utils.isEmpty(liveModules)){
            throw new LiveException("进行中资源不能被删除");
        }
        boolean b = liveModuleRepo.deleteModule(lmid);

        try {
            Boolean isVanishRedis=hpModuleCache.vanishRedis();
            if(isVanishRedis){
                LOGGER.info("删除首页缓存的模块数据成功");
            }
        }catch (Exception e){
            LOGGER.warn("删除首页缓存的模块数据失败，exception:{}",e);
        }

        return b;
    }

    public List<LiveModule> search(LiveModuleSearch search){
        if(Utils.isEmpty(search.getOrderBy())){
            search.setOrderBy("create_time desc");
        }
        DBLiveModuleQuery dbLiveModuleQuery= QueryFactory.getInstance(search);
        try {
              List<LiveModuleDTO> liveModuleDTOS= liveModuleQueryHander.list(dbLiveModuleQuery).get();
            return liveModuleDTOS.stream().map(b -> {
                return b.po2LiveModule(b.toDO());
            }).collect(Collectors.toList());
        }  catch (Exception e) {
            LOGGER.error("查询banner失败{}", e);
            throw LiveException.failure("查询banner失败");
        }

    }

    public int count(LiveModuleSearch search){
        DBLiveModuleQuery dbLiveModuleQuery= QueryFactory.getInstance(search);
        try {

            return liveModuleQueryHander.count(dbLiveModuleQuery);
        }  catch (Exception e) {
            LOGGER.error("查询模块数量失败{}", e);
            throw LiveException.failure("查询模块数量失败");
        }

    }


    @Transactional
    public boolean onShelfModuleByStyle(Integer style){

        LiveModuleSearch search=new LiveModuleSearch();
        search.setState(ModuleState.IN_PROGRESS.getCode());
        List<LiveModule> moduleList=new ArrayList<>();
        List<LiveModule> allProgress=liveModuleQueryHander.search(search);
        for(int i=1;i<=style;i++){
            search.setPosition(i);
            LiveModule liveModule =liveModuleQueryHander.getPositionOnModule(search);
            if(null !=liveModule){
                moduleList.add(liveModule);
            }
        }
        if(moduleList.size()<style){
            throw new LiveException("上线模块资源不足");
        }
        moduleList.forEach(liveModule -> {
            liveModule.setStatus(ModuleStatus.ON.getCode());
            updateModule(liveModule);
        });
        //下线剩余所有位置数据
        if(!Utils.isEmpty(allProgress) && allProgress.size()>style){
                allProgress.stream().filter(r->r.getPosition()>style).forEach(liveModule -> {
                    liveModule.setStatus(ModuleStatus.OFF.getCode());
                    updateModule(liveModule);
                });
        }
        try {
            Boolean isVanishRedis=hpModuleCache.vanishRedis();
            if(isVanishRedis){
                LOGGER.info("删除首页缓存的模块数据成功");
            }
        }catch (Exception e){
            LOGGER.warn("删除首页缓存的模块数据失败，exception:{}",e);
        }
        return true;
    }





}

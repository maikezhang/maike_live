package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.ModuleService;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.query.live.LiveModuleSearch;
import cn.idongjia.live.restructure.biz.ModuleBO;
import cn.idongjia.live.restructure.query.LiveModuleQueryHander;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("moduleServiceImpl")
public class ModuleServiceImpl implements ModuleService {

    @Resource
    ModuleBO moduleBO;

    @Resource
    private LiveModuleQueryHander liveModuleQueryHander;

    @Override
    public Long createModule(LiveModule liveModule) {

        return moduleBO.addModule(liveModule);
    }

    @Override
    public boolean modifyModule(Long id, LiveModule liveModule) {

        return moduleBO.updateModule(id,liveModule);
    }

    @Override
    public boolean deleteModule(Long mid) {

        return moduleBO.deleteModule(mid);
    }

    @Override
    public LiveModule getModuleById(Long id) {
        return liveModuleQueryHander.getModuleById(id);
    }

    @Override
    public List<LiveModule> listModuleGroupByStyle(Integer style) {

        return liveModuleQueryHander.getModuleByStyle(style);
    }

    @Override
    public boolean onShelfModuleGroup(Integer style) {

        return moduleBO.onShelfModuleByStyle(style);
    }

    @Override
    public List<LiveModule> listLiveModuleByPosition(Integer position, LiveModuleSearch search) {

        return liveModuleQueryHander.getModuleByPosition(position,search);
    }

    @Override
    public List<LiveModule> listModuleGroup() {

        return liveModuleQueryHander.getOnShelfModule();
    }

    @Override
    public boolean isDuplicateStartTime(Integer position, Long startTime) {

        return liveModuleQueryHander.isExistStarttime(position,startTime);
    }

    @Override
    public int countLiveModuleByPosition(Integer position, LiveModuleSearch search) {
        return moduleBO.count(search);


    }
}

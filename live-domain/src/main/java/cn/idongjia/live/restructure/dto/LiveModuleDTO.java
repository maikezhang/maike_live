package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveModulePO;
import cn.idongjia.live.pojo.homePage.LiveModuleCO;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.util.Utils;

public class LiveModuleDTO extends BaseDTO<LiveModulePO> {
    public LiveModuleDTO(LiveModulePO entity) {
        super(entity);
    }

    public Long getId(){
        return entity.getId();
    }

    public void buildFromReq(LiveModule liveModule){
        entity.setDesc(liveModule.getDesc());
        entity.setId(liveModule.getId());
        entity.setJumpAddr(liveModule.getJumpAddr());
        entity.setJumpType(liveModule.getJumpType());
        entity.setPic(liveModule.getPic());
        entity.setPosition(liveModule.getPosition());
        entity.setStartTime(liveModule.getStartTime());
        entity.setState(liveModule.getState());
        entity.setStatus(liveModule.getStatus());
        entity.setWeight(liveModule.getWeight());
        entity.setCreateTime(Utils.getCurrentMillis());
        entity.setUpdateTime(Utils.getCurrentMillis());
        entity.setTitle(liveModule.getTitle());
        entity.setSubTitle(liveModule.getSubTitle());

    }

    public LiveModule po2LiveModule(LiveModulePO liveModulePO){
        LiveModule liveModule=new LiveModule();
        liveModule.setCreateTime(entity.getCreateTime());
        liveModule.setDesc(entity.getDesc());
        liveModule.setId(entity.getId());
        liveModule.setJumpAddr(entity.getJumpAddr());
        liveModule.setJumpType(entity.getJumpType());
        liveModule.setPic(entity.getPic());
        liveModule.setPosition(entity.getPosition());
        liveModule.setStartTime(entity.getStartTime());
        liveModule.setState(entity.getState());
        liveModule.setStatus(entity.getStatus());
        liveModule.setSubTitle(entity.getSubTitle());
        liveModule.setTitle(entity.getTitle());
        liveModule.setUpdateTime(entity.getUpdateTime());
        liveModule.setWeight(entity.getWeight());
        return liveModule;
    }

    public static LiveModuleCO po2LiveModuleCO(LiveModule liveModule){
        LiveModuleCO liveModuleCO=new LiveModuleCO();
        liveModuleCO.setCreateTime(liveModule.getCreateTime());
        liveModuleCO.setDesc(liveModule.getDesc());
        liveModuleCO.setId(liveModule.getId());
        liveModuleCO.setAddr(liveModule.getJumpAddr());
        liveModuleCO.setType(liveModule.getJumpType());
        liveModuleCO.setCover(liveModule.getPic());
        liveModuleCO.setPosition(liveModule.getPosition());
        liveModuleCO.setStartTime(liveModule.getStartTime());
        liveModuleCO.setState(liveModule.getState());
        liveModuleCO.setStatus(liveModule.getStatus());
        liveModuleCO.setSubTitle(liveModule.getSubTitle());
        liveModuleCO.setTitle(liveModule.getTitle());
        liveModuleCO.setUpdateTime(liveModule.getUpdateTime());
        liveModuleCO.setWeight(liveModule.getWeight());
        return liveModuleCO;
    }
}

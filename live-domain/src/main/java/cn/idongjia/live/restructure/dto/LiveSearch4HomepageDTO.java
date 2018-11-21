package cn.idongjia.live.restructure.dto;

import cn.idongjia.essearch.lib.dto.live.LiveDTO;
import cn.idongjia.live.api.live.pojo.LiveSearchApiResp;

public class LiveSearch4HomepageDTO extends BaseDTO<LiveDTO> {
    public LiveSearch4HomepageDTO(LiveDTO entity) {
        super(entity);
    }

    public Long getLiveId(){
        return entity.getLid();
    }

    public Integer getLiveType(){return entity.getType();}

    public Long getAsid(){return entity.getAsid();}

    public LiveSearchApiResp po2Api(){
        LiveSearchApiResp resp=new LiveSearchApiResp();
        resp.setCover(entity.getCover());
        resp.setCraftsmanAvatar(entity.getAvatar());
        resp.setCraftsmanName(entity.getUsername());
        resp.setHot(null !=entity.getUv()?entity.getUv():0);
        resp.setId(entity.getLid());
        resp.setPlanEndTime(entity.getPlanEndTime());
        resp.setPlanStartTime(entity.getPlanStartTime());
        resp.setState(entity.getState());
        resp.setTitle(entity.getLiveTitle());
        resp.setType(entity.getType());
        resp.setAsid(entity.getAsid());
        return resp;
    }
}

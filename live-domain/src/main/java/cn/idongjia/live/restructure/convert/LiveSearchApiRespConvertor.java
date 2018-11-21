package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.api.live.pojo.LiveSearchApiResp;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/8/22
 * Time: 上午11:38
 */
@Component("liveSearchApiRespConvertor")
public class LiveSearchApiRespConvertor implements ConvertorI<LiveSearchApiResp,LiveEntity,LiveCO> {


    @Override
    public LiveSearchApiResp dataToClient(LiveCO co){
        LiveSearchApiResp resp=new LiveSearchApiResp();
        resp.setTitle(co.getTitle());
        resp.setAsid(co.getSessionId());
        resp.setBookState(co.getBookState());
        resp.setCover(co.getCover());
        resp.setCraftsmanAvatar(co.getCraftsmanAvatar());
        resp.setCraftsmanName(co.getCraftsmanName());
        resp.setHot(co.getHot().intValue());
        resp.setId(co.getId());
//        resp.setPlanEndTime();
        resp.setPlanStartTime(co.getPreStartTime());
        resp.setState(co.getState());
        resp.setType(co.getType());
        return resp;
    }
}

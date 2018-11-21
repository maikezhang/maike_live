package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.db.mybatis.po.PageTabLivePO;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabLiveCO;
import cn.idongjia.live.support.BaseEnum;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Component
public class PageTabLiveConvertor implements ConvertorI<PageTabLiveCO, PageTabLiveE, PageTabLivePO> {

    @Override
    public PageTabLiveE clientToEntity(PageTabLiveCO clientObject) {
        if (clientObject == null) {
            return null;
        }
        PageTabLiveE pageTabLiveV = new PageTabLiveE();
        pageTabLiveV.setId(clientObject.getId());
        pageTabLiveV.setLiveId(clientObject.getLiveId());
        pageTabLiveV.setWeight(clientObject.getWeight());
        return pageTabLiveV;
    }

    @Override
    public PageTabLivePO entityToData(PageTabLiveE entityObject) {
        if(entityObject==null){
            return null;
        }
        PageTabLivePO pageTabLivePO=new PageTabLivePO();
        pageTabLivePO.setCreateTime(entityObject.getCreateTime());
        pageTabLivePO.setId(entityObject.getId());
        pageTabLivePO.setLiveId(entityObject.getLiveId());
        if(!Objects.isNull(entityObject.getShowStatus())){
            pageTabLivePO.setShowStatus(entityObject.getShowStatus().getCode());
        }
        pageTabLivePO.setWeight(entityObject.getWeight());
        pageTabLivePO.setUpdateTime(entityObject.getUpdateTime());
        return pageTabLivePO;
    }

    @Override
    public PageTabLiveE dataToEntity(PageTabLivePO dataObject) {
        if(dataObject==null){
            return null;
        }
        PageTabLiveE pageTabLiveE =new PageTabLiveE();
        pageTabLiveE.setLiveId(dataObject.getLiveId());
        pageTabLiveE.setShowStatus(BaseEnum.parseInt2Enum(dataObject.getShowStatus(),BaseEnum.YesOrNo.values()).orElse(null));
        pageTabLiveE.setWeight(dataObject.getWeight());
        pageTabLiveE.setId(dataObject.getId());
        return pageTabLiveE;
    }

    @Override
    public PageTabLiveCO entityToClient(PageTabLiveE entityObject) {
        if(entityObject==null){
            return null;
        }
        PageTabLiveCO pageTabLiveCO=new PageTabLiveCO();
        pageTabLiveCO.setWeight(entityObject.getWeight());
        pageTabLiveCO.setLiveId(entityObject.getLiveId());
        pageTabLiveCO.setId(entityObject.getId());
        return pageTabLiveCO;
    }
}

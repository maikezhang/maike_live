package cn.idongjia.live.restructure.factory;

import cn.idongjia.live.pojo.purelive.book.AnchorsBookDO;
import cn.idongjia.live.restructure.domain.entity.user.AnchorEntity;
import cn.idongjia.live.support.BaseEnum;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/15
 * Time: 下午4:36
 */
public class AnchorEntityFactory  {

    private AnchorEntityFactory(){

    }

    public static AnchorEntity getInstance(AnchorsBookDO anchorsBookDO){
        AnchorEntity entity=new AnchorEntity();
        entity.setAnchorId(anchorsBookDO.getAnchorId());
        entity.setUid(anchorsBookDO.getUid());
//        entity.setCreateTime(anchorsBookDO.getCreateTm().getTime());
//        entity.setModifiedTime(anchorsBookDO.getModifiedTm().getTime());
        entity.setStatus(BaseEnum.parseInt2Enum(anchorsBookDO.getStatus(),BaseEnum.DataStatus.values()).orElse(null));
        return entity;

    }

}

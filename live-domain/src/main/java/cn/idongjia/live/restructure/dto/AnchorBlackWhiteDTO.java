package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.AnchorBlackWhitePO;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午3:23
 */
public class AnchorBlackWhiteDTO extends BaseDTO<AnchorBlackWhitePO> {
    public AnchorBlackWhiteDTO(AnchorBlackWhitePO entity) {
        super(entity);
    }

    public Long getId(){
        return entity.getId();
    }

    public Long getAnchorId(){
        return entity.getAnchorId();
    }

    public Integer getType(){
        return entity.getType();
    }


    public Long getCreateTime(){
        return entity.getCreateTime();
    }

    public Long getUpdateTime(){
        return entity.getUpdateTime();
    }




}

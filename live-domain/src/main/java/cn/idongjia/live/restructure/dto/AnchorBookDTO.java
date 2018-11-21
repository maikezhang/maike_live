package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.AnchorBookPO;
import cn.idongjia.live.pojo.purelive.book.AnchorsBookDO;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;

import java.sql.Timestamp;


/**
 * Description: 主播订阅
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午10:40
 */
public class AnchorBookDTO extends BaseDTO<AnchorBookPO> {


    public AnchorBookDTO(AnchorBookPO entity) {
        super(entity);
    }

    public Long getId(){
        return entity.getId();
    }
    public Long getAnchorId(){
        return entity.getAnchorId();
    }
    public Integer getStatus(){
        return entity.getStatus();
    }
    public Timestamp getCreateTm(){
        return entity.getCreateTime();
    }
    public Timestamp getModifiedTm(){
        return entity.getModifiedTime();
    }



    public Long getUserId() {
        return entity.getUserId();
    }

}

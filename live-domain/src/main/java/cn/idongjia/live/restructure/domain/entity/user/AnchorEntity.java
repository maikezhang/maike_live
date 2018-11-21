package cn.idongjia.live.restructure.domain.entity.user;

import cn.idongjia.live.db.mybatis.po.AnchorBookPO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.repo.AnchorBookRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/15
 * Time: 下午4:07
 */
@Getter
@Setter
public class AnchorEntity {

    public static final Log LOGGER= LogFactory.getLog(AnchorEntity.class);
    /**
       用户id
     */
    private Long uid;

    /**
     * 主播id
     */
    private Long anchorId;

    /**
     * 状态
     */
    private BaseEnum.DataStatus status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后修改时间
     */
    private Long modifiedTime;

    private final AnchorBookRepo anchorBookRepo= SpringUtils.getBean("anchorBookRepo",AnchorBookRepo.class)
            .orElseThrow(()-> LiveException.failure("获取anchorBookRepo失败"));


    public Long addAnchorBook(){
        AnchorBookPO po=anchorBookRepo.getByUidAnchorId(uid,anchorId);
        if(!Objects.isNull(po)){
            LOGGER.info("订阅关系已存在，更新状态，主播id:{},用户：{}",anchorId,uid);
            po.setStatus(LiveConst.STATUS_BOOK_NORMAL);
            po.setModifiedTime(new Timestamp(Utils.getCurrentMillis()));
            anchorBookRepo.updateAnchorBook(po);
            return po.getId();
        }else{
            LOGGER.info("添加一条订阅数据，主播：{}，用户：{}",anchorId,uid);
            AnchorBookPO newPO=new AnchorBookPO();
            newPO.setModifiedTime(new Timestamp(Utils.getCurrentMillis()));
            newPO.setStatus(LiveConst.STATUS_BOOK_NORMAL);
            newPO.setAnchorId(anchorId);
            newPO.setUserId(uid);
            newPO.setCreateTime(new Timestamp(Utils.getCurrentMillis()));
            anchorBookRepo.addAnchorBook(newPO);
            return newPO.getId();
        }


    }

    public boolean delAnchorBook(){
        AnchorBookPO po=new AnchorBookPO();
        po.setUserId(uid);
        po.setAnchorId(anchorId);
        po.setStatus(LiveConst.STATUS_BOOK_DEL);
        po.setModifiedTime(new Timestamp(Utils.getCurrentMillis()));
        return anchorBookRepo.updateAnchorBook(po);

    }

    public void banAnchor(){


    }

}

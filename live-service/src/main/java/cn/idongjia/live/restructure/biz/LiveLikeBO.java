package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.db.mybatis.po.AnchorBlackWhitePO;
import cn.idongjia.live.db.mybatis.po.LiveLikePO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBlackWhiteQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.dto.AnchorBlackWhiteDTO;
import cn.idongjia.live.restructure.dto.LiveLikeDTO;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.pojo.cmd.LiveLikeAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.LiveLikeDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteAddCmd;
import cn.idongjia.live.restructure.query.AnchorBlackWhiteQueryHandler;
import cn.idongjia.live.restructure.repo.AnchorBlackWhiteRepo;
import cn.idongjia.live.restructure.repo.LiveLikeRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午2:00
 */
@Component
public class LiveLikeBO {

    @Resource
    private LiveLikeRepo liveLikeRepo;


    private static final Log LOGGER = LogFactory.getLog(LiveLikeBO.class);

    public void add(LiveLikeAddCmd cmd) {
        LiveLikePO po=new LiveLikePO();
        po.setLiveId(cmd.getLiveLikeCO().getLiveId());
        po.setUserId(cmd.getLiveLikeCO().getUserId());
        po.setCreateTime(Utils.getCurrentMillis());
        po.setUpdateTime(Utils.getCurrentMillis());
        po.setStatus(0);
        LiveLikeDTO dto=new LiveLikeDTO(po);

         liveLikeRepo.add(dto);


    }

    public boolean delete(LiveLikeDelCmd cmd){

        LiveLikePO po=new LiveLikePO();
        po.setLiveId(cmd.getLiveLikeCO().getLiveId());
        po.setUserId(cmd.getLiveLikeCO().getUserId());
        po.setCreateTime(Utils.getCurrentMillis());
        po.setUpdateTime(Utils.getCurrentMillis());
        po.setStatus(-1);
        LiveLikeDTO dto=new LiveLikeDTO(po);
        return liveLikeRepo.delete(dto);
    }

}

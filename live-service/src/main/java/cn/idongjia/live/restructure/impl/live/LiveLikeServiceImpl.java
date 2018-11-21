package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.AnchorBlackWhiteServiceI;
import cn.idongjia.live.api.live.LiveLikeServiceI;
import cn.idongjia.live.restructure.biz.AnchorBlackWhiteBO;
import cn.idongjia.live.restructure.biz.LiveLikeBO;
import cn.idongjia.live.restructure.pojo.cmd.LiveLikeAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.LiveLikeDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteUpdateCmd;
import cn.idongjia.live.restructure.pojo.co.live.AnchorBlackWhiteCO;
import cn.idongjia.live.restructure.pojo.query.AnchorBlackWhiteQry;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午1:39
 */
@Component("liveLikeServiceImpl")
public class  LiveLikeServiceImpl implements LiveLikeServiceI {



    @Resource
    private LiveLikeBO liveLikeBO;
    @Override
    public void addLike(LiveLikeAddCmd cmd) {

         liveLikeBO.add(cmd);

    }

    @Override
    public boolean deleteLike(LiveLikeDelCmd cmd) {

        return liveLikeBO.delete(cmd);
    }
}

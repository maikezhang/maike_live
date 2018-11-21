package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.AnchorBlackWhiteServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.restructure.biz.AnchorBlackWhiteBO;
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
@Component("anchorBlackWhiteServiceImpl")
public class AnchorBlackWhiteServiceImpl implements AnchorBlackWhiteServiceI {

    @Resource
    private AnchorBlackWhiteBO anchorBlackWhiteBO;

    @Override
    public void add(AnchorBlackWhiteAddCmd cmd) {
         anchorBlackWhiteBO.add(cmd);
    }

    @Override
    public Long update(AnchorBlackWhiteUpdateCmd cmd) {
        return null;
    }

    @Override
    public boolean delete(AnchorBlackWhiteDelCmd cmd) {
        return anchorBlackWhiteBO.delete(cmd.getId(),cmd.getType());
    }

    @Override
    public MultiResponse<AnchorBlackWhiteCO> page(AnchorBlackWhiteQry qry) {



        return anchorBlackWhiteBO.page(qry);
    }
}

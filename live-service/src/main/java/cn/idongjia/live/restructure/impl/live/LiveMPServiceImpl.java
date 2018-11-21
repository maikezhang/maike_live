package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.LiveMPServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.restructure.biz.LiveMPBO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPDetailCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPFormIdCO;
import cn.idongjia.live.restructure.pojo.query.LiveMPQry;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/4
 * Time: 上午11:13
 */
@Component("liveMPServiceImpl")
public class LiveMPServiceImpl implements LiveMPServiceI {


    @Resource
    private LiveMPBO liveMPBO;

    @Override
    public MultiResponse<LiveMPCO> list(LiveMPQry qry) {

        return liveMPBO.list(qry);
    }

    @Override
    public boolean updateWeight(Long liveId,Integer weight) {
        return liveMPBO.updateWeight(liveId,weight);
    }

    @Override
    public List<LiveMPCO> mpPageApi(LiveMPQry qry) {
        return liveMPBO.mpPageApi(qry);
    }

    @Override
    public LiveMPDetailCO getDetail(Long liveId, Long userId) {
        return liveMPBO.getDetail(liveId,userId);
    }

    @Override
    public boolean collectFormId(LiveMPFormIdCO liveMPFormIdCO) {
        return liveMPBO.collectFormId(liveMPFormIdCO);
    }

}

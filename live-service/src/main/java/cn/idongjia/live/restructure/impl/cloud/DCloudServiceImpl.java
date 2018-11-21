package cn.idongjia.live.restructure.impl.cloud;

import cn.idongjia.live.api.cloud.DCloudService;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.query.cloud.DLiveCloudSearch;
import cn.idongjia.live.restructure.biz.LiveShowBO;
import cn.idongjia.live.restructure.cloud.dcloud.DCloudRepo;
import cn.idongjia.live.restructure.query.LivePullUrlQueryHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component("dCloudServiceImpl")
public class DCloudServiceImpl implements DCloudService {


    @Resource
    private LiveShowBO liveShowBO;
    @Resource
    private LivePullUrlQueryHandler livePullUrlQueryHandler;

    @Override
    public boolean modifyDCloud(Long uid, LivePullUrl livePullUrl) {
        return liveShowBO.updateDLiveCloud(uid, livePullUrl);
    }

    @Override
    public LivePullUrl getPullUrlByUid(Long uid) {
        return livePullUrlQueryHandler.getPullUrlsByUid(uid);
    }

    @Override
    public List<Map<String, Object>> listAllDLiveCloud(DLiveCloudSearch dLiveCloudSearch) {
        return null;
    }
}

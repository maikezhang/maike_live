package cn.idongjia.live.restructure.cloud.dcloud;

import cn.idongjia.live.db.mybatis.po.DLiveCloudPO;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.cloud.api.CloudClient;
import cn.idongjia.live.restructure.domain.entity.live.LiveRecord;
import cn.idongjia.live.restructure.dto.DLiveCloudDTO;
import cn.idongjia.live.support.SpringBeanLoader;

import java.util.List;

public class DCloudClient implements CloudClient{

    private DCloudRepo dCloudRepo = SpringBeanLoader.getBean("dCloudRepo", DCloudRepo.class);

    @Override
    public String getRecordUrl(String cloudId, String videoName) {
        return null;
    }

    @Override
    public LivePullUrl getPlayUrl(String cloudId) {
        //从数据库中获取拉流地址rtmp/hls/flv
        DLiveCloudPO dLiveCloudPO=dCloudRepo.getPullUrlsById(cloudId).toDO();
        LivePullUrl url=new LivePullUrl();
        url.setFlvUrl(dLiveCloudPO.getFlvUrl());
        url.setHlsUrl(dLiveCloudPO.getHlsUrl());
        url.setRtmpUrl(dLiveCloudPO.getRtmpUrl());

        return url;
    }

    @Override
    public String getCloudId(String seed) {
        //组装数据
        DLiveCloudPO po=new DLiveCloudPO();
        po.setUserId(Long.parseLong(seed));
        //向数据库中放入地址
        return dCloudRepo.addDLiveCloud(new DLiveCloudDTO(po)).toString();
    }

    @Override
    public boolean startLiveShow(String cloudId) {
        //假直播开始
        return true;
    }

    @Override
    public boolean stopLiveShow(String cloudId) {
        //假直播停止
        Long id = Long.parseLong(cloudId);
        DLiveCloudPO po=new DLiveCloudPO();
        po.setId(id);
        po.setFlvUrl("");
        po.setHlsUrl("");
        po.setRtmpUrl("");
        dCloudRepo.updateDLiveCloudById(new DLiveCloudDTO(po));
        return true;
    }

    @Override
    public boolean startRecord(String cloudId, String videoName) {
        //假录制开始
        return true;
    }

    @Override
    public boolean stopRecord(String cloudId, String videoName) {
        //假录制停止
        return true;
    }

    @Override
    public List<LiveRecord> listRecords(String cloudId) {
        //返回直播记录
        return null;
    }
}

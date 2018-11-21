package cn.idongjia.live.restructure.manager;

import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.cloud.api.CloudClient;
import cn.idongjia.live.restructure.domain.entity.live.LiveRecord;
import cn.idongjia.live.support.CloudClientConst;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.idongjia.live.restructure.cloud.CloudClientFactory.getClientInstance;

@Component
public class CloudManager {

    public int getCloudType(String cloud){
        return CloudClientConst.CloudType.valueOf(cloud).getClientCode();
    }

    public String getRecordUrlByCloudType(String cloudId, int type, String videoName){
        CloudClient client = getClientInstance(type);
        return client.getRecordUrl(cloudId, videoName);
    }

    public LivePullUrl getPlayUrlByCloudType(String cloudId, int type){
        CloudClient client = getClientInstance(type);
        return client.getPlayUrl(cloudId);
    }

    public String getCloudID(String seed, int type){
        CloudClient client = getClientInstance(type);
        return client.getCloudId(seed);
    }

    public boolean startLive(String cloudId, int type) {
        CloudClient client = getClientInstance(type);
        return client.startLiveShow(cloudId);
    }

    public boolean stopLive(String cloudId, int type){
        CloudClient client = getClientInstance(type);
        return client.stopLiveShow(cloudId);
    }

    public void startRecord(String cloudId, int type, String videoName) {
        CloudClient client = getClientInstance(type);
        client.startRecord(cloudId, videoName);
    }

    public void stopRecord(String cloudId, int type, String videoName) {
        CloudClient client = getClientInstance(type);
        client.stopRecord(cloudId, videoName);
    }

    public List<LiveRecord> listRecords(String cloudId, int type) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        CloudClient client = getClientInstance(type);
        return client.listRecords(cloudId);
    }
}

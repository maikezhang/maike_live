package cn.idongjia.live.restructure.cloud.vcloud;

import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.cloud.api.CloudClient;
import cn.idongjia.live.restructure.domain.entity.live.LiveRecord;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.support.CheckSum;
import cn.idongjia.live.support.HttpUtil;
import cn.idongjia.live.support.LiveUtil;
import cn.idongjia.live.support.SpringBeanLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VCloudClient implements CloudClient {

    private static ConfigManager configManager = SpringBeanLoader.getBean("configManager", ConfigManager.class);

    private static final String HTTP_PULL_URL = "httpPullUrl";
    private static final String HLS_PULL_URL = "hlsPullUrl";
    private static final String RTMP_PULL_URL = "rtmpPullUrl";
    private static final String CID = "cid";

    private static final String PUSH_URL = "pushUrl";

    private void setHead(final HttpPost httpPost) {
        String nonce = LiveUtil.getRandom(VCloudConst.VCLOUD_RANGE);
        String curTime = String.valueOf(cn.idongjia.util.Utils.getCurrentSecond());
        String checkSum =
                CheckSum.encode("sha1",  configManager.getVCloudAppSecret()+ nonce + curTime);
        // 设置请求的header
        httpPost.addHeader(VCloudConst.APP_KEY, configManager.getVCloudAppKey());
        httpPost.addHeader(VCloudConst.NONCE, nonce);
        httpPost.addHeader(VCloudConst.CUR_TIME, curTime);
        httpPost.addHeader(VCloudConst.CHECK_SUM, checkSum);
        httpPost.addHeader(VCloudConst.CONTENT_TYPE, HttpUtil.JSON_TYPE);
    }

    private HttpPost createPost(final String json, final String url) {
        HttpEntity httpEntity = new StringEntity(json, HttpUtil.UTF_8);
        HttpPost httpPost = new HttpPost(configManager.getProxyPrefix() + url);
        setHead(httpPost);
        httpPost.setEntity(httpEntity);
        return httpPost;
    }

    private Map<String, String> createChannelPost(final String channelName) {
        HttpPost httpPost = createPost(
                "{\"name\":\"" + channelName + "\", \"type\":" + 0 + "}",
                configManager.getVCloudCreateUrl());
        String outCome = HttpUtil.sendPost(httpPost);
        JsonObject ret;
        if ((ret = checkResultCode(outCome)) != null) {
            return dealPostResult(ret, Arrays.asList(CID, PUSH_URL));
        }
        else {return new HashMap<>();}
    }

    private Map<String, String> getAddressChannelPost(final String channelId) {
        HttpPost httpPost = createPost(
                "{\"cid\":\"" + channelId + "\"}", configManager.getVCloudAddressUrl());
        String outCome = HttpUtil.sendPost(httpPost);
        JsonObject ret;
        if ((ret = checkResultCode(outCome)) != null){
            return dealPostResult(ret, Arrays.asList(PUSH_URL, HLS_PULL_URL, HTTP_PULL_URL, RTMP_PULL_URL));
        }else{
            return new HashMap<>();
        }
    }

    private Map<String, String> resumeChannelPost(String channelId) {
        HttpPost httpPost =  createPost("{\"cid\":\"" + channelId + "\"}", configManager.getVCloudResumeUrl());
        String outCome = HttpUtil.sendPost(httpPost);
        if (outCome != null) {
            return dealPostResult(new JsonParser().parse(outCome).getAsJsonObject()
                    , Collections.singletonList("code"));
        }else {
            return new HashMap<>();
        }
    }

    private Map<String, String> stopChannenlPost(String channelId) {
        HttpPost httpPost =  createPost("{\"cid\":\"" + channelId + "\"}", configManager.getVCloudPauseUrl());
        String outCome = HttpUtil.sendPost(httpPost);
        if (outCome != null) {
            return dealPostResult(new JsonParser().parse(outCome).getAsJsonObject()
                    , Collections.singletonList("code"));
        }else {
            return new HashMap<>();
        }
    }

    private Map<String, String> startRecordPost(String channelId, String fileName) {
        HttpPost httpPost = createStartRecordPost(channelId, fileName);
        String outCome = HttpUtil.sendPost(httpPost);
        if (outCome != null) {
            return dealPostResult(new JsonParser().parse(outCome).getAsJsonObject()
                    , Collections.singletonList("code"));
        }else {
            return new HashMap<>();
        }
    }

    private Map<String, String> stopRecordPost(String channelId, String videoName) {
        HttpPost httpPost = createStopRecordPost(channelId, videoName);
        String outCome = HttpUtil.sendPost(httpPost);
        if (outCome != null) {
            return dealPostResult(new JsonParser().parse(outCome).getAsJsonObject()
                    , Collections.singletonList("code"));
        }else {
            return new HashMap<>();
        }
    }

    private String listRecordPost(String channelId) {
        HttpPost httpPost = createListRecordsPost(channelId);
        String outCome = HttpUtil.sendPost(httpPost);
        if (outCome != null) {
            return outCome;
        }else {
            return null;
        }
    }

    private JsonObject checkResultCode(String outCome){
        if (outCome != null) {
            JsonObject jsonObject = new JsonParser().parse(outCome).getAsJsonObject();
            if (jsonObject.get("code").getAsInt() == 200) {
                return jsonObject.get("ret").getAsJsonObject();
            }
            else {return null;}
        }else {
            return null;
        }
    }

    private Map<String, String> dealPostResult(JsonObject jsonObject, List<String> keys){
        Map<String, String> reMap = new HashMap<>();
        keys.forEach(k -> reMap.put(k, jsonObject.get(k).getAsString()));
        return reMap;
    }

    private HttpPost createStartRecordPost(String channelId, String fileName) {
        return createPost(
                "{\"cid\": \"" + channelId +"\", \"needRecord\": 1"
                        + ", \"duration\": " + configManager.getVCloudRecordDuration()
                        + ", \"format\": " + configManager.getRecordFormat()
                        + ", \"filename\": \"" + fileName + "\""
                        + "}"
                , configManager.getVCloudRecordUrl());
    }

    private HttpPost createStopRecordPost(String channelId, String fileName) {
        return createPost(
                "{\"cid\": \"" + channelId +"\", \"needRecord\": 0"
                        + ", \"duration\": " + configManager.getVCloudRecordDuration()
                        + ", \"format\": " + configManager.getRecordFormat()
                        + ", \"filename\": \"" + fileName + "\""
                        + "}"
                , configManager.getVCloudRecordUrl());
    }

    private HttpPost createListRecordsPost(String channelId) {
        return createPost(
                "{\"cid\":\"" + channelId + "\"}", configManager.getVCloudVideoListUrl());
    }

    @Override
    public String getRecordUrl(String cloudId, String videoName) {
        return getAddressChannelPost(cloudId).get("pushUrl");
    }

    @Override
    public LivePullUrl getPlayUrl(String cloudId) {
        Map<String, String> reMap = getAddressChannelPost(cloudId);
        LivePullUrl livePullUrl = new LivePullUrl();
        livePullUrl.setFlvUrl(reMap.getOrDefault("httpPullUrl", null));
        livePullUrl.setRtmpUrl(reMap.getOrDefault("rtmpPullUrl", null));
        livePullUrl.setHlsUrl(reMap.getOrDefault("hlsPullUrl", null));
        return livePullUrl;
    }

    @Override
    public String getCloudId(String seed) { //需要调用网易api生成
        Map<String, String> reMap = createChannelPost(seed + "_"
                + UUID.randomUUID().toString().replace("-", ""));
        return reMap.get("cid");
    }

    @Override
    public boolean startLiveShow(String cloudId) {
        Map<String, String> reMap = resumeChannelPost(cloudId);
        return Integer.valueOf(reMap.get("code")) == 200;
    }

    @Override
    public boolean stopLiveShow(String cloudId) {
        Map<String, String> reMap = stopChannenlPost(cloudId);
        return Integer.valueOf(reMap.get("code")) == 200;
    }

    @Override
    public boolean startRecord(String cloudId, String videoName) {
        Map<String, String> reMap = startRecordPost(cloudId, videoName);
        return Integer.valueOf(reMap.get("code")) == 200;
    }

    @Override
    public boolean stopRecord(String cloudId, String videoName) {
        Map<String, String> reMap = stopRecordPost(cloudId, videoName);
        return Integer.valueOf(reMap.get("code")) == 200;
    }

    @Override
    public List<LiveRecord> listRecords(String channelId) {
        List<LiveRecord> reList = new ArrayList<>();
        String outCome = listRecordPost(channelId);
        if (outCome == null) {return new ArrayList<>();}
        JsonObject ret = new JsonParser().parse(outCome).getAsJsonObject().get("ret").getAsJsonObject();
        if (ret != null){
            JsonArray videoList = ret.get("videoList").getAsJsonArray();
            for (JsonElement video : videoList){
                String vid = video.getAsJsonObject().get("vid").getAsString();
                JsonObject detail = getVideoDetail(vid, channelId);
                LiveRecord liveRecord = new LiveRecord();
                String vName = detail.get("videoName").getAsString();
                liveRecord.setVideoName(vName);
                liveRecord.setVodUrl(detail.get("origUrl").getAsString());
                liveRecord.setDownloadUrl(detail.get("downloadOrigUrl").getAsString());
                String timeStr = vName.substring(vName.indexOf("_"));
                String[] t = timeStr.split("-");
                if (t.length > 1) {
                    String start_time = t[0];
                    String end_time = t[1];
                    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
                    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        start_time = newFormat.format(oldFormat.parse(start_time));
                        end_time = newFormat.format(oldFormat.parse(end_time));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    liveRecord.setStarttm(start_time);
                    liveRecord.setEndtm(end_time);
                }
                reList.add(liveRecord);
            }
        }
        return reList;
    }

    private HttpPost createVideoDetailPost(String vid, String channelId) {
        return createPost("{\"vid\":\"" + vid + "\""
                + ", \"cid\":\"" + channelId + "\""
                + "}", configManager.getVCloudVideoDetailUrl());
    }

    private JsonObject getVideoDetail(String vid, String channelId){
        HttpPost httpPost = createVideoDetailPost(vid, channelId);
        String outCome = HttpUtil.sendPost(httpPost);
        if (outCome != null) {
            return new JsonParser().parse(outCome).getAsJsonObject().get("ret").getAsJsonObject();
        }else {
            return new JsonObject();
        }
    }

}

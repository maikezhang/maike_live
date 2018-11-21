package cn.idongjia.live.restructure.cloud.qcloud;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveCloudStat;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.cloud.api.CloudClient;
import cn.idongjia.live.restructure.domain.entity.live.LiveRecord;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.support.CheckSum;
import cn.idongjia.live.support.CloudClientConst;
import cn.idongjia.live.support.HttpUtil;
import cn.idongjia.live.support.SpringBeanLoader;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static cn.idongjia.util.Utils.getCurrentSecond;

public class QCloudClient implements CloudClient {

    private              ConfigManager configManager = SpringBeanLoader.getBean("configManager", ConfigManager.class);
    //    private static final String        PUSH_URL_COMMON_PART = ".livepush.myqcloud.com/live/";
//    private static final String        PULL_URL_COMMON_PART = ".liveplay.myqcloud.com/live/";
    private static final String        LIVE_RESUME   = "1";
    private static final String        LIVE_PAUSE    = "0";
    private static final int           RES_SUCCESS   = 0;
    private static final Log           LOGGER        = LogFactory.getLog(QCloudClient.class);

    private String generateRecordUrl(String cloudId, Long txTime) {
        String safeUrl = getSafeUrl(cloudId, txTime);
        return CloudClientConst.RTMP_PREFIX + configManager.getLivePushUrl() + "/live/"
                + configManager.getQCloudBizidKey() + "_" + cloudId
                + "?bizid=" + configManager.getQCloudBizidKey() + "&" + safeUrl;
    }

    private String generatePlayUrl(String cloudId, String prefix, String suffix) {
        return prefix + configManager.getLivePullUrl() + "/live/"
                + configManager.getQCloudBizidKey() + "_" + cloudId + suffix;
    }

    private String getSafeUrl(String cloudId, long txTime) {
        String input = configManager.getQCloudRecordKey() + configManager.getQCloudBizidKey()
                + "_" + cloudId + Long.toHexString(txTime).toUpperCase();
        String txSecret = CheckSum.encode("MD5", input);
        return txSecret == null ? "" : "txSecret=" + txSecret + "&" + "txTime=" +
                Long.toHexString(txTime).toUpperCase();
    }

    /**
     * 对外提供查询推流地址的方法
     *
     * @param cloudId 云id
     * @param txTime  过期时间
     * @return 推流地址
     */
    public String getPushUrl(String cloudId, Long txTime) {
        return generateRecordUrl(cloudId, txTime);
    }

    /**
     * 开启或者关闭一个直播流的可推流状态。
     * 0 为停止，1 为开始
     *
     * @param cloudId 流id
     */
    private boolean setLiveFStatus(String cloudId, String status) {
        String cmd           = configManager.getQCloudAppId();
        String liveInterface = "Live_Channel_SetStatus";
        String t             = String.valueOf(getCurrentSecond() + 3600 * 24);
        String sign          = CheckSum.encode("MD5", configManager.getQCloudAPIKey() + t);
        String streamId      = configManager.getQCloudBizidKey() + "_" + cloudId;
        String url = configManager.getQCloudFcgiUrl() + "?cmd=" + cmd + "&interface=" + liveInterface
                + "&t=" + t + "&sign=" + sign + "&Param.s.channel_id=" + streamId
                + "&Param.n.status=" + status;
        String     httpResult = HttpUtil.sendGet(url);
        JsonObject jsonObject;
        try {
            jsonObject = new JsonParser().parse(httpResult).getAsJsonObject();
        } catch (Exception e) {
            if (Objects.nonNull(httpResult) && httpResult.contains("request failed.no returnData")) {
                LOGGER.warn("调用腾讯云的设置开播推流状态返回结果异常.不影响正常推流状态，result:{} exception:{}", httpResult, e);
                return true;
            } else {
                throw new LiveException(-12138, httpResult);
            }
        }
        int ret = jsonObject.get("ret").getAsInt();
        if (ret == RES_SUCCESS) {
            LOGGER.info("设置直播流开始成功");
            return true;
        } else {
            String message = jsonObject.get("message").getAsString();
            LOGGER.info("设置直播流开始失败, 错误信息: " + message);
            throw new LiveException(-12138, "修改状态失败!");
        }
    }

    /**
     * 查询指定直播流的推流和播放信息
     * 统计数据均为查询时间点的瞬时统计数据，而并非历史累计数据。
     * 如果目标流不在直播中，则返回结果中的output字段为空。
     * 推流信息的统计数据每5秒钟更新一次，也就是快于5秒的查询频率是浪费的。
     * 播放信息的统计数据每1分钟更新一次，也就是快于60秒的查询频率是浪费的。
     *
     * @param cloudId id可以为空，若为空则查询所有正在直播中的流
     */
    public LiveCloudStat getLiveStaticStatus(String cloudId) {
        String cmd           = configManager.getQCloudAppId();
        String liveInterface = "Get_LiveStat";
        String t             = String.valueOf(getCurrentSecond() + 3600);
        String sign          = CheckSum.encode("MD5", configManager.getQCloudAPIKey() + t);
        String pageNo        = "1";
        String pageSize      = "200";
        String streamId      = configManager.getQCloudBizidKey() + "_" + cloudId;
        String url = configManager.getQCloudStatcgiUrl() + "?cmd=" + cmd + "&interface=" + liveInterface
                + "&t=" + t + "&sign=" + sign + "&Param.n.page_no=" + pageNo
                + "&Param.n.page_size=" + pageSize + "&stream_id=" + streamId;
        JsonObject    jsonObject      = new JsonParser().parse(HttpUtil.sendGet(url)).getAsJsonObject();
        int           ret             = jsonObject.get("ret").getAsInt();
        LiveCloudStat liveCloudStatic = new LiveCloudStat();
        if (ret == RES_SUCCESS) {
            JsonObject output = jsonObject.get("output").getAsJsonObject();
            liveCloudStatic.setStreamCount(output.get("stream_count").getAsInt());  //所有在线的直播流数量
            liveCloudStatic.setTotalBandWidth(output.get("total_bandwidth").getAsDouble()); //当前账号在查询时间点的总带宽	Mbps
            liveCloudStatic.setTotalOnline(output.get("total_online").getAsInt());  //当前账号在查询时间点的总在线人数
            JsonObject streamInfo = output.get("stream_info").getAsJsonObject();
            liveCloudStatic.setCloudId(streamInfo.get("stream_name").getAsString());  //直播码
            liveCloudStatic.setBandWidth(streamInfo.get("bandwidth").getAsDouble()); //该直播流的瞬时带宽占用 Mbps
            liveCloudStatic.setOnlineCount(streamInfo.get("online").getAsInt());  //该直播流的瞬时在线人数
            liveCloudStatic.setClientIP(streamInfo.get("client_ip").getAsString()); //推流客户端IP
            liveCloudStatic.setServerIP(streamInfo.get("server_ip").getAsString());  //接流服务器IP
            liveCloudStatic.setPushFps(streamInfo.get("fps").getAsInt());  //瞬时推流帧率
            liveCloudStatic.setPushSpeed(streamInfo.get("speed").getAsInt());  //瞬时推流码率
        } else {
            String msg = jsonObject.get("message").getAsString();
            LOGGER.warn("腾讯直播: " + cloudId + "状态查询失败!失败原因: " + msg);
            throw new LiveException(-12138, "状态查询失败!!");
        }
        return liveCloudStatic;
    }

    /**
     * 获取直播流的录制文件
     *
     * @param cloudId id
     */
    private List<LiveRecord> getLiveTapeList(String cloudId) {
        String cmd           = configManager.getQCloudAppId();
        String liveInterface = "Live_Tape_GetFilelist";
        String t             = String.valueOf(getCurrentSecond() + 3600);
        String sign          = CheckSum.encode("MD5", configManager.getQCloudAPIKey() + t);
        String streamId      = configManager.getQCloudBizidKey() + "_" + cloudId;
        String pageNo        = "1";
        String pageSize      = "200";
        String url = configManager.getQCloudFcgiUrl() + "?cmd=" + cmd + "&interface=" + liveInterface
                + "&t=" + t + "&sign=" + sign + "&Param.s.channel_id=" + streamId
                + "&Param.n.page_no=" + pageNo + "&Param.n.page_size=" + pageSize + "&Param.s.sort_type=desc";
        JsonObject       jsonObject = new JsonParser().parse(HttpUtil.sendGet(url)).getAsJsonObject();
        JsonObject       output     = jsonObject.get("output").getAsJsonObject();
        JsonArray        fileList   = output.get("file_list").getAsJsonArray();
        List<LiveRecord> reList     = new ArrayList<>();
        for (JsonElement file : fileList) {
            String     vid           = file.getAsJsonObject().get("vid").getAsString();
            String     startTime     = file.getAsJsonObject().get("start_time").getAsString();
            String     endTime       = file.getAsJsonObject().get("end_time").getAsString();
            String     recordFileUrl = file.getAsJsonObject().get("record_file_url").getAsString();
            LiveRecord liveRecord    = new LiveRecord();
            liveRecord.setVideoName(vid + "");
            liveRecord.setStarttm(startTime);
            liveRecord.setEndtm(endTime);
            if (recordFileUrl != null) {
                liveRecord.setVodUrl(recordFileUrl);
                liveRecord.setDownloadUrl(recordFileUrl);
            } else {
                liveRecord.setVodUrl("http://" + configManager.getQCloudBizidKey()
                        + ".vod.myQCloud.com/" + vid + ".f0.flv");
                liveRecord.setDownloadUrl("http://" + configManager.getQCloudBizidKey()
                        + ".vod.myQCloud.com/" + vid + ".f0.flv");
            }
            reList.add(liveRecord);
        }
        return reList;
    }

    @Override
    public String getRecordUrl(String cloudId, String videoName) {
        Long lastTime = getCurrentSecond() + configManager.getLiveDuration();
        return generateRecordUrl(cloudId, lastTime)
                + "&record=" + transferFormat(configManager.getRecordFormat())
                + "&record_interval=" + configManager.getQCloudRecordDuration()
                + "&record_name=" + videoName;
    }

    @Override
    public LivePullUrl getPlayUrl(String cloudId) {
        LivePullUrl livePullUrl = new LivePullUrl();
        livePullUrl.setFlvUrl(generatePlayUrl(cloudId, CloudClientConst.HTTP_PREFIX, CloudClientConst.FLV_SUFFIX));
        livePullUrl.setRtmpUrl(generatePlayUrl(cloudId, CloudClientConst.RTMP_PREFIX, ""));
        livePullUrl.setHlsUrl(generatePlayUrl(cloudId, CloudClientConst.HTTP_PREFIX, CloudClientConst.HLS_SUFFIX));
        return livePullUrl;
    }

    @Override
    public String getCloudId(String seed) {//本地直接生成cloudId
        return seed + "_" + UUID.randomUUID().toString().replace("-", "");
    }


    @Override
    public boolean startLiveShow(String cloudId) {
        return setLiveFStatus(cloudId, LIVE_RESUME);
    }

    @Override
    public boolean stopLiveShow(String cloudId) {
        try {
            return setLiveFStatus(cloudId, LIVE_PAUSE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean startRecord(String cloudId, String videoName) {
        return true;
    }

    @Override
    public boolean stopRecord(String cloudId, String videoName) {
        return true;
    }

    @Override
    public List<LiveRecord> listRecords(String channelId) {
        return getLiveTapeList(channelId);
    }

    private String transferFormat(int format) {
        switch (format) {
            case CloudClientConst.FLV_FORMAT:
                return "flv";
            case CloudClientConst.MP4_FORMAT:
                return "mp4";
            default:
                return "";
        }
    }
}

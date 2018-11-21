package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.PlayBack;
import cn.idongjia.live.restructure.domain.manager.LiveEntityManager;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.idongjia.log.LogFactory.getLog;

/**
 * @author lc
 * @create at 2018/7/18.
 */
@Component
public class RoomCallbackBO {
    private static final Log logger = getLog(RoomCallbackBO.class);

    private static final String CHANNEL_ID = "channel_id";
    private static final String VIDEO_URL = "video_url";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final int LIVE_PUSH_ON = 1;
    private static final int LIVE_PUSH_OFF = 0;

    @Resource
    private MqProducerManager mqProducerManager;

    @Resource
    private LiveEntityManager liveEntityManager;
    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    public void dealPushOn(Map<String, Object> msg) {
        logger.info("腾讯推流恢复回调");
        String channelId = (String) msg.get(CHANNEL_ID);
        String cloudId = channelId.substring(channelId.indexOf("_") + 1);
        logger.info("推流恢复成功: " + cloudId);
        //增加发送恢复推流消息
        Long uid = Long.valueOf(cloudId.substring(0, cloudId.indexOf("_")));
        try {
            LiveShowDTO liveShowDTO = liveShowQueryHandler.getLiveShowOnByUid(uid);
            if (null == liveShowDTO) {
                liveShowDTO = liveShowQueryHandler.getLiveShowRecentlyEnd(uid);
            }
            //发送推流状态变化消息
            mqProducerManager.broadCastLivePushState(liveShowDTO.getId(), LIVE_PUSH_ON, liveShowDTO.getType());
        } catch (Exception e) {
            logger.error("获取直播失败{}", e);
            throw LiveException.failure("获取直播失败");
        }

    }

    public void dealPushOff(Map<String, Object> msg) {
        logger.info("腾讯推流关闭回调");
        String channelId = (String) msg.get(CHANNEL_ID);
        String cloudId = channelId.substring(channelId.indexOf("_") + 1);
        logger.info("腾讯云推流恢复失败: " + cloudId);
        //增加发送断流消息
        Long uid = Long.valueOf(cloudId.substring(0, cloudId.indexOf("_")));
        LiveShowDTO liveShowDTO = liveShowQueryHandler.getLiveShowOnByUid(uid);
        if (liveShowDTO == null) {
            liveShowDTO = liveShowQueryHandler.getLiveShowRecentlyEnd(uid);
        }
        //发送推流状态变化消息
        mqProducerManager.broadCastLivePushState(liveShowDTO.getId(), LIVE_PUSH_OFF, liveShowDTO.getType());
    }

    public void dealNewSnapShot(Map<String, Object> msg) {
        logger.info("腾讯产生截图文件回调");
        String channelId = (String) msg.get(CHANNEL_ID);
        String cloudId = channelId.substring(channelId.indexOf("_") + 1);
        logger.info("腾讯云生产新的截图文件, 频道ID: " + cloudId);
    }

    public void dealNewRecordFile(Map<String, Object> msg) {
        logger.info("腾讯产生录制文件回调");
        //根据channelId获取主播ID
        String channelId = (String) msg.get(CHANNEL_ID);
        String cloudId = channelId.substring(channelId.indexOf("_") + 1);
        Long userId = Long.valueOf(cloudId.substring(0, cloudId.indexOf("_")));
        String videoUrl = (String) msg.get(VIDEO_URL);
        Integer startTm = (Integer) msg.get(START_TIME);
        Integer endTm = (Integer) msg.get(END_TIME);
        Long durationMillis = TimeUnit.SECONDS.toMillis(endTm - startTm);
        //TODO 是否要增加根据预计结束时间进行判断
        //先获取正在直播的纯直播若没有则获取最近结束的纯直播
        LiveShowDTO liveShowDTO = liveShowQueryHandler.getLiveShowOnByUid(userId);
        if (liveShowDTO == null) {
            liveShowDTO = liveShowQueryHandler.getLiveShowRecentlyEnd(userId);
        }
        //验证链接是否有效
        /*try {
            String url = getByProxy(configManager.getProxyPrefix(), videoUrl);
            HttpUtil.sendGet(url);
        }catch (BizException e){
            logger.error("产生的录制地址无法访问");
            return;
        }*/
        Long liveId = liveShowDTO.getId();
        if (liveShowDTO != null ) {
            LiveEntity liveEntity = liveEntityManager.load(liveId);
            PlayBack playBack = PlayBack.builder().createTm(Utils.getNow()).duration(durationMillis)
                    .url(videoUrl).lid(liveId).modifiedTm(Utils.getNow()).status(0).build();
            liveEntity.addPlayBack(playBack);
            logger.info("腾讯云生产新的录制文件, 频道ID: " + cloudId);
            logger.info("录制地址为" + videoUrl);
        }
        //有回放以后通知更新直播
        mqProducerManager.pushLiveModify(liveId);
    }
}

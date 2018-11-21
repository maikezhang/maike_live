package cn.idongjia.live.restructure.event.handler;

import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.event.LiveStopData;
import cn.idongjia.live.restructure.event.LiveStopEvent;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import cn.idongjia.live.restructure.manager.SearchManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.restructure.repo.LiveRoomRepo;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.mq.topic.LiveTopic;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lc
 * @create at 2018/6/8.
 */
@Component
public class LiveStopStepOneHandler implements EventHandler<LiveStopEvent>, WorkHandler<LiveStopEvent> {
    private static final Log LOGGER = LogFactory.getLog(LiveStopStepOneHandler.class);

    @Resource
    private ZooManager    zooManager;
    @Resource
    private LiveRoomRepo  liveRoomRepo;
    @Resource
    private SearchManager searchManager;
    @Resource
    private MqProducerManager mqProducerManager;

    @Override
    public void onEvent(LiveStopEvent event, long sequence, boolean endOfBatch) throws Exception {
        onEvent(event);
    }


    @Override
    public void onEvent(LiveStopEvent event) throws Exception {

        LiveStopData liveStopData = event.getValue();
        Long         zid          = liveStopData.getZid();
        Long         liveShowId   = liveStopData.getId();
        Long         liveRoomId   = liveStopData.getLiveRoomId();
        String       title        = liveStopData.getTitle();
        Integer      type         = liveStopData.getType();
        Long         uid          = liveStopData.getUid();
        LOGGER.info("直播ID: " + liveShowId + "停播数据更新成功");
        // 停止聊天室记录人数
        zooManager.turnOffZooRoomRecord(zid);
        // 随机降低聊天室人数
        zooManager.turnZooRoomUserCountToZero(zid);
        liveRoomRepo.stopLiveShow(liveRoomId);
        // 根据直播间ID来关闭直播间录制
        liveRoomRepo.stopLiveRecord(liveRoomId, title);
        // mq发送直播状态改变消息
        byte[] message = new byte[2];
        message[0] = type.byteValue();
        message[1] = new Integer(LiveConst.STATE_LIVE_OVER).byteValue();
        mqProducerManager.pushMessageWithByte(LiveTopic.LIVE_STATE_CHANGE, liveShowId.toString(), message);
        LOGGER.info("直播ID: " + liveShowId + "停播状态扩展mq发送成功");

        mqProducerManager.pushLiveModify(liveShowId);
    }
}

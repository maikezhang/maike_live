package cn.idongjia.live.restructure.impl.cloud;

import cn.idongjia.live.api.cloud.QCloudService;
import cn.idongjia.live.pojo.live.LiveCloudStat;
import cn.idongjia.live.restructure.biz.LiveShowBO;
import cn.idongjia.live.restructure.biz.RoomCallbackBO;
import cn.idongjia.live.restructure.query.LiveRoomQueryHandler;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component("qCloudServiceImpl")
public class QCloudServiceImpl implements QCloudService {
    private static final int PUSH_ON = 1;
    private static final int PUSH_OFF = 0;
    private static final int NEW_SNAPSHOT_PIC = 200;
    private static final int NEW_RECORD_FILE = 100;

    private static final Log LOGGER = LogFactory.getLog(QCloudServiceImpl.class);

    @Resource
    private LiveShowBO liveShowBO;


    @Resource
    private LiveRoomQueryHandler liveRoomQueryHandler;

    @Resource
    private RoomCallbackBO roomCallbackBO;

    @Override
    public Map<String, Integer> qCloudCallBack(Map<String, Object> callBackMsg) {
        int eventType = (Integer) callBackMsg.get("event_type");
        LOGGER.info("接收到腾讯的回调" + callBackMsg);
        switch (eventType) {
            case PUSH_ON:
                roomCallbackBO.dealPushOn(callBackMsg);
                break;
            case PUSH_OFF:
                roomCallbackBO.dealPushOff(callBackMsg);
                break;
            case NEW_RECORD_FILE:
                roomCallbackBO.dealNewRecordFile(callBackMsg);
                break;
            case NEW_SNAPSHOT_PIC:
                roomCallbackBO.dealNewSnapShot(callBackMsg);
        }
        Map<String, Integer> reMap = new HashMap<>();
        reMap.put("code", 0);
        return reMap;
    }

    @Override
    public LiveCloudStat getLiveFlowStatic(Long liveId) {
        return liveRoomQueryHandler.getLiveFlowStatic(liveId);

    }


}

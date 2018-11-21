package cn.idongjia.live.restructure.manager;

import cn.idongjia.live.pojo.live.LiveAnchorEnum;
import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.v2.pojo.LiveAnchorBan;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.task.common.task.*;
import cn.idongjia.task.excuter.annocation.TaskRunner;
import cn.idongjia.task.provider.common.ITaskProvider;
import cn.idongjia.util.GsonUtil;
import cn.idongjia.util.Utils;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 任务系统
 *
 * @author zhang created on 2018/1/18 下午1:54
 * @version 1.0
 */
@Component
public class TaskManager {


    Log LOGGER = LogFactory.getLog(TaskManager.class);

    private static final String TASK_EXECUTOR = "LiveTaskExecutor";

    private static final String AUTO_ONLINE_TYPE      = "auto_online";
    private static final String AUTO_ONLINE_ID_PREFIX = "auto_online_";
    //用户付款后5分钟再次推送
    private static final String ITEM_PAID_REPUSH_TYPE = "item_paid_repush";
    //取消禁播匠人
    public static final String BANNED_ANCHOR_RELEASE_TYPE = "banned_anchor_release";

    public static final String LIVE_MODULE_TYPE="live_module";

    public static final String LIVE_START_PUSH="live_start_push";
    @Resource
    private ITaskProvider         taskProvider;


    public void addAutoOnlineTask(Long liveId, Long executeTime) {
        Type type = Type.TIMING;
        if (executeTime <= Utils.getCurrentMillis()) {
            executeTime = null;
            type = Type.REAL;
        }
        Task task = new Task();
        task.setExcuteType(ExcuteType.JAVA);
        task.setExtParam("id", liveId.toString());
        task.setExuterName(TASK_EXECUTOR);
        task.setFaultType(FaultType.LOST);
        task.setTaskType(AUTO_ONLINE_TYPE);
        task.setTriggerTime(executeTime);
        task.setTaskId(AUTO_ONLINE_ID_PREFIX + liveId);
        task.setType(type);
        taskProvider.submitTask(task);
    }

    public void updateAutoOnlineTask(Long liveId, Long executeTime) {
        Type type = Type.TIMING;
        if (executeTime <= Utils.getCurrentMillis()) {
            executeTime = null;
            type = Type.REAL;
        }
        Task task = new Task();
        task.setExcuteType(ExcuteType.JAVA);
        task.setExtParam("id", liveId.toString());
        task.setExuterName(TASK_EXECUTOR);
        task.setFaultType(FaultType.LOST);
        task.setTaskType(AUTO_ONLINE_TYPE);
        task.setTriggerTime(executeTime);
        task.setTaskId(AUTO_ONLINE_ID_PREFIX + liveId);
        task.setType(type);
        taskProvider.updateTask(task);
    }

    public void cancelAutoOnlineTask(Long liveId) {
        taskProvider.cancelTask(AUTO_ONLINE_ID_PREFIX + liveId);
    }


    public void setItemPaidRepush(long itemId, long userId) {
        LOGGER.info("付款通知再推送任务 itemId={} userId={}", itemId, userId);
        Task task = new Task();
        task.setExcuteType(ExcuteType.JAVA);
        task.setExuterName(TASK_EXECUTOR);
        task.setFaultType(FaultType.FaultType);
        task.setTaskType(ITEM_PAID_REPUSH_TYPE);
        task.setTaskId(ITEM_PAID_REPUSH_TYPE + "_" + userId + "." + itemId);
        task.setType(Type.TIMING);
        long trggerTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
        task.setTriggerTime(trggerTime);
        task.setExtParam("itemId", String.valueOf(itemId));
        task.setExtParam("userId", String.valueOf(userId));
        taskProvider.updateTask(task);
    }




    /**
     * 禁拍任务
     *
     * @param userId
     * @param durationDay
     */
    public void releaseAnchorTask(long userId, int durationDay) {
        Task task = new Task();
        task.setExcuteType(ExcuteType.JAVA);
        task.setExuterName(TASK_EXECUTOR);
        task.setFaultType(FaultType.FaultType);
        task.setTaskType(BANNED_ANCHOR_RELEASE_TYPE);
        task.setTaskId(BANNED_ANCHOR_RELEASE_TYPE + "_" + userId);
        task.setType(Type.TIMING);
        long trggerTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(durationDay);
        task.setTriggerTime(trggerTime);
        task.setExtParam("userId", String.valueOf(userId));
        taskProvider.updateTask(task);
        LOGGER.info("添加取消禁拍任务 userId={}", userId);
    }

    public void cancelReleaseAnchorTask(long userId) {
        taskProvider.cancelTask(BANNED_ANCHOR_RELEASE_TYPE + "_" + userId);
        LOGGER.info("禁拍任务取消 userId={}", userId);
    }

    public void releaseLiveModuleTask(long mid,int position,long executeTime){
        Task task=new Task();
        task.setExcuteType(ExcuteType.JAVA);
        task.setExuterName(TASK_EXECUTOR);
        task.setFaultType(FaultType.FaultType);
        task.setTaskType(LIVE_MODULE_TYPE);
        task.setTaskId(LIVE_MODULE_TYPE + "_" + mid);
        Type type=Type.TIMING;
        if(executeTime<=Utils.getCurrentMillis()){
            type=Type.REAL;
        }
        task.setType(type);
        Map<String,String> extParams=new HashMap<>();
        extParams.put("mid",String.valueOf(mid));
        extParams.put("position",String.valueOf(position));
        task.setExtParams(extParams);
        task.setTriggerTime(executeTime);
        taskProvider.submitTask(task);
        LOGGER.info("添加生效模块任务 mid={}", mid);
    }

    public void cancelLiveModuleTask(long mid){
        taskProvider.cancelTask(LIVE_MODULE_TYPE + "_" + mid);
        LOGGER.info("取消生效模块任务 mid={}", mid);
    }

    public void updateLiveModuleTask(long mid,int position,long executeTime){
        cancelLiveModuleTask(mid);
        releaseLiveModuleTask(mid,position,executeTime);
    }

    public void liveStartPushTask(Long liveId,int repeatedCount,long repeatInterval){
        Task task = new Task();
        task.setExcuteType(ExcuteType.JAVA);
        task.setExtParam("id", liveId.toString());
//        task.setExtParam("uidsToPush", GsonUtil.GSON.toJson(uidsToPush));
        task.setExuterName(TASK_EXECUTOR);
        task.setFaultType(FaultType.LOST);
        task.setTaskType(LIVE_START_PUSH);
//        task.setTriggerTime(executeTime);
        task.setTaskId(LIVE_START_PUSH + liveId);
        task.setRepeatedCount(repeatedCount);
        task.setRepeatInterval(repeatInterval);
        taskProvider.submitTask(task);
    }
}

package cn.idongjia.live.restructure.manager;

import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.pojo.live.LiveAnchorEnum;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.query.live.LiveModuleSearch;
import cn.idongjia.live.restructure.biz.AnchorBO;
import cn.idongjia.live.restructure.biz.ModuleBO;
import cn.idongjia.live.restructure.cache.LiveStartPushCahche;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.manager.LiveEntityManager;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.query.LiveModuleQueryHander;
import cn.idongjia.live.support.GsonUtil;
import cn.idongjia.live.support.enumeration.ModuleState;
import cn.idongjia.live.support.enumeration.ModuleStatus;
import cn.idongjia.live.v2.pojo.LiveAnchorBan;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.task.common.task.ExcuteResult;
import cn.idongjia.task.common.task.Task;
import cn.idongjia.task.excuter.annocation.TaskRunner;
import cn.idongjia.util.Utils;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/26
 * Time: 上午9:35
 */
@TaskRunner
@Component
public class TaskRunnerManager {

    Log LOGGER = LogFactory.getLog(TaskManager.class);
    private static final String AUTO_ONLINE_ID_PREFIX = "auto_online_";
    //用户付款后5分钟再次推送
    private static final String ITEM_PAID_REPUSH_TYPE = "item_paid_repush";

    private static final String AUTO_ONLINE_TYPE = "auto_online";


    @Resource
    private LiveEntityManager liveEntityManager;

    @Resource
    private ItemOperationPushManager itemOperationPushManager;

    @Resource
    private AnchorBO anchorBO;

    @Resource
    private ModuleBO      moduleBO;
    @Resource
    private NotifyManager notifyManager;

    @Resource
    private LiveModuleQueryHander liveModuleQueryHander;
    @Resource
    private MqProducerManager     mqProducerManager;

    @Resource
    private LiveStartPushCahche liveStartPushCahche;

    @Resource
    private ConfigManager configManager;

    @Resource
    private DivineSearchManager divineSearchManager;


    @TaskRunner(taskType = AUTO_ONLINE_TYPE)
    @SuppressWarnings("unused")
    public ExcuteResult autoOnlineTask(Task task) {
        String idStr = task.getExtParam("id");
        if (!Strings.isNullOrEmpty(idStr)) {
            Long id = Long.valueOf(idStr);

            LiveEntity entity = liveEntityManager.load(id);
            entity.updateLiveOnline(1);

        }
        return new ExcuteResult();
    }


    @TaskRunner(taskType = ITEM_PAID_REPUSH_TYPE)
    public ExcuteResult itemPaiedRepushTask(Task task) {
        Long itemId = Long.valueOf(task.getExtParam("itemId"));
        Long userId = Long.valueOf(task.getExtParam("userId"));
        LOGGER.info("付款通知再推送任务执行 itemId={} userId={}", itemId, userId);
        itemOperationPushManager.repushPaidOperation(userId, itemId);
        return new ExcuteResult();
    }

    @TaskRunner(taskType = TaskManager.BANNED_ANCHOR_RELEASE_TYPE)
    public ExcuteResult runReleaseAnchorTask(Task task) {
        long          userId = Long.valueOf(task.getExtParam("userId"));
        LiveAnchorBan ban    = new LiveAnchorBan();
        ban.setAdminId(0L);
        ban.setDurationDay(-1);
        ban.setOperation(LiveAnchorEnum.BanOperationType.RELEASE.getCode());
        ban.setMessage("自动任务取消禁播");
        ban.setUserId(userId);
        try {
            anchorBO.banAnchor(ban);
            LOGGER.info("自动取消禁播成功 userId={}", userId);
        } catch (Exception e) {
            LOGGER.error("自动取消禁播失败 userId={}", userId);
        }
        return new ExcuteResult();
    }

    @TaskRunner(taskType = TaskManager.LIVE_MODULE_TYPE)
    public ExcuteResult runReleaseModuleTask(Task task) {
        long             mid      = Long.valueOf(task.getExtParam("mid"));
        int              position = Integer.valueOf(task.getExtParam("position"));
        LiveModuleSearch search   = new LiveModuleSearch();
        search.setState(ModuleState.IN_PROGRESS.getCode());
        search.setPosition(position);
        List<LiveModule> liveModuleList = liveModuleQueryHander.search(search);
        boolean          isOnline       = false;
        //此位置进行中的 直接过期
        if (!Utils.isEmpty(liveModuleList)) {
            for (LiveModule liveModule : liveModuleList) {
                if (!isOnline && liveModule.getStatus() == ModuleStatus.ON.getCode()) {
                    isOnline = true;
                }
                liveModule.setState(ModuleState.EXPIRED.getCode());
                moduleBO.updateModule(liveModule);
            }
        }
        LiveModule liveModule = new LiveModule();
        liveModule.setId(mid);
        liveModule.setState(ModuleState.IN_PROGRESS.getCode());
        if (isOnline) {
            liveModule.setStatus(ModuleStatus.ON.getCode());
        }
        moduleBO.updateModule(liveModule);
        return new ExcuteResult();
    }

    @TaskRunner(taskType = TaskManager.LIVE_START_PUSH)
    public ExcuteResult runLiveStartPushTask(Task task) {
        try {
            long liveId = Long.valueOf(task.getExtParam("id"));
            LOGGER.info("任务：LIVE_START_PUSH repeated liveId:{}", liveId);
            LiveEntity entity     = liveEntityManager.load(liveId);
            Long       sessionId  = null;
            List<Long> uidsToPush = liveStartPushCahche.lpopLiveStartPushUserIdByCount(liveId, configManager.getLiveStartPushBatchCount());
            Set<Long>  uidSet     = new HashSet<>(uidsToPush);
            uidsToPush=new ArrayList<>(uidSet);
            if (Objects.equals(LiveEnum.LiveType.LIVE_AUCTION.getCode(), entity.getLiveType().getCode())) {
                GeneralLiveCO generalLiveCO = divineSearchManager.getById(entity.getId());
                sessionId = generalLiveCO.getSessionId();
            }else {
                mqProducerManager.pushMessage2Dynamic(liveId, entity.getHuid(), entity.getTitle(),
                        uidsToPush);
            }
            notifyManager.sendNotify(uidsToPush, entity.getHuid()
                    , entity.getTitle(), sessionId == null ? liveId : sessionId, entity.getPic(), entity.getLiveType().getCode());
        } catch (Exception e) {
            LOGGER.warn("LIVE_START_PUSH 任务执行异常 task:{} ,exception :{}：", task, e);
        }

        return new ExcuteResult();
    }
}

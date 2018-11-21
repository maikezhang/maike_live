package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.db.mybatis.po.AnchorBannedRecordPO;
import cn.idongjia.live.db.mybatis.po.LiveAnchorPO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.LiveAnchorEnum;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.user.LiveAdmin;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.dto.AnchorBannedRecordDTO;
import cn.idongjia.live.restructure.dto.LiveAnchorDTO;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.DisconfManager;
import cn.idongjia.live.restructure.manager.TaskManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.repo.AnchorBannedRecordRepo;
import cn.idongjia.live.restructure.repo.HotAnchorsRepo;
import cn.idongjia.live.restructure.repo.LiveAnchorRepo;
import cn.idongjia.live.v2.pojo.LiveAnchorBan;
import cn.idongjia.live.v2.pojo.LiveAnchorBanRecord;
import cn.idongjia.live.v2.pojo.query.LiveAnchorBanRecordQuery;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 主播
 *
 * @author lc
 * @create at 2018/6/13.
 */
@Component
public class AnchorBO {

    private static final Log            LOGGER = LogFactory.getLog(AnchorBO.class);
    @Resource
    private              HotAnchorsRepo hotAnchorsRepo;

    @Resource
    private UserManager            userManager;
    @Resource
    private LiveAnchorRepo         liveAnchorRepo;
    @Resource
    private AnchorBannedRecordRepo anchorBannedRecordRepo;
    @Resource
    private TaskManager            taskManager;

    @Resource
    private ConfigManager configManager;

    @Resource
    private DisconfManager                                                    disconfManager;

    @Resource
    private ConvertorI<LiveAnchorBanRecord, LiveAnchor, AnchorBannedRecordPO> liveAnchorBanRecordConvertor;

    public List<Long> hotAnchors() {
        return hotAnchorsRepo.listHotAnchors();
    }

    public List<Anchor> listRandomly(Long uid) {
        return hotAnchorsRepo.listRandomly(uid);
    }


    public void banAnchor(LiveAnchorBan liveAnchorBan) {
        LiveAnchorDTO liveAnchorDTO = checkAnchor(liveAnchorBan.getUserId());
        if (LiveAnchorEnum.BanOperationType.BANNED.getCode() == liveAnchorBan.getOperation()) {
            ban(liveAnchorBan, liveAnchorDTO);
        } else if (LiveAnchorEnum.BanOperationType.RELEASE.getCode() == liveAnchorBan.getOperation()) {
            release(liveAnchorBan, liveAnchorDTO);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void ban(LiveAnchorBan ban, LiveAnchorDTO liveAnchorDTO) {
        if (liveAnchorDTO.getAnchorState() == LiveAnchorEnum.AnchorState.BANNED.getCode()) {
            return;
        }
        liveAnchorDTO.setAnchorState(LiveAnchorEnum.BanOperationType.BANNED.getCode());
        AnchorBannedRecordDTO anchorBannedRecordDTO = buildLiveAnchorBanRecord(ban);
        anchorBannedRecordRepo.insert(anchorBannedRecordDTO);
        liveAnchorDTO.setUpdateTime(System.currentTimeMillis());
        liveAnchorRepo.update(liveAnchorDTO);
        //取消禁播任务
        if (ban.getDurationDay() > 0) {
            taskManager.releaseAnchorTask(ban.getUserId(), ban.getDurationDay());
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void release(LiveAnchorBan ban, LiveAnchorDTO liveAnchorDTO) {
        if (liveAnchorDTO.getAnchorState() == LiveAnchorEnum.AnchorState.RELEASE.getCode()) {
            return;
        }
        liveAnchorDTO.setAnchorState(LiveAnchorEnum.BanOperationType.RELEASE.getCode());
        AnchorBannedRecordDTO anchorBannedRecordDTO = buildLiveAnchorBanRecord(ban);
        anchorBannedRecordRepo.insert(anchorBannedRecordDTO);
        liveAnchorDTO.setUpdateTime(System.currentTimeMillis());
        liveAnchorRepo.update(liveAnchorDTO);
        taskManager.cancelReleaseAnchorTask(liveAnchorDTO.getUserId());
    }

    public LiveAnchorDTO checkAnchor(long userId) {
        if (!userManager.hasCraftsman(userId)) {
            throw LiveException.failure("匠人不存在");
        }
        LiveAnchorDTO liveAnchorDTO = liveAnchorRepo.getByUserId(userId);
        if (liveAnchorDTO == null) {
            liveAnchorDTO = buildLiveAnchorDTO(userId);
            liveAnchorRepo.insert(liveAnchorDTO);

        }
        return liveAnchorDTO;
    }


    private LiveAnchorDTO buildLiveAnchorDTO(long userId) {
        LiveAnchorPO liveAnchorDO = LiveAnchorPO.builder()
                .anchorState(LiveAnchorEnum.AnchorState.RELEASE.getCode())
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .userId(userId)
                .build();
        return new LiveAnchorDTO(liveAnchorDO);
    }

    private AnchorBannedRecordDTO buildLiveAnchorBanRecord(LiveAnchorBan liveAnchorBan) {
        AnchorBannedRecordPO anchorBannedRecordPO = AnchorBannedRecordPO.builder()
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .userId(liveAnchorBan.getUserId())
                .adminId(liveAnchorBan.getAdminId())
                .durationDay(liveAnchorBan.getDurationDay())
                .message(liveAnchorBan.getMessage())
                .operation(liveAnchorBan.getOperation())
                .build();
        return new AnchorBannedRecordDTO(anchorBannedRecordPO);
    }


    /**
     * 主播禁播记录查询
     * 主播禁播查询是以主播表为基础进行查询
     *
     * @param query
     * @return
     */
    public BaseList<LiveAnchorBanRecord> listBannedRecord(LiveAnchorBanRecordQuery query) {
        fillRecordQuery(query);
        Integer                    count        = anchorBannedRecordRepo.countWithAnchor(query);
        List<AnchorBannedRecordPO> recordDOList = anchorBannedRecordRepo.selectWithAnchor(query);
        //匠人查询
        final List<Long> userIds = recordDOList.stream()
                .map(AnchorBannedRecordPO::getUserId)
                .distinct()
                .collect(Collectors.toList());
        final Map<Long, LiveAnchor> craftsmanMap = userManager.takeCraftmsnWithCategoryList(userIds);
        //管理员查询
        final List<Long> adminIds = recordDOList.stream()
                .map(AnchorBannedRecordPO::getAdminId)
                .distinct()
                .collect(Collectors.toList());
        final Map<Long, LiveAdmin> adminMap = userManager.takeBatchAdmin(adminIds);
        //数据组装
        List<LiveAnchorBanRecord> items = recordDOList.stream().map(recordDO -> {

            final LiveAnchorBanRecord record    = liveAnchorBanRecordConvertor.dataToClient(recordDO);
            final LiveAnchor          craftsman = craftsmanMap.getOrDefault(recordDO.getUserId(), null);
            if (craftsman != null) {
                record.setUserName(craftsman.getUsername());
                record.setUserRegisterTime(craftsman.getRegisterTime());
            }
            final LiveAdmin admin = adminMap.getOrDefault(record.getAdminId(), null);
            if (admin != null) {
                record.setAdminName(admin.getName());
            }
            return record;
        }).collect(Collectors.toList());
        //结果组装
        BaseList<LiveAnchorBanRecord> result = new BaseList<LiveAnchorBanRecord>();
        result.setItems(items);
        result.setCount(count);
        return result;
    }

    /**
     * 一些默认
     *
     * @param query
     */
    private void fillRecordQuery(LiveAnchorBanRecordQuery query) {
        if (query.getOperationDate() != null) {
            query.setOperationTimeFrom(query.getOperationDate());
            Long end = query.getOperationDate() + TimeUnit.DAYS.toMillis(1);
            query.setOperationTimeEnd(end);
        }
        //只查询已禁播的数据
        query.setAnchorState(LiveAnchorEnum.AnchorState.BANNED.getCode());
        //按用户名查询
        if (!Utils.isEmpty(query.getUserName())) {
            final List<Long> userIds = userManager.searchUserIds(query.getUserName());
            if (userIds != null) {
                query.setUserIds(userIds);
            } else {
                query.setUserIds(Collections.singletonList(0L));
            }
        }
        //排序
        if (LiveAnchorBanRecordQuery.ORDERBY_CONDITIONS.contains(query.getOrderBy())) {
            String order = LiveAnchorBanRecordQuery.ORDER_CONTIDIONTS.get(0);
            if (LiveAnchorBanRecordQuery.ORDER_CONTIDIONTS.contains(query.getOrder())) {
                order = query.getOrder();
            }
            String orderBy = "record." + query.getOrderBy() + " " + order;
            query.setOrderBy(orderBy);
        } else {
            query.setOrderBy(null);
        }
    }


    /**
     * 批量增加推荐主播
     *
     * @param uids 主播ID列表
     * @return 是否成功
     */
    public boolean addHotAnchors(List<Long> uids) {
        Set<Long> anchorIds     = listHotAnchorsAsSet();
        Set<Long> failedAnchors = new HashSet<>();
        LOGGER.info("增加之前热门主播: " + anchorIds);
        for (Long uid : uids) {
            try {
                userManager.getUser(uid);
                anchorIds.add(uid);
            } catch (LiveException e) {
                failedAnchors.add(uid);
            }
        }
        LOGGER.info("增加之后热门主播: " + anchorIds);
        replaceHotAnchors(anchorIds);
        if (failedAnchors.size() == 0) {
            return true;
        } else {
            throw new LiveException(-12138, "用户ID: " + StringUtils.join(failedAnchors, ",") + "不存在");
        }
    }

    /**
     * 集合形式获取主播ID
     *
     * @return 主播ID集合
     */
    private Set<Long> listHotAnchorsAsSet() {
        String    uidStr = configManager.getHotAnchors();
        Set<Long> reSet  = new HashSet<>();
        if (uidStr == null || "".equals(uidStr)) {
            return reSet;
        }
        for (String uid : uidStr.split(",")) {
            reSet.add(Long.parseLong(uid));
        }
        return reSet;
    }

    /**
     * 根据推荐主播集合写入disconf
     *
     * @param collection 集合
     * @return 是否成功
     */
    private boolean replaceHotAnchors(Collection collection) {
        String uidStr = StringUtils.join(collection, ",");
        return disconfManager.addConfig(uidStr, configManager.getHotAnchorsConfName(), "hot_anchors");
    }

    /**
     * 删除推荐主播ID
     *
     * @param uid 主播ID
     * @return 是否成功
     */
    public boolean deleteHotAnchor(Long uid) {
        Set<Long> uids = listHotAnchorsAsSet();
        LOGGER.info("删除之前热门主播: " + uids);
        if (uids.contains(uid)) {
            uids.remove(uid);
        }
        LOGGER.info("删除之后热门主播: " + uids);
        return replaceHotAnchors(uids);
    }


    /**
     * 增加推荐主播ID
     *
     * @param uid 主播ID
     * @return 是否成功
     */
    public boolean addHotAnchor(Long uid) {
        userManager.getUser(uid);
        Set<Long> uids = listHotAnchorsAsSet();
        LOGGER.info("增加之前热门主播: " + uids);
        try {
            userManager.getUser(uid);
            uids.add(uid);
        } catch (Exception e) {
            throw new LiveException(-12138, "用户ID: " + uid + "不存在");
        }
        LOGGER.info("增加之后热门主播: " + uids);
        return replaceHotAnchors(uids);
    }


}

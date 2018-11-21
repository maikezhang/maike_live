package cn.idongjia.live.restructure.domain.entity.live;

import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.db.mybatis.po.*;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.pojo.live.LiveTypeConfig;
import cn.idongjia.live.restructure.dto.*;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.GemManager;
import cn.idongjia.live.restructure.repo.*;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.enumeration.LiveResourceType;
import cn.idongjia.live.support.enumeration.LiveStatus;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.live.v2.pojo.LiveResourceEnum;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import com.alibaba.dubbo.common.utils.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.DateTimeUtil.millis2Timestamp;
import static cn.idongjia.util.Utils.getCurrentMillis;


/**
 * @author lc
 * @create at 2018/6/6.
 */
public interface LiveBaseRole {
    /**
     * 开播
     */
    default void start(LiveShowDTO liveShowDTO) {
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.startLiveShow(liveShowDTO);
    }

    /**
     * 结束
     */
    default void stop(LiveShowDTO liveShowDTO) {
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.stopLiveShow(liveShowDTO);
    }

    /**
     * 创建
     */
    void create(LiveEntity liveEntity);

    /**
     * 删除
     *
     * @param liveId
     */
    void delete(long liveId);

//    /**
//     * 更新
//     *
//     * @param liveEntity
//     */
//    boolean update(LiveEntity liveEntity, LiveShow liveShow, PureLive pureLive, CraftsLive craftsLive);

    /**
     * 更新
     *
     * @param oldEntity 更新前数据
     * @param newEntity 需要更新数据
     * @return
     */
    boolean update(LiveEntity oldEntity, LiveEntity newEntity);


    void updateLiveOnline(LiveEntity entity, Integer online);

    default void updateLiveShowOnline(Long liveId, Integer online) {
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.updateLiveShowOnline(liveId, online);

    }

    default void updateLiveAutoOnline(Long liveId, Integer autoOnline) {
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.updateLiveShowAutoOnline(liveId, autoOnline);
    }

    /**
     * 修改直播通用权重
     *
     * @param lid
     * @param weight
     */
    default void modifyLivemodifyGeneralWeight(Long lid, Integer weight) {
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.modifyGeneralWeight(lid, weight);

    }


    default boolean updateLiveShow(LiveEntity oldEntity, LiveEntity newEntity) {
        initUpdateLiveShow(oldEntity, newEntity);
        updateLiveShowValidate(oldEntity, newEntity);
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        return liveShowRepo.updateLiveShow(assmableLiveShowPO(newEntity).toDO());

    }

    default void updateLiveShowValidate(LiveEntity oldEntity, LiveEntity newEntity) {
        Log              LOGGER           = LogFactory.getLog(LiveBaseRole.class);
        Long             previewTime      = newEntity.getPreviewTime() == null ? oldEntity.getPreviewTime() : newEntity.getPreviewTime();
        Long             estimatedEndTime = newEntity.getEstimatedEndTime() == null ? oldEntity.getEstimatedEndTime() : newEntity.getEstimatedEndTime();
        LiveShowRepo     liveShowRepo     = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        List<LiveShowPO> po               = liveShowRepo.overlapLiveByPreStartTmList(newEntity.getEstimatedStartTime(), estimatedEndTime, newEntity.getHuid(), oldEntity.getId());
        if (Objects.nonNull(po) && po.size() > 0) {
            LiveShowPO overlap = po.get(0);
            LOGGER.warn("更新时间与直播ID: " + overlap.getId() + ", 名字: " + overlap.getTitle() + "直播冲突");
            throw new LiveException(-12138, "更新时间与直播ID: " + overlap.getId() + ", 名字: " + overlap.getTitle() + "直播冲突");
        }
    }


    default void initUpdateLiveShow(LiveEntity entity, LiveEntity newEntity) {
        // 补全直播信息并更新数据库
        if (newEntity.getEstimatedStartTime() == null) {
            newEntity.setEstimatedStartTime(newEntity.getPreviewTime());
        }
        if (Objects.isNull(newEntity.getStatus())) {
            newEntity.setStatus(LiveStatus.NORMAL);
        }

        newEntity.setId(entity.getId());
        newEntity.setModifiedTime(getCurrentMillis());

        if (!Objects.isNull(newEntity.getTitle())) {
            String  newTitle = newEntity.getTitle().trim();
            Integer newType  = newEntity.getLiveType().getCode();
            if (!Objects.equals(LiveEnum.LiveType.LIVE_AUCTION.getCode(), newType.intValue())) {
                LiveShowRepo         liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
                Map<Integer, String> mapLiveType  = liveShowRepo.mapLiveTypeConfig();
                if (!Objects.isNull(mapLiveType) && !Objects.equals(LiveEnum.LiveType.ELSE_TYPE.getCode(), newType.intValue())) {
                    if (!newTitle.contains(mapLiveType.get(newType)))
                        newEntity.setTitle(mapLiveType.get(newType) + newTitle);
                }
            }

        }


    }


    /**
     * 继续直播
     *
     * @param roomId
     */
    default void resume(long roomId) {
        LiveRoomRepo liveRoomRepo = SpringUtils.getBean("liveRoomRepo", LiveRoomRepo.class).orElseThrow(() -> LiveException.failure("获取直播间失败"));
        liveRoomRepo.startLiveShow(roomId);
    }

    default void addPlayBack(PlayBack playBack) {
        PlayBackRepo playBackRepo = SpringUtils.getBean("playBackRepo", PlayBackRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        Long         id           = playBackRepo.addPlayBack(assamblePlayBack(playBack));
        playBack.setId(id);
    }

    default PlayBackDTO assamblePlayBack(PlayBack playBack) {
        PlayBackPO po = PlayBackPO.builder()
                .createTime(playBack.getCreateTm())
                .modifiedTime(playBack.getModifiedTm())
                .duration(playBack.getDuration())
                .liveId(playBack.getLid())
                .status(playBack.getStatus())
                .url(playBack.getUrl())
                .build();
        PlayBackDTO dto = new PlayBackDTO(po);
        return dto;


    }

    default void addLiveBook(LivePureBook livePureBook) {
        Log LOGGER = LogFactory.getLog(LiveBaseRole.class);
        DBLiveBookQuery dbLiveBookQuery = DBLiveBookQuery.builder()
                .liveIds(Arrays.asList(livePureBook.getLid()))
                .userId(livePureBook.getUid())
                .status(LiveConst.STATUS_BOOK_NORMAL)
                .build();
        LiveBookRepo     liveBookRepo = SpringUtils.getBean("liveBookRepo", LiveBookRepo.class).orElseThrow(() -> LiveException.failure("获取LiveBookRepo实例失败"));
        List<LiveBookPO> pos          = liveBookRepo.list(dbLiveBookQuery).stream().map(LiveBookDTO::toDO).collect(Collectors.toList());
        LiveBookPO       po           = assambleLiveBookPO(livePureBook);
        if (CollectionUtils.isNotEmpty(pos)) {
            po.setCreateTime(null);
            LiveBookDTO dto = new LiveBookDTO(po);
            liveBookRepo.update(dto);

        } else {
            po.setCreateTime(new Timestamp(Utils.getCurrentMillis()));
            liveBookRepo.addPureLiveBook(new LiveBookDTO(po));
            LOGGER.info("增加直播订阅成功，ID: " + po.getId());
        }
        livePureBook.setId(po.getId());
    }

    default LiveBookPO assambleLiveBookPO(LivePureBook livePureBook) {
        LiveBookPO po = new LiveBookPO();
        po.setLiveId(livePureBook.getLid());
        po.setUserId(livePureBook.getUid());
        po.setCreateTime(new Timestamp(Utils.getCurrentMillis()));
        po.setModifiedTime(new Timestamp(Utils.getCurrentMillis()));
        po.setStatus(LiveConst.STATUS_BOOK_NORMAL);
        return po;
    }

    default void delLiveBook(LivePureBook livePureBook) {
        LiveBookRepo liveBookRepo = SpringUtils.getBean("liveBookRepo", LiveBookRepo.class).orElseThrow(() -> LiveException.failure("获取LiveBookRepo实例失败"));
        LiveBookPO   po           = assambleLiveBookPO(livePureBook);
        po.setStatus(LiveConst.STATUS_BOOK_DEL);
        po.setCreateTime(null);
        LiveBookDTO dto = new LiveBookDTO(po);
        liveBookRepo.update(dto);

    }

    default void addLiveTagRel(LiveTagRel rel) {
        LiveTagRepo  liveTagRepo = SpringUtils.getBean("liveTagRepo", LiveTagRepo.class).orElseThrow(() -> LiveException.failure("获取LiveTagRepo实例失败"));
        LiveTagRelPO po          = assambleLiveTagRel(rel);
        po.setStatus(LiveConst.STATUS_TAG_REL_NORMAL);
        Long id = liveTagRepo.addTagRel(new LiveTagRelDTO(po));
        rel.setId(id);
    }

    default boolean updateLiveTagRel(LiveTagRel rel) {
        LiveTagRepo  liveTagRepo = SpringUtils.getBean("liveTagRepo", LiveTagRepo.class).orElseThrow(() -> LiveException.failure("获取LiveTagRepo实例失败"));
        LiveTagRelPO po          = assambleLiveTagRel(rel);
        return liveTagRepo.updatePureLiveTagRel(new LiveTagRelDTO(po));


    }

    default boolean delLiveTagRel(LiveTagRel rel) {
        LiveTagRepo  liveTagRepo = SpringUtils.getBean("liveTagRepo", LiveTagRepo.class).orElseThrow(() -> LiveException.failure("获取LiveTagRepo实例失败"));
        LiveTagRelPO po          = assambleLiveTagRel(rel);
        po.setStatus(LiveConst.STATUS_TAG_REL_DEL);
        return liveTagRepo.updatePureLiveTagRel(new LiveTagRelDTO(po));

    }

    default LiveTagRelPO assambleLiveTagRel(LiveTagRel rel) {
        LiveTagRelPO po = new LiveTagRelPO();
        po.setCreateTime(new Timestamp(Utils.getCurrentMillis()));
        po.setId(rel.getId());
        po.setModifiedTime(new Timestamp(Utils.getCurrentMillis()));
        po.setLiveId(rel.getLid());
        po.setStatus(rel.getStatus());
        po.setTagId(rel.getTagId());
        return po;


    }


    default void createValidate(LiveEntity entity) {
        LiveAnchorRepo         liveAnchorRepo         = SpringUtils.getBean("liveAnchorRepo", LiveAnchorRepo.class).orElseThrow(() -> LiveException.failure("获取liveAnchorRepo失败"));
        AnchorBannedRecordRepo anchorBannedRecordRepo = SpringUtils.getBean("anchorBannedRecordRepo", AnchorBannedRecordRepo.class).orElseThrow(() -> LiveException.failure("获取AnchorBannedRecordRepo失败"));

        Long huid = entity.getHuid();
        if (liveAnchorRepo.isBanned(huid)) {
            String                errorMessage          = "您因违反直播规则，禁播";
            String                dayMessage            = "天";
            String                message;
            AnchorBannedRecordDTO anchorBannedRecordDTO = anchorBannedRecordRepo.takeLastBannedRecord(huid);
            if (anchorBannedRecordDTO == null) {
                message = errorMessage + "永久";
            } else if (anchorBannedRecordDTO.getDurationDay() <= 0) {
                message = errorMessage + "永久";
            } else {
                message = errorMessage + anchorBannedRecordDTO.getDurationDay() + dayMessage;
            }
            throw LiveException.failure(message);
        }
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        Log          LOGGER       = LogFactory.getLog(LiveBaseRole.class);
        if (Objects.isNull(entity.getEstimatedStartTime())) {
            throw LiveException.failure("预计开始时间不能为空");
        }
        Long currentTime = Utils.getCurrentMillis();
        if (entity.getEstimatedStartTime() < currentTime) {
            throw LiveException.failure("预计开始时间不能小于当前时间");
        }
        if (Objects.nonNull(entity.getEstimatedEndTime())) {
            if (entity.getEstimatedEndTime() < currentTime) {
                throw LiveException.failure("预计结束时间必须大于当前时间");
            }
            if (entity.getEstimatedStartTime() >= entity.getEstimatedEndTime()) {
                throw LiveException.failure("预计开始时间必须小于预计结束时间");
            }
        }

        if (entity.getState() == null || !entity.getState().equals(LiveEnum.LiveState.FINISHED)) {
            List<LiveShowPO> liveShowsOverLap = new ArrayList<>();
            liveShowsOverLap = liveShowRepo.overlapLiveByPreStartTmList(entity.getEstimatedStartTime(), entity.getEstimatedEndTime(), huid, null);
            if (liveShowsOverLap.size() > 0) {
                LOGGER.warn("直播创建失败: " + entity.getTitle());
                LiveShowPO overLap = liveShowsOverLap.get(0);
                LOGGER.warn("直播主播用户ID: " + huid + "与直播: " + overLap.getTitle() + "时间冲突");
                throw new LiveException(-12138, "直播主播用户ID: " + huid + "与直播: " + overLap.getTitle() + "时间冲突");
            }
        }


    }

    default void createBase(LiveEntity entity) {
        initCreate(entity);
        createValidate(entity);
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));

        Long lid = liveShowRepo.addLiveShow(assmableLiveShowPO(entity));

        if (Objects.equals(entity.getLiveType().getCode(), LiveEnum.LiveType.LIVE_AUCTION.getCode()) && Objects.nonNull(entity.getLiveVideoCover())) {
            VideoCoverRepo videoCoverRepo = SpringUtils.getBean("videoCoverRepo", VideoCoverRepo.class)
                    .orElseThrow(() -> LiveException.failure("获取videoCoverRepo实例失败"));
            VideoCoverPO po = new VideoCoverPO();
            po.setLiveId(lid);
            po.setId(entity.getLiveVideoCover().getId());
            VideoCoverDTO dto = new VideoCoverDTO(po);

            videoCoverRepo.update(dto);
        }


        entity.setId(lid);


    }

    default LiveShowDTO assmableLiveShowPO(LiveEntity entity) {

        LiveShowPO po = new LiveShowPO();
        po.setModifiedTime(millis2Timestamp(entity.getModifiedTime()));
        if (!Objects.isNull(entity.getAutoOnline())) {
            po.setAutoOnline(entity.getAutoOnline().getCode());
        }
        po.setCreateTime(millis2Timestamp(entity.getCreateTime()));
        po.setEndTime(millis2Timestamp(entity.getEndTime()));

        po.setPreviewTime((millis2Timestamp(entity.getPreviewTime())));
        po.setEstimatedStartTime(millis2Timestamp(entity.getEstimatedStartTime()));
        po.setEstimatedEndTime(millis2Timestamp(entity.getEstimatedEndTime()));
        if (!Objects.isNull(entity.getStartTime())) {
            po.setStartTime(millis2Timestamp(entity.getStartTime()));
        }
        po.setGeneralWeight(entity.getWeight());
        po.setId(entity.getId());
        po.setStatus(entity.getStatus().getCode());
        if (!Objects.isNull(entity.getOnline())) {
            po.setOnline(entity.getOnline().getCode());
        }
        po.setRoomId(entity.getRoomId());
        po.setScreenDirection(entity.getScreenDirection());
        po.setShowDesc(entity.getShowDesc());
        if (!Objects.isNull(entity.getState())) {
            po.setState(entity.getState().getCode());
        }
        po.setTitle(entity.getTitle());
        po.setType(entity.getLiveType().getCode());
        po.setUserId(entity.getHuid());
        if (!Objects.isNull(entity.getLiveVideoCover())) {
            po.setVideoCoverId(entity.getLiveVideoCover().getId() == null ? null : entity.getId());
        }
        po.setZooId(entity.getZid());
        LiveShowDTO dto = new LiveShowDTO(po);
        return dto;

    }


    default void initCreate(LiveEntity liveEntity) {

        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class)
                .orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        //设置预展时间
        if (Objects.isNull(liveEntity.getPreviewTime())) {
            liveEntity.setPreviewTime(liveEntity.getEstimatedStartTime());
        }

        //如果预计结束时间为空
        if (Objects.isNull(liveEntity.getEstimatedEndTime())) {
            //首先获取主播下一场直播预计结束时间
            Long preEndTime = liveShowRepo.nextLive(liveEntity.getHuid(), liveEntity.getEstimatedStartTime());
            liveEntity.setEstimatedEndTime(preEndTime);
        }
        LiveRoomRepo liveRoomRepo = SpringUtils.getBean("liveRoomRepo", LiveRoomRepo.class)
                .orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        if (Objects.isNull(liveEntity.getRoomId())) {
            Long liveRoomId = liveRoomRepo.assignLiveRoom(liveEntity.getHuid(), liveEntity.getCloudType());
            liveEntity.setRoomId(liveRoomId);
        }
        // 补全直播对应信息
        if (liveEntity.getEstimatedStartTime() == null) {
            liveEntity.setEstimatedStartTime(liveEntity.getPreviewTime());
        }
        if (liveEntity.getOnline() == null) {
            liveEntity.setOnline(LiveEnum.LiveOnline.OFFLINE);
        }
        if (Objects.isNull(liveEntity.getAutoOnline())) {
            liveEntity.setAutoOnline(BaseEnum.YesOrNo.NO);
        }
        if (Objects.isNull(liveEntity.getStatus())) {
            liveEntity.setStatus(LiveStatus.NORMAL);
        }
        if (Objects.isNull(liveEntity.getState())) {
            liveEntity.setState(LiveEnum.LiveState.UNSTART);
        }
        if (!Objects.equals(LiveEnum.LiveType.LIVE_AUCTION.getCode(), liveEntity.getLiveType().getCode())) {
            Integer              liveType    = liveEntity.getLiveType().getCode();
            String               liveTitle   = liveEntity.getTitle().trim();
            Map<Integer, String> mapLiveType = liveShowRepo.mapLiveTypeConfig();
            if (!Objects.isNull(mapLiveType) && !Objects.equals(LiveEnum.LiveType.ELSE_TYPE.getCode(), liveType.intValue())) {
                if (!liveTitle.contains(mapLiveType.get(liveType))) {
                    liveEntity.setTitle(mapLiveType.get(liveType) + liveEntity.getTitle());
                }
            }
        }

        liveEntity.setCreateTime(getCurrentMillis());
        liveEntity.setModifiedTime(getCurrentMillis());

    }

    /**
     * 添加直播资源
     */
    default Map<Long, String> addLiveResource(LiveEntity entity, List<LiveShowResource> liveResources) {

        LiveResourceRepo liveResourceRepo = SpringUtils.getBean("liveResourceRepo", LiveResourceRepo.class)
                .orElseThrow(() -> LiveException.failure("获取liveResourceRepo实例失败"));

        if (Utils.isEmpty(liveResources)) {
            return new HashMap<>(0);
        }
        Map<Long, String>    reMap       = new HashMap<>(7);
        Timestamp            currentTime = new Timestamp(Utils.getCurrentMillis());
        List<LiveResourcePO> newDetails  = assambleLiveResource(entity.getId(), liveResources, null);
        if (Objects.equals(newDetails.get(0).getResourceType(), LiveResourceType.ITEM.getCode())) {
            //获取原有资源
            DBLiveResourceQuery query = DBLiveResourceQuery.builder()
                    .status(LiveConst.STATUS_DETAIL_NORMAL)
                    .resourceType(cn.idongjia.live.support.enumeration.LiveResourceType.ITEM.getCode())
                    .liveIds(Arrays.asList(entity.getId()))
                    .build();
            //获取原有资源
            List<LiveResourcePO> oldResource = liveResourceRepo.list(query).stream().map(LiveResourceDTO::toDO).collect(Collectors.toList());
            List<LiveResourcePO> allDetails  = new ArrayList<>(oldResource);
            //去除新增中重复的
            newDetails.removeAll(oldResource);
            allDetails.addAll(newDetails);
            Set<Long> itemIdSet = allDetails.stream().map(LiveResourcePO::getResourceId).collect(Collectors.toSet());
            //去除不能关联商品
            reMap = judgeItemResource(itemIdSet);
            List<LiveResourcePO>     need2Delete = new ArrayList<>();
            Iterator<LiveResourcePO> iterator    = allDetails.iterator();
            Set<Long>                failedIdSet = reMap.keySet();
            int                      maxWeight   = allDetails.size();
            while (iterator.hasNext()) {
                LiveResourcePO detailDO   = iterator.next();
                long           resourceId = detailDO.getResourceId();
                if (failedIdSet.contains(resourceId)) {
                    need2Delete.add(detailDO);
                    iterator.remove();
                } else {
                    detailDO.setWeight(maxWeight--);
                }
            }
            ConfigManager configManager = SpringUtils.getBean("configManager", ConfigManager.class)
                    .orElseThrow(() -> LiveException.failure("获取configManager实例失败"));
            if (allDetails.size() >= configManager.getLiveResourceMaxNum()) {
                throw LiveException.failure("直播资源总量不能大于" + configManager.getLiveResourceMaxNum() + "件");
            }

            //删除失效资源
            if (!Utils.isEmpty(need2Delete)) {
                liveResourceRepo.batchDeleteResource(need2Delete);
//                SpringUtils.sendEvent(new DismissedResourceEvent(liveId, need2Delete));
            }
//            //更新权重
//            for (PureLiveDetailDO detailDO : oldDetails) {
//                detailDO.setLid(liveId);
//                detailDO.setModifiedTm(new Timestamp(Utils.getCurrentMillis()));
//                pureLiveDetailMapper.updateWeight(detailDO);
//            }
            //更新权重
            liveResourceRepo.updateWeight(oldResource);
            //增加关联关系
            allDetails.removeAll(oldResource);
            if (CollectionUtils.isNotEmpty(allDetails)) {
                liveResourceRepo.batchInsertResource(allDetails);
            }
//            SpringUtils.sendEvent(new RelatedResourceEvent(liveId, newDetails));
            return reMap;
        } else {
            return new HashMap<>(0);
        }


    }

    /**
     * 判断商品资源是否合理，返回不合理以及原因
     *
     * @param itemIdSet 商品id集合
     * @return 不合理
     */
    default Map<Long, String> judgeItemResource(Set<Long> itemIdSet) {
        GemManager gemManager = SpringUtils.getBean("gemManager", GemManager.class).orElseThrow(() -> LiveException.failure("获取gemManager实例失败"));
        //需要校验商品状态
        Map<Long, String> reMap = new HashMap<>(13);
        Map<Long, ItemPext> itemPextMap = gemManager.takeBatchItem(itemIdSet).stream()
                .collect(Collectors.toMap(ItemPext::getIid, i -> i, (k, v) -> v));
        for (Long itemId : itemIdSet) {
            ItemPext itemPext = itemPextMap.getOrDefault(itemId, null);
            if (Objects.isNull(itemPext)) {
                reMap.put(itemId, "商品不存在");
            } else if (itemPext.getStatus().equals(ItemPext.Status.ITEM_DELETED.get())) {
                reMap.put(itemPext.getIid(), "商品已经删除");
            } else if (itemPext.getStatus().equals(ItemPext.Status.ITEM_OFFSHELF.get())) {
                reMap.put(itemPext.getIid(), "商品已经下架");
            } else if (itemPext.getStock() == 0) {
                reMap.put(itemPext.getIid(), "商品已经售罄");
            } else if (itemPext.getSaletype().equals(3)) {
                reMap.put(itemPext.getIid(), "商品是微众筹");
            }
        }
        return reMap;
    }

    /**
     * 删除直播资源
     *
     * @param entity
     * @param liveResources
     */
    default void deleteLiveResource(LiveEntity entity, List<LiveShowResource> liveResources) {
        LiveResourceRepo liveResourceRepo = SpringUtils.getBean("liveResourceRepo", LiveResourceRepo.class)
                .orElseThrow(() -> LiveException.failure("获取liveResourceRepo实例失败"));
        List<LiveResourcePO> pos = assambleLiveResource(entity.getId(), liveResources, LiveConst.STATUS_DETAIL_DEL);
        deleteResource(entity.getId(), liveResourceRepo, pos);

    }

    default void deleteResource(Long lid, LiveResourceRepo
            liveResourceRepo, List<LiveResourcePO> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            liveResourceRepo.deleteAllByLiveId(lid);
        }
        liveResourceRepo.batchDeleteResource(pos);
        //查询是否有主推的商品
        Long mainResourceId = liveResourceRepo.getMainResource(lid
                , LiveResourceType.ITEM.getCode());
        if (Objects.isNull(mainResourceId)) {
            return;
        }

        Set<Long> idSet = pos.stream().map(LiveResourcePO::getResourceId)
                .collect(Collectors.toSet());
        if (idSet.contains(mainResourceId)) {
            liveResourceRepo.mainResource(lid, mainResourceId
                    , LiveResourceType.ITEM.getCode(), LiveResourceEnum.Status.MAINNOT.getCode());
        }
    }


    /**
     * 组装直播资源
     *
     * @param lid       直播id
     * @param resources 资源
     * @return 资源po列表
     */
    default List<LiveResourcePO> assambleLiveResource(Long
                                                              lid, List<LiveShowResource> resources, Integer status) {
        List<LiveResourcePO> pos = new ArrayList<>();
        resources.forEach(resource -> {
            LiveResourcePO po = new LiveResourcePO();
            po.setCreateTime(Utils.getCurrentMillis());
            po.setModifiedTime(Utils.getCurrentMillis());
            po.setLiveId(lid);
            po.setResourceId(resource.getResourceId());
            po.setResourceType(resource.getResourceType());
            if (Objects.isNull(status)) {
                po.setStatus(resource.getStatus());
            } else {
                po.setStatus(status);
            }
            pos.add(po);
        });
        return pos;
    }

    default Map<Long, String> updateResource(LiveEntity
                                                     entity, List<LiveShowResource> liveResources) {
        Timestamp         currentTime = new Timestamp(Utils.getCurrentMillis());
        Map<Long, String> reMap       = new HashMap<>(3);
        LiveResourceRepo liveResourceRepo = SpringUtils.getBean("liveResourceRepo", LiveResourceRepo.class)
                .orElseThrow(() -> LiveException.failure("获取liveResourceRepo实例失败"));
        //如果上传的资源列表为空则删除直播关联的所有资源
        if (Utils.isEmpty(liveResources)) {
            //删除所有资源

            deleteResource(entity.getId(), liveResourceRepo, assambleLiveResource(entity.getId(), liveResources, LiveConst.STATUS_DETAIL_DEL));
        } else {
            List<LiveResourcePO> resourcePOS = assambleLiveResource(entity.getId(), liveResources, null);
            Map<Integer, List<LiveResourcePO>> resourceMap = resourcePOS.stream()
                    .collect(Collectors.groupingBy(LiveResourcePO::getResourceType));

            for (LiveResourceType type : LiveResourceType.values()) {
                List<LiveResourcePO> newDetails = resourceMap.get(type.getCode());
                if (Objects.isNull(newDetails)) {
                    if (Objects.equals(type.getCode(), LiveResourceType.ITEM.getCode())) {
                        liveResourceRepo.deleteAllByLiveId(entity.getId());
                    }
                    continue;
                }
                if (newDetails.isEmpty()) {
                    //删除对应类型的资源
                    liveResourceRepo.resourcesDeleteByLidAndType(entity.getId(), type.getCode(), currentTime.getTime());
                } else {
                    DBLiveResourceQuery query = DBLiveResourceQuery.builder()
                            .liveIds(Arrays.asList(entity.getId()))
                            .status(LiveConst.STATUS_DETAIL_NORMAL)
                            .resourceType(type.getCode())
                            .build();
                    List<LiveResourcePO> oldDetails = liveResourceRepo.list(query).stream().map(LiveResourceDTO::toDO).collect(Collectors.toList());
                    //需要删除的资源 old - new = toDelete
                    List<LiveResourcePO> need2Delete = new ArrayList<>(oldDetails);
                    need2Delete.removeAll(newDetails);
                    oldDetails.removeAll(need2Delete);
                    if (Objects.equals(type.getCode(), LiveResourceType.ITEM.getCode())) {
                        //获取资源的所有对应商品信息
                        Set<Long> itemIdSet = newDetails.stream().map(LiveResourcePO::getResourceId).collect(Collectors.toSet());
                        Set<Long> oldIdSet  = oldDetails.stream().map(LiveResourcePO::getResourceId).collect(Collectors.toSet());
                        itemIdSet.addAll(oldIdSet);
                        GemManager gemManager = SpringUtils.getBean("gemManager", GemManager.class).orElseThrow(() -> LiveException.failure("获取gemManager实例失败"));
                        Map<Long, ItemPext> itemPextMap = gemManager.takeBatchItem(itemIdSet).stream()
                                .collect(Collectors.toMap(ItemPext::getIid, i -> i, (k, v) -> v));
                        //最大权重为列表长度
                        int                      maxWeight         = newDetails.size();
                        Iterator<LiveResourcePO> iterator          = newDetails.iterator();
                        List<LiveResourcePO>     need2UpdateWeight = new ArrayList<>();
                        while (iterator.hasNext()) {
                            LiveResourcePO resourcePO = iterator.next();
                            Long           resourceId = resourcePO.getResourceId();
                            ItemPext       itemPext   = itemPextMap.getOrDefault(resourceId, null);
                            if (Objects.isNull(itemPext)) {
                                reMap.put(resourceId, "商品不存在");
                                if (oldIdSet.contains(resourceId)) {
                                    need2Delete.add(resourcePO);
                                }
                                iterator.remove();
                            } else if (itemPext.getStatus().equals(ItemPext.Status.ITEM_DELETED.get())) {
                                reMap.put(itemPext.getIid(), "商品已经删除");
                                if (oldIdSet.contains(resourceId)) {
                                    need2Delete.add(resourcePO);
                                }
                                iterator.remove();
                            } else if (itemPext.getStatus().equals(ItemPext.Status.ITEM_OFFSHELF.get())) {
                                reMap.put(itemPext.getIid(), "商品已经下架");
                                if (oldIdSet.contains(resourceId)) {
                                    need2Delete.add(resourcePO);
                                }
                                iterator.remove();
                            } else if (itemPext.getStock() == 0) {
                                reMap.put(itemPext.getIid(), "商品已经售罄");
                                if (oldIdSet.contains(resourceId)) {
                                    need2Delete.add(resourcePO);
                                }
                                iterator.remove();
                            } else if (itemPext.getSaletype().equals(3)) {
                                reMap.put(itemPext.getIid(), "商品是微众筹");
                                if (oldIdSet.contains(resourceId)) {
                                    need2Delete.add(resourcePO);
                                }
                                iterator.remove();
                            } else {
                                resourcePO.setWeight(maxWeight);
                                maxWeight--;
                                if (oldIdSet.contains(resourceId)) {
                                    need2UpdateWeight.add(resourcePO);
                                }
                            }
                        }
                        ConfigManager configManager = SpringUtils.getBean("configManager", ConfigManager.class)
                                .orElseThrow(() -> LiveException.failure("获取configManager实例失败"));
                        if (newDetails.size() >= configManager.getLiveResourceMaxNum()) {
                            throw LiveException.failure("直播资源数量不能大于" + configManager.getLiveResourceMaxNum() + "件");
                        }
                        if (CollectionUtils.isNotEmpty(need2Delete)) {
                            deleteResource(entity.getId(), liveResourceRepo, need2Delete);
                        }
//                        SpringUtils.sendEvent(new DismissedResourceEvent(pureLiveId, need2Delete));
                        //需要修改权重的资源 new in old
//                        for (PureLiveDetailDO detailDO : need2UpdateWeight) {
//                            detailDO.setLid(pureLiveId);
//                            detailDO.setModifiedTm(new Timestamp(Utils.getCurrentMillis()));
//                            pureLiveDetailMapper.updateWeight(detailDO);
//                        }
                        liveResourceRepo.updateWeight(need2UpdateWeight);
                        //需要增加的资源 new - old = toAdd
                        newDetails.removeAll(oldDetails);
                        if (CollectionUtils.isNotEmpty(newDetails)) {
                            liveResourceRepo.batchInsertResource(newDetails);
                        }
//                        batchResourceSave(pureLiveId, newDetails);
//                        SpringUtils.sendEvent(new RelatedResourceEvent(pureLiveId, newDetails));
                    } else {
                        Log LOGGER = LogFactory.getLog(LiveBaseRole.class);
                        LOGGER.info("newdetails:{},olddetails:{}", newDetails, oldDetails);
                        newDetails.removeAll(oldDetails);
                        if (CollectionUtils.isNotEmpty(newDetails)) {
                            liveResourceRepo.batchDeleteResource(need2Delete);
                            liveResourceRepo.batchInsertResource(newDetails);
                        }
//                        batchResourceSave(pureLiveId, newDetails);
                    }
                }
            }
        }
        return reMap;

    }

    default void resetStartTimeAndEndTime(Long liveId) {

        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class)
                .orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.resetStartTimeAndEndTime(liveId);

    }


    default LivePullUrlV getLivePullUrl(Long roomId) {
        LiveRoomRepo liveRoomRepo = SpringUtils.getBean("liveRoomRepo", LiveRoomRepo.class).orElseThrow(() -> LiveException.failure("获取直播间失败"));

        LivePullUrl  pullUrls     = liveRoomRepo.getPullUrls(roomId);
        LivePullUrlV livePullUrlV = null;
        if (pullUrls != null) {
            livePullUrlV = new LivePullUrlV();
            livePullUrlV.setFlvUrl(pullUrls.getFlvUrl());
            livePullUrlV.setHlsUrl(pullUrls.getHlsUrl());
            livePullUrlV.setRtmpUrl(pullUrls.getRtmpUrl());
        }
        return livePullUrlV;
    }
}

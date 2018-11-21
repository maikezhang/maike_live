package cn.idongjia.live.restructure.domain.entity.live;

import cn.idongjia.live.db.mybatis.po.LiveBookPO;
import cn.idongjia.live.db.mybatis.po.LivePurePO;
import cn.idongjia.live.db.mybatis.po.LiveResourcePO;
import cn.idongjia.live.db.mybatis.po.VideoCoverPO;
import cn.idongjia.live.pojo.live.LiveEnum;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.repo.*;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.util.Utils;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 纯直播角色
 *
 * @author lc
 * @create at 2018/6/6.
 */
public class PureLiveRole implements LiveBaseRole {
    /**
     * 创建 纯直播
     *
     * @param liveEntity
     */
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Throwable.class)
    @Override
    public void create(LiveEntity liveEntity) {
        //创建直播基本数据
        createBase(liveEntity);

        //对纯直播的标题做字数限制
        String title = liveEntity.getTitle();
        ConfigManager configManager = SpringUtils.getBean("configManager", ConfigManager.class).orElseThrow(() -> LiveException.failure("获取configManager实例失败"));
        if (Objects.nonNull(title) && title.length() > configManager.getLiveTileLength()) {
            throw LiveException.failure(configManager.getLiveTitleLengthText());
        }

        //创建纯直播数据
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.addPureLive(assambleLivePureDTO(liveEntity));
        //添加直播的小视频封面
        if (!Objects.isNull(liveEntity.getLiveVideoCover())) {
            VideoCoverRepo videoCoverRepo = SpringUtils.getBean("videoCoverRepo", VideoCoverRepo.class)
                    .orElseThrow(() -> LiveException.failure("获取videoCoverRepo实例失败"));
            videoCoverRepo.add(assambleVideoCoverDTO(liveEntity.getId(), liveEntity.getLiveVideoCover()));
        }

        //添加纯直播资源
        if (!CollectionUtils.isEmpty(liveEntity.getResource())) {
            LiveResourceRepo liveResourceRepo = SpringUtils.getBean("liveResourceRepo", LiveResourceRepo.class)
                    .orElseThrow(() -> LiveException.failure("获取liveResourceRepo实例失败"));
            liveResourceRepo.batchInsertResource(assambleLiveResource(liveEntity.getId(), liveEntity.getResource(), LiveConst.STATUS_DETAIL_NORMAL));
        }
        //如果该匠人被用户订阅则需要增加订阅关系
        if (!CollectionUtils.isEmpty(liveEntity.getLivePureBooks())) {
            LiveBookRepo liveBookRepo = SpringUtils.getBean("liveBookRepo", LiveBookRepo.class)
                    .orElseThrow(() -> LiveException.failure("获取liveBookRepo失败"));
            List<LivePureBook> livePureBooks = liveEntity.getLivePureBooks();
            livePureBooks.forEach(livePureBook -> {
                livePureBook.setLid(liveEntity.getId());
            });

            List<LiveBookPO> pos = livePureBooks.stream().map(x -> assambleLiveBookPO(x))
                    .collect(Collectors.toList());
            liveBookRepo.batchAddLiveBook(pos);

        }


    }

    @Override
    public void delete(long liveId) {

    }


    private VideoCoverDTO assambleVideoCoverDTO(Long lid, LiveVideoCover liveVideoCover) {
        VideoCoverPO po = new VideoCoverPO();
        po.setId(liveVideoCover.getId());
        po.setPic(liveVideoCover.getPic());
        po.setLiveId(lid);
        po.setDuration(liveVideoCover.getDuration());
        Long currentTime = Utils.getCurrentMillis();
        po.setCreateTime(currentTime);
        po.setUpdateTime(currentTime);
        po.setUrl(liveVideoCover.getUrl());
        VideoCoverDTO dto = new VideoCoverDTO(po);
        return dto;


    }

    private LivePureDTO assambleLivePureDTO(LiveEntity entity) {
        Integer status = 0;
        if (Objects.equals(entity.getStatus().getCode(), BaseEnum.DataStatus.DELETE_STATUS.getCode())) {
            status = BaseEnum.DataStatus.DELETE_STATUS.getCode();
        } else {
            status = entity.getOnline() == null ? null : entity.getOnline().getCode();
        }
        int weight = 0;
        if (!Objects.isNull(entity.getGeneralWeight())) {
            weight = entity.getGeneralWeight();
        }


        LivePurePO po = LivePurePO.builder()
                .id(entity.getId())
                .pic(entity.getPic())
                .desc(entity.getShowDesc())
                .status(status)
                .weight(weight)
                .timeStrategy(1L)
                .exemption(LiveConst.NOT_EXEMPTION)
                .build();
        LivePureDTO dto = new LivePureDTO(po);
        return dto;
    }


    /**
     * 更新
     *
     * @param oldEntity 更新前数据
     * @param newEntity 需要更新数据
     * @return true or  false
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean update(LiveEntity oldEntity, LiveEntity newEntity) {
        updatevalidate(oldEntity, newEntity);
        newEntity.setPreviewTime(newEntity.getEstimatedStartTime());
        //更新直播liveShow 数据
        updateLiveShow(oldEntity, newEntity);

        //更新纯直播数据
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.updatePureLive(assambleLivePureDTO(newEntity));

        //更新纯直播的视屏封面
        VideoCoverRepo videoCoverRepo = SpringUtils.getBean("videoCoverRepo", VideoCoverRepo.class)
                .orElseThrow(() -> LiveException.failure("获取videoCoverRepo实例失败"));
        if (!Objects.isNull(newEntity.getLiveVideoCover())) {
            videoCoverRepo.update(assambleVideoCoverDTO(newEntity.getId(), newEntity.getLiveVideoCover()));
        } else {
            videoCoverRepo.delete(newEntity.getId());
        }

        //更新直播资源
        if (CollectionUtils.isNotEmpty(newEntity.getResource())) {
            updateResource(oldEntity, newEntity.getResource());
        }

        return true;
    }

    private LiveShowResource liveResourcePO2Resource(LiveResourcePO po) {
        LiveShowResource resource = new LiveShowResource();
        resource.setId(po.getId());
        resource.setCreateTm(new Timestamp(po.getCreateTime()));
        resource.setLid(po.getLiveId());
        resource.setModifiedTm(new Timestamp(po.getModifiedTime()));
        resource.setResourceId(po.getResourceId());
        resource.setResourceType(po.getResourceType());
        resource.setStatus(po.getStatus());
        resource.setWeight(po.getWeight());
        return resource;
    }


//    /**
//     * 更新直播资源
//     * @param lid 更新的直播lid
//     * @param resources 需要更新的资源
//     */
//    private void updateResource(Long lid, List<LiveShowResource> resources){
//
//        //获取更新前的资源列表
//        LiveResourceRepo liveResourceRepo=SpringUtils.getBean("liveResourceRepo",LiveResourceRepo.class)
//                .orElseThrow(()->LiveException.failure("获取liveResourceRepo实例失败"));
//        DBLiveResourceQuery query=DBLiveResourceQuery.builder()
//                .lids(Arrays.asList(lid))
//                .type(LiveResourceType.ITEM.getCode())
//                .status(ResourceStatus.NORMAL.getCode())
//                .build();
//        List<LiveShowResource> oldResource=liveResourceRepo.list(query).stream().map(LiveResourceDTO::toDO).map(this::liveResourcePO2Resource).collect(Collectors.toList());
//
//        Timestamp currentTime = new Timestamp(Utils.getCurrentMillis());
//        Map<Long, String> reMap = new HashMap<>(3);
//        //如果上传的资源列表为空则删除直播关联的所有资源
//        if (Utils.isEmpty(resources)){
//            //删除所有资源
//            liveResourceRepo.delByLid(lid);
//            //删除主推
//            Long mainResourceId = liveResourceRepo.getMainResource(lid
//                    , LiveResourceType.ITEM.getCode());
//            Set<Long> idSet = oldResource.stream().map(LiveShowResource::getResourceId)
//                    .collect(Collectors.toSet());
//            if (idSet.contains(mainResourceId)){
//                liveResourceRepo.mainResource(lid, mainResourceId
//                        , LiveResourceType.ITEM.getCode(), LiveResourceEnum.Status.MAINNOT.getCode());
//            }
//        }else{
//            Map<Integer, List<LiveShowResource>> resourceMap = resources.stream()
//                    .collect(Collectors.groupingBy(LiveShowResource::getResourceType));
//            for (LiveResourceType type : LiveResourceType.values()){
//                List<LiveShowResource> newDetails = resourceMap.get(type.getCode());
//                if (Objects.isNull(newDetails)){
//                    continue;
//                }
//                if (newDetails.isEmpty()){
//                    //删除对应类型的资源
//                    liveResourceRepo.resourcesDeleteByLidAndType(lid,type.getCode(),currentTime.getTime());
//                }else{
////                    List<PureLiveDetailDO> oldDetails = listResourcesByLidAndType(pureLiveId, type.getCode());
//                    //需要删除的资源 old - new = toDelete
//                    List<LiveShowResource> need2Delete = new ArrayList<>(oldResource);
//                    need2Delete.removeAll(newDetails);
//                    oldResource.removeAll(need2Delete);
//                    if (Objects.equals(type.getCode(), LiveResourceType.ITEM.getCode())){
//                        //获取资源的所有对应商品信息
//                        Set<Long> itemIdSet = newDetails.stream().map(LiveShowResource::getResourceId).collect(Collectors.toSet());
//                        Set<Long> oldIdSet = oldResource.stream().map(LiveShowResource::getResourceId).collect(Collectors.toSet());
//                        itemIdSet.addAll(oldIdSet);
//                        Map<Long, ItemPext> itemPextMap = gemManager.takeBatchItem(itemIdSet).stream()
//                                .collect(Collectors.toMap(ItemPext::getIid, i -> i, (k, v) -> v));
//                        //最大权重为列表长度
//                        int maxWeight = newDetails.size();
//                        Iterator<PureLiveDetailDO> iterator = newDetails.iterator();
//                        List<PureLiveDetailDO> need2UpdateWeight = new ArrayList<>();
//                        while (iterator.hasNext()){
//                            PureLiveDetailDO detailDO = iterator.next();
//                            Long resourceId = detailDO.getResourceId();
//                            ItemPext itemPext = itemPextMap.getOrDefault(resourceId, null);
//                            if (Objects.isNull(itemPext)){
//                                reMap.put(resourceId, "商品不存在");
//                                if (oldIdSet.contains(resourceId)) {
//                                    need2Delete.persist(detailDO);
//                                }
//                                iterator.remove();
//                            }else if (itemPext.getStatus().equals(ItemPext.Status.ITEM_DELETED.get())) {
//                                reMap.put(itemPext.getIid(), "商品已经删除");
//                                if (oldIdSet.contains(resourceId)) {
//                                    need2Delete.persist(detailDO);
//                                }
//                                iterator.remove();
//                            } else if (itemPext.getStatus().equals(ItemPext.Status.ITEM_OFFSHELF.get())) {
//                                reMap.put(itemPext.getIid(), "商品已经下架");
//                                if (oldIdSet.contains(resourceId)) {
//                                    need2Delete.persist(detailDO);
//                                }
//                                iterator.remove();
//                            } else if (itemPext.getStock() == 0) {
//                                reMap.put(itemPext.getIid(), "商品已经售罄");
//                                if (oldIdSet.contains(resourceId)) {
//                                    need2Delete.persist(detailDO);
//                                }
//                                iterator.remove();
//                            } else if (itemPext.getSaletype().equals(3)) {
//                                reMap.put(itemPext.getIid(), "商品是微众筹");
//                                if (oldIdSet.contains(resourceId)) {
//                                    need2Delete.persist(detailDO);
//                                }
//                                iterator.remove();
//                            } else{
//                                detailDO.setWeight(maxWeight);
//                                maxWeight --;
//                                if (oldIdSet.contains(resourceId)){
//                                    need2UpdateWeight.persist(detailDO);
//                                }
//                            }
//                        }
//                        if (newDetails.size() >= configManager.getLiveResourceMaxNum()){
//                            throw LiveException.failure("直播资源数量不能大于" + configManager.getLiveResourceMaxNum() + "件");
//                        }
//                        deleteMain(pureLiveId, need2Delete, currentTime);
//                        SpringUtils.sendEvent(new DismissedResourceEvent(pureLiveId, need2Delete));
//                        //需要修改权重的资源 new in old
//                        for (PureLiveDetailDO detailDO : need2UpdateWeight) {
//                            detailDO.setLid(pureLiveId);
//                            detailDO.setModifiedTm(new Timestamp(Utils.getCurrentMillis()));
//                            pureLiveDetailMapper.updateWeight(detailDO);
//                        }
//                        //需要增加的资源 new - old = toAdd
//                        newDetails.removeAll(oldDetails);
//                        batchResourceSave(pureLiveId, newDetails);
//                        SpringUtils.sendEvent(new RelatedResourceEvent(pureLiveId, newDetails));
//                    }else{
//                        newDetails.removeAll(oldDetails);
//                        batchResourceSave(pureLiveId, newDetails);
//                    }
//                }
//            }
//
//
//
//
//    }
//
//    private void deleteMain(Long pureLiveId, List<PureLiveDetailDO> oldDetails, Timestamp currentTime){
//        if (! Utils.isEmpty(oldDetails)) {
//            pureLiveDetailMapper.batchDelete(pureLiveId, oldDetails, currentTime);
//            //需要删除对应主推关系
//            Long mainResourceId = liveResourceRepo.getMainResource(pureLiveId
//                    , LiveResourceType.ITEM.getCode());
//            Set<Long> idSet = oldDetails.stream().map(PureLiveDetailDO::getResourceId)
//                    .collect(Collectors.toSet());
//            if (idSet.contains(mainResourceId)){
//                liveResourceRepo.mainResource(pureLiveId, mainResourceId
//                        , LiveResourceType.ITEM.getCode(), LiveResourceEnum.Status.MAINNOT.getCode());
//            }
//        }
//    }

    private void updatevalidate(LiveEntity oldEntity, LiveEntity newEntity) {

        //对纯直播的标题做字数限制
        String title = newEntity.getTitle();
        ConfigManager configManager = SpringUtils.getBean("configManager", ConfigManager.class).orElseThrow(() -> LiveException.failure("获取configManager实例失败"));
        if (Objects.nonNull(title) && title.length() > configManager.getLiveTileLength()) {
            throw LiveException.failure(configManager.getLiveTitleLengthText());
        }


        Long currentTime = Utils.getCurrentMillis();
        if (Objects.nonNull(newEntity.getEstimatedStartTime()) && Objects.nonNull(newEntity.getEstimatedEndTime())) {
            if (newEntity.getEstimatedEndTime() < currentTime) {
                throw LiveException.failure("预计结束时间必须大于当前时间");
            }
            if (newEntity.getEstimatedStartTime() >= newEntity.getEstimatedEndTime()) {
                throw LiveException.failure("预计开始时间必须小于预计结束时间");
            }
        }
        if (Objects.equals(oldEntity.getState().getCode(), LiveEnum.LiveState.FINISHED.getCode())) {
            throw LiveException.failure("不能修改已结束直播");
        }
        if (Objects.equals(oldEntity.getState().getCode(), LiveEnum.LiveState.UNSTART.getCode())) {
            if (Objects.equals(oldEntity.getOnline(), LiveEnum.LiveOnline.ONLINE)) {
                throw LiveException.failure("上线后不能修改直播介绍");
            }
            if (!Objects.equals(oldEntity.getScreenDirection(), newEntity.getScreenDirection())) {
                throw LiveException.failure("上线后不能修改直播屏幕方向");
            }
        }
        if (Objects.equals(oldEntity.getState().getCode(), LiveEnum.LiveState.PLAYING.getCode())) {
            if (!Objects.equals(oldEntity.getEstimatedStartTime(), newEntity.getEstimatedStartTime())) {
                throw LiveException.failure("直播开始后不能修改直播预播时间");
            }
            if (!Objects.equals(oldEntity.getShowDesc(), newEntity.getShowDesc())) {
                throw LiveException.failure("直播开始后不能修改直播介绍");
            }
            if (!Objects.equals(oldEntity.getScreenDirection(), newEntity.getScreenDirection())) {
                throw LiveException.failure("直播开始后不能修改直播屏幕方向");
            }

        } else {
            if (newEntity.getEstimatedStartTime() < currentTime) {
                throw LiveException.failure("预计开始时间必须大于当前时间");
            }
        }
    }

    /**
     * 纯直播上下线
     *
     * @param entity 更新前的直播实体
     * @param online 上下线状态
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateLiveOnline(LiveEntity entity, Integer online) {
        updateLiveShowOnline(entity.getId(), online);
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        liveShowRepo.updatePureLiveOnline(entity.getId(), online);


    }
}

package cn.idongjia.live.restructure.query;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.gem.lib.pojo.Item;
import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.gem.lib.query.ItemSearch;
import cn.idongjia.live.db.mybatis.po.LiveResourceCountPO;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.dto.ItemPExtDTO;
import cn.idongjia.live.restructure.dto.LiveResourceCountDTO;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.manager.GemManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.repo.LiveResourceRepo;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.restructure.v2.support.enumeration.LiveResourceType;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.v2.pojo.ItemResource;
import cn.idongjia.live.v2.pojo.ItemResourcePackage;
import cn.idongjia.live.v2.pojo.query.ResourceSearch;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class LiveResourceQueryHandler {
    private static final Log              logger = LogFactory.getLog(LiveResourceQueryHandler.class);
    @Resource
    private              LiveResourceRepo liveResourceRepo;

    @Resource
    private ItemQueryHandler itemQueryHandler;

    @Resource
    private LiveShowRepo liveShowRepo;

    @Resource
    private GemManager gemManager;

    @Resource
    private UserManager userManager;

    @Async
    public Future<List<LiveResourceDTO>> list(DBLiveResourceQuery dbLiveResourceQuery) {
        List<LiveResourceDTO> liveResourceDTOS = liveResourceRepo.list(dbLiveResourceQuery);
        return new AsyncResult<>(liveResourceDTOS);
    }

    @Async
    public Future<Map<Long, List<LiveResourceDTO>>> map(DBLiveResourceQuery dbLiveResourceQuery) {
        List<LiveResourceDTO>            liveResourceDTOS   = liveResourceRepo.list(dbLiveResourceQuery);
        Map<Long, List<LiveResourceDTO>> liveResourceDTOMap = liveResourceDTOS.stream().collect(Collectors.groupingBy(LiveResourceDTO::getLiveId));
        return new AsyncResult(liveResourceDTOMap);
    }


    public Map<Long, LiveResourceCountDTO> groupCountMap(DBLiveResourceQuery dbLiveResourceQuery) {
        List<LiveResourceCountPO> liveResourceCountPOS = liveResourceRepo.groupCount(dbLiveResourceQuery);
        return liveResourceCountPOS.stream().collect(Collectors.toMap(LiveResourceCountPO::getLiveId, v1 -> {
            LiveResourceCountDTO liveResourceCountDTO = new LiveResourceCountDTO(v1);
            return liveResourceCountDTO;
        }, (v1, v2) -> v1));
    }


    /**
     * 获取纯直播关联超级模板id
     *
     * @param liveShowId
     * @return
     */
    public Long getTemplateId(Long liveShowId) {
        Long templateId = null;
        DBLiveResourceQuery query = DBLiveResourceQuery.builder()
                .liveIds(Arrays.asList(liveShowId))
                .status(LiveConst.STATUS_DETAIL_NORMAL)
                .resourceType(LiveResourceType.TEMPLATE.getCode())
                .build();
        try {
            List<LiveResourceDTO> list = list(query).get();
            if (!CollectionUtils.isEmpty(list)) {
                templateId = list.get(0).getResourceId();
            }

        } catch (Exception e) {
            logger.warn("查询直播资源失败{}", e);
            throw LiveException.failure("查询直播资源失败");
        }
        return templateId;

    }

    public List<Map<String, Object>> urlMap(List<Long> resourceIds, Integer resourceType) {
        return liveResourceRepo.urlMap(resourceIds, resourceType);
    }


    public List<ItemResource> listItemResource(Long liveId) {
        DBLiveResourceQuery dbLiveResourceQuery = DBLiveResourceQuery.builder().status(LiveConst.STATUS_DETAIL_NORMAL).resourceType(LiveResourceType.ITEM.getCode()).liveIds(Arrays.asList(liveId))
                .build();
        try {
            List<LiveResourceDTO> liveResourceDTOS = list(dbLiveResourceQuery).get();
            List<Long>            resourceIds      = liveResourceDTOS.stream().map(LiveResourceDTO::getResourceId).collect(Collectors.toList());
            List<ItemPext>        itemPexts        = itemQueryHandler.list(resourceIds);
            Map<Long, ItemPext>   itemPextMap      = itemPexts.stream().collect(Collectors.toMap(itemPext -> itemPext.getIid(), itemPext -> itemPext, (u1, u2) -> u1));
            Long                  mainResourceId   = liveResourceRepo.getMainResource(liveId, LiveResourceType.ITEM.getCode());
            return liveResourceDTOS.stream().map(liveResourceDTO -> {
                return liveResourceDTO.toItemResource(itemPextMap.get(liveResourceDTO.getResourceId()), mainResourceId);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("查询异常{}", e);
            return null;
        }
    }

    public ItemResourcePackage listCraftsItems(ResourceSearch resourceSearch) {
        ItemResourcePackage itemResourcePackage = new ItemResourcePackage();
        //获取匠人商品
        ItemSearch itemSearch = buildCraftsItemSearch(resourceSearch);
        itemSearch.setOrderBy("updatetm desc");

        try {
            List<ItemPext> itemPexts = itemQueryHandler.list(itemSearch);
            if (itemPexts != null && itemPexts.size() > 0) {
                List<ItemResource> allResource = itemPexts.stream().map(itemPext -> {
                    ItemPExtDTO itemPExtDTO = new ItemPExtDTO(itemPext);
                    return itemPExtDTO.toItemResources();
                }).collect(Collectors.toList());
                itemResourcePackage.setAll(allResource);
            }

            //已关联的商品列表
            if (resourceSearch.getLid() != null) {
                DBLiveResourceQuery dbLiveResourceQuery = DBLiveResourceQuery.builder().status(LiveConst.STATUS_DETAIL_NORMAL).resourceType(LiveResourceType.ITEM.getCode()).liveIds(Arrays.asList
                        (resourceSearch.getLid())).build();

                List<LiveResourceDTO> liveResourceDTOS   = list(dbLiveResourceQuery).get();
                List<Long>            relatedResourceIds = liveResourceDTOS.stream().map(LiveResourceDTO::getResourceId).collect(Collectors.toList());
                itemResourcePackage.setRelated(relatedResourceIds);
            }

        } catch (Exception e) {
            logger.error("查询商品失败{}", e);
        }


        return itemResourcePackage;
    }

    public ItemSearch buildCraftsItemSearch(ResourceSearch resourceSearch) {
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setUid(resourceSearch.getCraftsUid()); //匠人id
        itemSearch.setPage(resourceSearch.getPage());
        itemSearch.setLimit(resourceSearch.getLimit());
        itemSearch.setStockMin(0); // >最小库存数
        itemSearch.setStatus(Item.Status.ITEM_ONSHELF.get()); //上架的商品
        itemSearch.setSaleType(Item.SaleType.GOODS.get()); //普通商品
        return itemSearch;
    }

    public List<ItemResource> getSelectedItems(Long liveId) {
        //获取已关联的商品
        DBLiveResourceQuery dbLiveResourceQuery = DBLiveResourceQuery.builder().status(LiveConst.STATUS_DETAIL_NORMAL).resourceType(LiveResourceType.ITEM.getCode()).liveIds(Arrays.asList(liveId))
                .build();
        List<ItemResource> resources = new ArrayList<>();
        try {
            List<LiveResourceDTO> liveResourceDTOS = list(dbLiveResourceQuery).get();
            //获取当前主推商品
            Long mainResourceId = liveResourceRepo.getMainResource(liveId, LiveResourceType.ITEM.getCode());
            //批量获取商品
            List<Long>          iids        = liveResourceDTOS.stream().map(LiveResourceDTO::getResourceId).collect(Collectors.toList());
            List<ItemPext>      itemPexts   = itemQueryHandler.list(iids);
            Map<Long, ItemPext> itemPextMap = itemPexts.stream().collect(Collectors.toMap(itemPext -> itemPext.getIid(), itemPext -> itemPext, (u1, u2) -> u1));
            //按顺序返回
            liveResourceDTOS.stream().forEach(liveResourceDTO -> {
                ItemResource itemResource = liveResourceDTO.toItemResource(itemPextMap.get(liveResourceDTO.getResourceId()), mainResourceId);
                resources.add(itemResource);
            });

        } catch (Exception e) {
            logger.error("查询商品异常{}", e);
        }
        return resources;

    }

    public Map<String, Object> mapItemResource(long liveId) {
        LiveShowDTO liveShowDTO = liveShowRepo.getById(liveId);
        if (Objects.isNull(liveShowDTO)) {
            return new HashMap<>(1);
        }
        Map<String, Object> reMap = new HashMap<>(5);
        reMap.put("liveTitle", liveShowDTO.getTitle());
        reMap.put("hostId", liveShowDTO.getUserId());
        List<ItemResource> itemResources = listItemResource(liveId);
        Set<Long>          itemIdSet     = itemResources.stream().map(ItemResource::getResourceId).collect(Collectors.toSet());
        Set<ItemPext>      itemPextSet   = gemManager.takeBatchItem(itemIdSet);
        Map<Long, ItemPext> itemPextMap = itemPextSet.stream()
                .collect(Collectors.toMap(ItemPext::getIid, i -> i, (k, v) -> k));
        List<Long>      userIds = itemPextSet.stream().map(ItemPext::getUid).collect(Collectors.toList());
        Map<Long, User> userMap = userManager.takeBatchUser(userIds);
        reMap.put("state", liveShowDTO.getState());
        for (ItemResource resource : itemResources) {
            ItemPext itemPext = itemPextMap.get(resource.getResourceId());
            if (Objects.nonNull(itemPext)) {
                User user = userMap.get(itemPext.getUid());
                if (Objects.nonNull(user)) {
                    resource.setCraftsName(user.getUsername());
                }
            }
        }
        reMap.put("resources", itemResources);
        return reMap;
    }

}

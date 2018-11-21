package cn.idongjia.live.restructure.repo;


import cn.idongjia.consts.TokenType;
import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.db.mybatis.mapper.LiveResourceMapper;
import cn.idongjia.live.db.mybatis.po.LiveResourceCountPO;
import cn.idongjia.live.db.mybatis.po.LiveResourcePO;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.proto.LiveProto;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.exception.PureLiveException;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.GemManager;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.redis.ResourceRedis;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.LiveUtil;
import cn.idongjia.live.support.enumeration.LiveResourceType;
import cn.idongjia.live.v2.pojo.LiveResourceEnum;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import cn.idongjia.zoo.proto.ZooProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.LiveConst.STATUS_DETAIL_NORMAL;
import static cn.idongjia.util.GsonUtil.GSON;
import static cn.idongjia.util.Utils.isEmpty;

@Component
public class LiveResourceRepo {
    private static final Log LOGGER = LogFactory.getLog(LiveResourceRepo.class);

    @Resource
    private UserManager userManager;
    @Resource
    private MqProducerManager mqProducerManager;
    @Resource
    private GemManager gemManager;

    @Autowired
    private ResourceRedis resourceRedis;

    @Autowired
    private LiveShowRepo liveShowRepo;
    @Resource
    private LiveResourceMapper liveResourceMapper;
    @Resource
    private ConfigManager configManager;

    public List<LiveResourceDTO> list(DBLiveResourceQuery dbLiveResourceQuery) {
        List<LiveResourcePO> liveResourcePOS = liveResourceMapper.list(dbLiveResourceQuery);
        return liveResourcePOS.stream().map(LiveResourceDTO::new).collect(Collectors.toList());
    }

    /**
     * 设置资源是否主推
     *@author zhangyingjie
     * @param lid
     * @param rid
     * @param rtype
     * @param status
     * @return
     */
    public boolean mainResource(Long lid, Long rid, Integer rtype, Integer status) {

        //设置主推
        if (LiveResourceEnum.Status.MAIN.getCode() == status) {
            Map<String, String> resourceMap = resourceRedis.takeRedis(String.valueOf(lid)).orElse(new HashMap<>());
            resourceMap.put(String.valueOf(rtype), String.valueOf(rid));
            resourceRedis.putRedis(String.valueOf(lid), resourceMap);
        } else {//取消主推
            resourceRedis.delField(String.valueOf(lid), String.valueOf(rtype));
        }
        //推送
        pushMainResource(lid, rid, rtype, status);
        return false;
    }

    /**
     * @author zhangyingjie
     * @param lid
     * @param rtype
     * @return
     */
    public Long getMainResource(Long lid, Integer rtype) {
        Map<String, String> resourceMap = resourceRedis.takeRedis(String.valueOf(lid)).orElse(new HashMap<>());
        if (null != resourceMap.get(String.valueOf(rtype))) {
            return Long.valueOf(resourceMap.get(String.valueOf(rtype)));
        }
        return null;
    }


    /**
     * @author zhangyingjie
     * @param lid
     * @param rid
     * @param rtype
     * @param status
     */
    private void pushMainResource(Long lid, Long rid, Integer rtype, Integer status) {
        LiveShowDTO liveShow = liveShowRepo.getById(lid);
        ZooProto.ZooPack.Builder zooBuilder = ZooProto.ZooPack.newBuilder();
        if (LiveResourceEnum.Status.MAIN.getCode() == status) {
            LiveProto.LiveMainResourcePush.Builder builder = LiveProto.LiveMainResourcePush.newBuilder();
            ItemPext itemPext = gemManager.takeItem(rid).orElseThrow(() -> PureLiveException.RESOURCE_NOT_FOUND);
            Anchor anchor = userManager.getAnchorByUid(liveShow.getUserId());
            builder.setResourceId(rid);
            builder.setLiveId(lid);
            builder.setResourceType(rtype);
            builder.setPic(getPic(itemPext.getPictures()));
            builder.setPrice(LiveUtil.itemPrice2Int(itemPext.getPrice()));
            builder.setTitle(itemPext.getTitle());
            builder.setUsername(null != anchor ? anchor.getHusername() : "");
            zooBuilder.setSerialized(builder.build().toByteString());
            zooBuilder.setService(LiveProto.LiveServiceType.LIVE_MAIN_RESOURCE_VALUE);
        } else {//取消主推
            LiveProto.LiveRemoveMainResourcePush.Builder builder = LiveProto.LiveRemoveMainResourcePush.newBuilder();
            builder.setLiveId(lid);
            builder.setResourceType(rtype);
            builder.setResourceId(rid);
            zooBuilder.setSerialized(builder.build().toByteString());
            zooBuilder.setService(LiveProto.LiveServiceType.LIVE_CANCEL_MAIN_RESOURCE_VALUE);
        }
        zooBuilder.setTime(Utils.getCurrentSecond());
        mqProducerManager.broadCastMessage2Zoo(liveShow.getZooId(), zooBuilder.build());
    }

    private String getPic(String pics) {
        if (!isEmpty(pics)) {
            List<String> picList = GSON.fromJson(pics, TokenType.LIST_STRING_TYPE);
            if (!isEmpty(picList)) {
                return picList.get(0);
            }
        }
        return "";
    }


    /**
     * 根据纯直播ID获取其超级模版ID
     *
     * @param liveId 纯直播ID
     * @return 超级模版ID
     */
    public Long getTemplateId(Long liveId) {
        // 构建纯直播详情检索条件
        DBLiveResourceQuery dbLiveResourceQuery = DBLiveResourceQuery.builder().resourceType(LiveResourceType.TEMPLATE.getCode()).liveIds(Arrays.asList(liveId)).status(STATUS_DETAIL_NORMAL).build();
        List<LiveResourcePO> liveResourcePOS = liveResourceMapper.list(dbLiveResourceQuery);
        if (Utils.isEmpty(liveResourcePOS)) {
            return null;
        } else {
            return liveResourcePOS.get(0).getResourceId();
        }
    }

    /**
     * 根据资源ID列表获取资源详情
     *
     * @param resourceIds 资源ID列表
     * @return 详情列表
     */
    public List<Map<String, Object>> urlMap(List<Long> resourceIds, int resourceType) {
        if (resourceIds == null) {
            return new ArrayList<>();
        }
        if (resourceIds.size() == 0) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> liveDetailApis = new ArrayList<>();
        DBLiveResourceQuery dbLiveResourceQuery = DBLiveResourceQuery.builder().liveIds(resourceIds).resourceType(resourceType).build();
        List<LiveResourcePO> liveResourcePOS = liveResourceMapper.list(dbLiveResourceQuery);
        Map<Integer, LiveResourcePO> map = liveResourcePOS.stream().collect(Collectors.toMap(LiveResourcePO::getResourceType, v1 -> v1, (v1, v2) -> v1));
        LiveResourcePO detailDO = map.get(LiveResourceType.TEMPLATE.getCode());
        Map<String, Object> reMap = new HashMap<>(1);
        if (Objects.nonNull(detailDO) && Objects.nonNull(detailDO.getResourceId())) {
            // 根据超级模版ID组装H5链接
            reMap.put("url", LiveUtil.assembleH5Url(detailDO.getResourceId().toString()));
            liveDetailApis.add(reMap);
        } else {
            reMap.put("url", configManager.getDefaultH5Url());
            liveDetailApis.add(reMap);
        }
        return liveDetailApis;
    }



    /**
     * 批量添加直播资源
     *
     * @param liveResourcePOS 资源数据
     * @author zhangyingjie
     */
    public void batchInsertResource(List<LiveResourcePO> liveResourcePOS){
        Integer weight=liveResourcePOS.size();
        for (LiveResourcePO po:liveResourcePOS){
            po.setWeight(weight);
            po.setStatus(LiveConst.STATUS_DETAIL_NORMAL);
            weight--;
        }
        liveResourceMapper.batchInsert(liveResourcePOS);


    }

    /**
     * 批量删除
     * @author zhangyingjie
     * @param liveResourcePOS
     */
    public void batchDeleteResource(List<LiveResourcePO> liveResourcePOS){
       if(CollectionUtils.isEmpty(liveResourcePOS)){
           return;
       }
        for (LiveResourcePO po:liveResourcePOS){
            liveResourceMapper.deleteResource(po,Utils.getCurrentMillis());
        }
    }

    public void deleteAllByLiveId(Long liveId){
        liveResourceMapper.deleteAllByLiveId(liveId,Utils.getCurrentMillis());
    }

    /**
     * 更新权重
     * @author zhangyingjie
     * @param liveResourcePOS
     */
    public void updateWeight(List<LiveResourcePO> liveResourcePOS){

        liveResourcePOS.forEach(liveResource->{
            liveResource.setModifiedTime(Utils.getCurrentMillis());
        });
        for (LiveResourcePO po:liveResourcePOS){
            liveResourceMapper.updateWeight(po,Utils.getCurrentMillis());
        }
    }

    /**
     * @author zhangyingjie
     * @param liveId
     * @param type
     * @param currentTime
     * @return
     */
    public boolean resourcesDeleteByLidAndType(Long liveId,Integer type,Long currentTime){
        LiveResourcePO po=new LiveResourcePO();
        po.setLiveId(liveId);
        po.setStatus(LiveConst.STATUS_DETAIL_DEL);
        po.setResourceType(type);
        return liveResourceMapper.update(po,currentTime)>0;
    }








    public List<LiveResourceCountPO> groupCount(DBLiveResourceQuery dbLiveResourceQuery) {
        return liveResourceMapper.countGroup(dbLiveResourceQuery);
    }
}

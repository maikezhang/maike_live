package cn.idongjia.live.restructure.manager;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.domain.entity.zoo.ZooCount;
import cn.idongjia.live.support.AssembleProto;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.mq.topic.ZooTopic;
import cn.idongjia.util.Utils;
import cn.idongjia.zoo.api.ZooCountService;
import cn.idongjia.zoo.api.ZooMessageService;
import cn.idongjia.zoo.api.ZooService;
import cn.idongjia.zoo.pojo.Zoo;
import cn.idongjia.zoo.pojo.ZooMessage;
import cn.idongjia.zoo.proto.ZooProto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by zhang on 2017/2/19.
 * 对聊天室接口封装
 */
@Component
public class ZooManager {

    private static final Log LOGGER = LogFactory.getLog(ZooManager.class);
    @Resource
    private ZooService zooService;
    @Resource
    private ZooCountService zooCountService;
    @Resource
    private ZooMessageService zooMessageService;
    @Resource
    private MqProducerManager mqProducerManager;
    @Resource
    private UserManager userManager;

    /**
     * 调用接口创建聊天室
     *
     * @param title 聊天室名字
     * @param zrc   聊天室基准随机人数
     * @param suid  聊天室管理员ID
     * @return 聊天室ID
     */
    public Long addZooRoom(String title, Integer zrc, Long suid) {
        if (suid == null) {
            throw new LiveException(-12138, "聊天室管理员不能为空");
        }
        Zoo zoo = buildZooInstance(null, title, zrc, suid);
        Zoo zooAfterInsert;
        try {
            zooAfterInsert = zooService.insertZoo(zoo);
        } catch (Exception e) {

            ZooManager.LOGGER.warn("创建聊天室服务失败" + e.getMessage());
            throw new LiveException(-12138, "创建聊天室服务失败");
        }
        if (zooAfterInsert == null) {
            ZooManager.LOGGER.warn("无法创建聊天室");
            throw new LiveException(-12138, "无法创建聊天室");
        }
        return zooAfterInsert.getZid();
    }

    /**
     * 调用聊天室接口更新聊天室信息
     *
     * @param zooRoomId 聊天室ID
     * @param title     聊天室标题
     * @param zrc       聊天室基准随机数
     * @param suid      聊天室管理员ID
     */
    public void modifyZooRoom(Long zooRoomId, String title, Integer zrc, Long suid) {
        if (zooRoomId == null) {
            ZooManager.LOGGER.warn("更新聊天室ID为空");
            throw new LiveException(-12138, "更新聊天室ID为空");
        }
        if (zrc == null) {
            return;
        }
        try {
            zooService.updateZoo(buildZooInstance(zooRoomId, title, zrc, suid));
        } catch (Exception e) {

            ZooManager.LOGGER.warn("更新聊天室服务失败" + e.getMessage());
            throw new LiveException(-12138, "更新聊天室服务失败");
        }
    }

    /**
     * 调用zooService来获取聊天室信息
     *
     * @param zooRoomId 聊天室ID
     * @return 聊天室信息
     */
    public Zoo aquireZooRoom(Long zooRoomId) {
        if (zooRoomId == null) {
            ZooManager.LOGGER.warn("聊天室ID为空");
            throw new LiveException(-12138, "聊天室ID为空");
        }
        Zoo zoo;
        try {
            zoo = zooService.getZoo(zooRoomId);
        } catch (Exception e) {

            ZooManager.LOGGER.warn("获取聊天室服务失败" + e.getMessage());
            throw new LiveException(-12138, "获取聊天室服务失败");
        }
        if (zoo == null) {
            ZooManager.LOGGER.warn(zooRoomId + " 聊天室信息为空");
            throw new LiveException(-12138, "聊天室信息为空");
        }
        return zoo;
    }

    /**
     * 调用zooCountService来开启聊天室记录历史人数功能
     *
     * @param zooRoomId 直播聊天室ID
     */
    public void turnOnZooRoomRecord(Long zooRoomId) {
        try {
            zooCountService.startRecordHistory(zooRoomId);
        } catch (Exception e) {

            ZooManager.LOGGER.warn("开启聊天室历史记录服务失败" + e.getMessage());
            throw new LiveException(-12138, "开启聊天室历史记录服务失败");
        }
    }

    /**
     * 调用zooCountService来关闭聊天室记录历史人数功能
     *
     * @param zooRoomId 直播聊天室ID
     */
    public void turnOffZooRoomRecord(Long zooRoomId) {
        try {
            zooCountService.stopRecordHistory(zooRoomId);
        } catch (Exception e) {

            ZooManager.LOGGER.warn("关闭聊天室历史记录服务失败" + e.getMessage());
            throw new LiveException(-12138, "关闭聊天室历史记录服务失败");
        }
    }

    /**
     * 调用zooCountService来开启聊天室随机人数变化功能
     *
     * @param zooRoomId 直播聊天室ID
     * @param target    增到的目标数值
     */
    private void turnOnZooRoomUserCountToTarget(Long zooRoomId, Integer target) {
        try {
            zooCountService.turnRandomCountGradually(zooRoomId, target);
        } catch (Exception e) {

            ZooManager.LOGGER.warn("聊天室随机人数变化服务失败" + e.getMessage());
            throw new LiveException(-12138, "聊天室随机人数变化服务失败");
        }
    }

    /**
     * 开始直播时，增长观看人数到基准随机数
     *
     * @param zooRoomId 聊天室ID
     */
    public void turnZooRoomUserCountToZrc(Long zooRoomId) {
        Zoo zoo = aquireZooRoom(zooRoomId);
        turnOnZooRoomUserCountToTarget(zooRoomId, zoo.getZrc());
    }

    /**
     * 开始直播时，增长观看人数到基准随机数
     *
     * @param zooRoomId 聊天室ID
     */
    public void turnZooRoomUserCountToZero(Long zooRoomId) {
        turnOnZooRoomUserCountToTarget(zooRoomId, 0);
    }

    /**
     * 开始直播时，增长观看人数到目标随机数
     *
     * @param zooRoomId 聊天室ID
     */
    public void turnZooRoomUserCountToTarget(Long zooRoomId, Integer target) {
        turnOnZooRoomUserCountToTarget(zooRoomId, target);
    }

    /**
     * 查询直播实时人数
     *
     * @param zooRoomId 聊天室ID
     */
    public Integer getZooRoomUserCountTimely(Long zooRoomId) {
        if (zooRoomId == null) {
            ZooManager.LOGGER.warn("直播聊天室ID为空");
            throw new LiveException(-12138, "直播聊天室ID为空");
        }
        try {
            return zooCountService.countZooTimely(zooRoomId);
        } catch (Exception e) {

            ZooManager.LOGGER.warn("聊天室查询直播人数服务失败" + e.getMessage());
            throw new LiveException(-12138, "聊天室查询直播人数服务失败");
        }
    }

    /**
     * 根据聊天室ID获取聊天室人数
     *
     * @param zooRoomId 聊天室ID
     * @return 聊天室人数
     */
    public Integer getZooRoomUserCount(Long zooRoomId) {
        if (zooRoomId == null) {
            ZooManager.LOGGER.warn("直播聊天室ID为空");
            throw new LiveException(-12138, "直播聊天室ID为空");
        }
        try {
            return zooCountService.countZoo(zooRoomId);
        } catch (Exception e) {

            ZooManager.LOGGER.warn("聊天室查询人数服务失败" + e.getMessage());
            throw new LiveException(-12138, "聊天室查询人数服务失败");
        }
    }

    /**
     * 根据zid复制聊天信息
     *
     * @param fromZid 从聊天室
     * @param toZid   到聊天室
     * @return 是否成功
     */
    public boolean replicateZooMessage(Long fromZid, Long toZid) {
        if (fromZid == null) {
            ZooManager.LOGGER.warn("从聊天室ID为空");
            throw new LiveException(-12138, "聊天室ID为空");
        }
        if (toZid == null) {
            ZooManager.LOGGER.warn("到聊天室ID为空");
            throw new LiveException(-12138, "聊天室ID为空");
        }
        try {
            return zooService.replicateZooMessage(fromZid, toZid);
        } catch (Exception e) {

            ZooManager.LOGGER.warn("调用聊天室服务失败" + e.getMessage());
            throw new LiveException(-12138, "聊天室服务调用失败");
        }
    }

    /**
     * 根据基本信息创建Zoo实例
     *
     * @param zooRoomId 聊天室ID
     * @param title     聊天室名字
     * @param zrc       聊天室基准随机数
     * @param uid       聊天室主播ID
     * @return 聊天室实例
     */
    private Zoo buildZooInstance(Long zooRoomId, String title, Integer zrc, Long uid) {
        Zoo zoo = new Zoo();
        zoo.setZid(zooRoomId);
        zoo.setName(title);
        if (zrc == null) {
            zrc = 0;
        }
        zoo.setZrc(zrc);
        zoo.setUid(uid);
        return zoo;
    }

    public Map<Long, ZooCount> countZoos(List<Long> zids) {
        Map<Long, ZooCount> zooCountMap = new HashMap<>();
        List<Long> distinctZids = zids.stream().distinct().collect(Collectors.toList());
        List<Integer> zooCounts = zooCountService.countZoos(distinctZids);
        Map<Long, Integer>  zooHotMap = zooCountService.batchCountZoo(distinctZids);
        for (int i = 0; i < zooCounts.size(); i++) {
            Long zooId = distinctZids.get(i);
            int hot = zooHotMap.get(zooId);
            Integer real = zooCounts.get(i);
            ZooCount zooCount =new ZooCount();
            zooCount.setHot(hot);
            zooCount.setReal(real);
            zooCountMap.put(zooId,zooCount);
        }
        return zooCountMap;
    }

    public void sendChatMessage(long chatRoomId, String message, long anchorId) {
        ZooMessage zooMessage = new ZooMessage();
        zooMessage.setContent(message);
        zooMessage.setCreatetm(Utils.getCurrentMillis());
        zooMessage.setUid(anchorId);
        zooMessage.setZid(chatRoomId);
        zooMessage.setType(ZooMessage.COMMENT_TYPE);
        zooMessage.setStatus(ZooMessage.STATUS_SUCCESS);
        //保存消息
        try {
            zooMessage = zooMessageService.saveZooMessage(zooMessage);

        } catch (Exception e) {
            ZooManager.LOGGER.warn("调用聊天室服务失败" + e.getMessage());
            throw new LiveException(-12138, "聊天室服务调用失败");
        }
        User user = userManager.getUser(anchorId);
        //mq广播协议
        // 组装聊天室Proto
        ZooProto.ZooPack zooPack = AssembleProto.assembleChatMessage(zooMessage, user);
        if (Objects.nonNull(zooPack)) {
            // mq发送到聊天室信息
            mqProducerManager.broadCastBytes(ZooTopic.ZOO_PUSH_TOPIC, String.valueOf(chatRoomId), zooPack.toByteArray());
        }
    }


    public Optional<LiveZoo> assembleLiveZoo(long zid) {
        Zoo zoo = aquireZooRoom(zid);
        Optional optional = null;
        if (null != zoo) {
            optional = Optional.empty();
        }
        int           hot      = zooCountService.countZoo(zid);
        List<Integer> reals    = zooCountService.countZoos(Arrays.asList(zid));
        ZooCount      zooCount =new ZooCount();
        zooCount.setReal(Utils.isEmpty(reals)?0:reals.get(0));
        zooCount.setHot(hot);
        return Optional.of(new LiveZoo(zoo, zooCount));
    }

    public List<LiveZoo> list(List<Long> ids) {
        List<Zoo> zooList = zooService.list(ids);
        Map<Long, ZooCount> countZoos = countZoos(ids);
        return zooList.stream().map(zoo -> {
            return new LiveZoo(zoo, countZoos.get(zoo.getZid()));
        }).collect(Collectors.toList());
    }
    public Map<Long,LiveZoo> map(List<Long> ids) {
        List<LiveZoo> liveZoos = list(ids);
        return liveZoos.stream().collect(Collectors.toMap(LiveZoo::getZid,v1->v1,(v1,v2)->v1));
    }

    public Map<Long,Integer> batchZooCount(List<Long> zids){
        if(Utils.isEmpty(zids)){
            return null;
        }
        return zooCountService.batchCountZoo(zids);
    }

    public Map<Long,Integer> mapBatchZooRealCount(List<Long> zids){


        if(CollectionUtils.isEmpty(zids)){
            return new HashMap<>();
        }
        return zooCountService.mapBatchZooRealCount(zids);



    }
}

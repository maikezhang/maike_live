package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.LiveRoomMapper;
import cn.idongjia.live.db.mybatis.po.LiveRoomPO;
import cn.idongjia.live.db.mybatis.query.DBLiveRoomQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.domain.entity.live.LiveRecord;
import cn.idongjia.live.restructure.dto.LiveRoomDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.manager.CloudManager;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.RedisManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.DateTimeUtil.millis2Timestamp;

@Component
public class LiveRoomRepo {

    private static final Log LOGGER = LogFactory.getLog(LiveRoomRepo.class);
    @Resource
    private LiveShowRepo liveShowRepo;
    @Resource
    private CloudManager cloudManager;
    @Resource
    private ConfigManager configManager;
    @Resource
    private LiveRoomMapper liveRoomMapper;
    @Resource
    private RedisManager redisManager;
    @Resource
    private UserManager userManager;

    /**
     * 根据直播间ID获取播放拉流地址
     *
     * @param id 直播间ID
     * @return 播放流地址列表(0.flv_url 1.rtmp_url 2.hls_url)
     */
    public LivePullUrl getPullUrls(Long id) {
        //根据直播主播ID获取直播间信息
        LiveRoomDTO liveRoomDTO = getById(id);
        return getPullUrls(liveRoomDTO);
    }

    LivePullUrl getPullUrls(LiveRoomDTO liveRoomDTO) {
        //根据直播云ID和直播云类型获取拉流播放地址
        try {
            LivePullUrl livePullUrl = redisManager.getUrlsFromRedis(liveRoomDTO.getUserId());
            if (livePullUrl == null) {
                livePullUrl = cloudManager.getPlayUrlByCloudType(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType());
                redisManager.putUrlsToRedis(liveRoomDTO.getUserId(), livePullUrl);
            }
            return livePullUrl;
        } catch (Exception e) {
            LiveRoomRepo.LOGGER.warn("获取拉流地址失败" + e.getMessage());
            throw new LiveException(-12138, "获取拉流地址失败");
        }
    }


    /**
     * 根据主播手机号获取推流地址
     *
     * @param mobile 主播手机号
     * @return 直播间推流地址
     */
    public String getPushUrlByMobile(String mobile) {
        Long uid = userManager.getUserIdByMobile(mobile);
        return getPushUrlByUid(uid);
    }

    /**
     * 根据uid获取默认直播间推流地址
     *
     * @param uid 直播主播uid
     * @return 直播间推流地址
     */
    public String getPushUrlByUid(Long uid) {
        //根据主播ID获取正在预展中或直播中的直播
        LiveShowDTO liveShowDTO = liveShowRepo.getLiveShowByUid(uid);
        //根据主播ID和直播云类型获取直播间信息
        LiveRoomDTO liveRoomDTO = getById(liveShowDTO.getRoomId());

        return cloudManager.getRecordUrlByCloudType(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType(), clearSpecialChar(liveShowDTO.getTitle()));
    }

    private String clearSpecialChar(String title) {
        String toRemoveStr = configManager.getCharsToRemove();
        String videoName = title;
        if (!Strings.isNullOrEmpty(toRemoveStr)) {
            String[] temp = toRemoveStr.split(",");
            for (String t : temp) {
                videoName = videoName.replace(t, "");
            }
        }
        return videoName.trim();
    }

    /**
     * 根据直播间ID获取推流信息
     *
     * @param id 直播间ID
     * @return 推流地址
     */
    String getPushUrlById(Long id, String videoName) {
        LiveRoomDTO liveRoomDTO = getById(id);
        return cloudManager.getRecordUrlByCloudType(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType(), clearSpecialChar(videoName));
    }

    /**
     * 根据主播ID分配直播间
     *
     * @param uid 主播ID
     */
    public Long assignLiveRoom(Long uid, Integer cloudType) {
        LiveRoomRepo.LOGGER.info("分配云直播间，主播ID为: " + uid + "，直播云类型为: " + cloudType);
        //判断是否为自定义云类型
        if (cloudType == null) {
            //若为空则获取系统默认云类型
            cloudType = cloudManager.getCloudType(configManager.getCloudType());
        }
        //根据主播ID和云类型获取直播间
        LiveRoomDTO liveRoomDTO = getLiveRoomByUidAndType(uid, cloudType);
        if (null == liveRoomDTO) {
            liveRoomDTO = createLiveRoom(uid, cloudType);
        }
        return liveRoomDTO.getId();
    }

    /**
     * 更换直播间
     *
     * @param uid 主播ID
     */
    public Long changeRoom(Long uid, Integer cloudType) {
        return assignLiveRoom(uid, cloudType);
    }

    /**
     * 直播间开播，若开播不成则会抛出异常
     *
     * @param id 直播间ID
     */
    public boolean startLiveShow(Long id) {
        //根据直播主播ID获取直播间信息
        LiveRoomDTO liveRoomDTO = getById(id);
        return cloudManager.startLive(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType());
    }

    /**
     * 直播间开启录制功能
     *
     * @param id 直播间ID
     */
    public void startLiveRecord(Long id, String videoName) {
        //根据直播主播ID获取直播间信息
        LiveRoomDTO liveRoomDTO = getById(id);
        cloudManager.startRecord(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType(), clearSpecialChar(videoName));
    }

    /**
     * 直播间停播，若停播不成则会抛出一异常
     *
     * @param id 直播间ID
     */
    public void stopLiveShow(Long id) {
        //根据主播ID获取直播间信息
        LiveRoomDTO liveRoomDTO = getById(id);
        cloudManager.stopLive(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType());
    }

    /**
     * 直播间停止录制
     *
     * @param id 直播间ID
     */
    public void stopLiveRecord(Long id, String videoName) {
        //根据直播主播ID获取直播间
        LiveRoomDTO liveRoomDTO = getById(id);
        cloudManager.stopRecord(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType(), clearSpecialChar(videoName));
    }

    /**
     * 根据直播主播ID获取直播云所有录制的视频
     *
     * @param uid 直播主播ID
     * @return 直播云录制视频信息
     */
    public List<cn.idongjia.live.pojo.live.LiveRecord> listRecordsByUid(Long uid) {
        //根据直播ID获取所有直播间
        List<LiveRoomDTO> liveRoomDTOS = getAllLiveRoomByUid(uid);
        List<cn.idongjia.live.pojo.live.LiveRecord> reList = new ArrayList<>();
        liveRoomDTOS.stream().forEach(liveRoomDTO -> {
            reList.addAll(listRecordsByCloud(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType()));
        });

        return reList;
    }

    /**
     * 根据主播ID获取其所有的直播间
     *
     * @param uid 主播ID
     * @return 直播间列表
     */
    private List<LiveRoomDTO> getAllLiveRoomByUid(Long uid) {
        DBLiveRoomQuery dbLiveRoomQuery = DBLiveRoomQuery.builder()
                .userIds(Arrays.asList(uid))
                .status(LiveConst.STATUS_ROOM_AVAILABLE)
                .build();
        List<LiveRoomPO> liveRoomPOS = liveRoomMapper.list(dbLiveRoomQuery);
        return liveRoomPOS.stream().map(LiveRoomDTO::new).collect(Collectors.toList());
    }

    /**
     * 根据云ID和云类型获取远端所有录制文件信息
     *
     * @param cloudId   直播云ID
     * @param cloudType 直播云类型
     * @return 录制文件列表
     */
    private List<cn.idongjia.live.pojo.live.LiveRecord> listRecordsByCloud(String cloudId, int cloudType) {
        try {
            List<LiveRecord> records = cloudManager.listRecords(cloudId, cloudType);
            return inRecord2OutRecord(records);
        } catch (Exception e) {
            LiveRoomRepo.LOGGER.warn("列出录制文件异常" + e.getMessage());
            throw new LiveException(-12138, "获取直播云录制文件失败");
        }
    }

    private List<cn.idongjia.live.pojo.live.LiveRecord> inRecord2OutRecord(List<LiveRecord> inRecords) {
        List<cn.idongjia.live.pojo.live.LiveRecord> outRecord = new ArrayList<>();
        inRecords.forEach(inRecord -> {
            cn.idongjia.live.pojo.live.LiveRecord out = new cn.idongjia.live.pojo.live.LiveRecord();
            out.setDownloadUrl(inRecord.getDownloadUrl());
            out.setEndtm(inRecord.getEndtm());
            out.setStarttm(inRecord.getStarttm());
            out.setVideoName(inRecord.getVideoName());
            out.setVodUrl(inRecord.getVodUrl());
            outRecord.add(out);

        });
        return outRecord;
    }

    /**
     * 根据直播间ID查询直播间信息
     *
     * @param roomId 直播间ID
     * @return 直播间信息
     */

    public LiveRoomDTO getById(Long roomId) {
        LiveRoomPO liveRoomPO = liveRoomMapper.get(roomId);
        return liveRoomPO == null ? null : new LiveRoomDTO(liveRoomPO);
    }


    /**
     * 根据主播ID和系统当前直播云类型获取直播间
     *
     * @param uid 主播ID
     * @return 如果存在直播间则返回信息，若不存在则返回空null
     */
    private LiveRoomDTO getLiveRoomByUidAndType(Long uid, int cloudType) {
        //组装搜索条件：主播ID、云类型和直播间状态正常
        DBLiveRoomQuery dbLiveRoomQuery = DBLiveRoomQuery.builder()
                .cloudType(cloudType)
                .userIds(Arrays.asList(uid))
                .status(LiveConst.STATUS_ROOM_AVAILABLE)
                .build();
        List<LiveRoomPO> liveRoomDOList = liveRoomMapper.list(dbLiveRoomQuery);
        if (liveRoomDOList != null && liveRoomDOList.size() != 0) {
            return new LiveRoomDTO(liveRoomDOList.get(0));
        } else {
            return null;
        }
    }

    /**
     * 根据主播用户ID和云直播类型创建直播间
     *
     * @param uid 直播用户
     * @return 直播间ID
     */
    private LiveRoomDTO createLiveRoom(Long uid, Integer cloudType) {
        //组装要增加的数据
        LiveRoomPO liveRoomPO = new LiveRoomPO();
        liveRoomPO.setUserId(uid);
        liveRoomPO.setCloudType(cloudType);
        liveRoomPO.setCloudId(acquireCloudId(uid, liveRoomPO.getCloudType()));
        liveRoomPO.setStatus(LiveConst.STATUS_ROOM_AVAILABLE);
        liveRoomPO.setCreateTime(millis2Timestamp(Utils.getCurrentMillis()));
        LOGGER.info("新增云直播间内容为:{} ", liveRoomPO);
        //新增数据
        liveRoomMapper.insert(liveRoomPO);
        return new LiveRoomDTO(liveRoomPO);
    }

    /**
     * 根据uid和系统配置的云类型获取直播云ID
     *
     * @param uid       主播ID
     * @param cloudType 直播云类型
     * @return 直播云ID
     */
    private String acquireCloudId(Long uid, int cloudType) {
        return cloudManager.getCloudID(String.valueOf(uid), cloudType);
    }


    public List<LiveRoomDTO> list(DBLiveRoomQuery dbLiveRoomQuery) {
        List<LiveRoomPO> liveRoomPOS = liveRoomMapper.list(dbLiveRoomQuery);
        return liveRoomPOS.stream().map(LiveRoomDTO::new).collect(Collectors.toList());
    }

}

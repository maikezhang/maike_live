package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.api.live.pojo.LiveApiResp;
import cn.idongjia.live.api.live.pojo.LiveItemResp;
import cn.idongjia.live.api.live.pojo.LiveResp;
import cn.idongjia.live.api.live.pojo.PlayBack;
import cn.idongjia.live.api.live.pojo.PreLiveResp;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveUtil;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.search.pojo.live.LiveIndexResp;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 直播索引查询结果DTO
 *
 * @author lc
 * @create at 2018/6/12.
 */
public class SearchIndexRespDTO extends BaseDTO<LiveIndexResp> {
    public SearchIndexRespDTO(LiveIndexResp entity) {
        super(entity);
    }

    public LiveApiResp assembleLiveApiResp(CustomerVo customerVo, VideoCoverDTO videoCoverDTO,
            List<LiveItemResp> liveItemResps, List<PlayBackDTO> playBackDTOS,Integer liveType) {
        ConfigManager configManager = SpringUtils.getBean("configManager", ConfigManager.class)
                .orElseThrow(() -> LiveException.failure("获取configManager实例失败"));
        int         type        = getType();
        LiveApiResp liveApiResp = assembleLiveRepo(type, null, customerVo, videoCoverDTO, liveItemResps);

        if(liveType !=2){
            liveApiResp.setLiveType(1);
        }

        if (type == 6 || type == 3) {
            List<PlayBack> playBacks      = new ArrayList<>();
            long           durationMillis = 0L;
            for (int i = 0; i < playBackDTOS.size(); i++) {
                PlayBackDTO playBackDTO = playBackDTOS.get(i);
                if (playBackDTO.getDuration() >= TimeUnit.MINUTES.toMillis(configManager.getLeastDuration())) {
                    playBacks.add(assemblePlayBack(playBackDTO));
                    durationMillis += playBackDTO.getDuration();
                }
            }
            // 去掉没有回放的直播
            if (LiveEnum.LiveState.FINISHED.getCode() == entity.getState() && playBacks.size() == 0) {
                return null;
            }
            // 计算回放时长
            long duration2Sec = durationMillis / 1000;
            long duration2Min = duration2Sec / 60;
            long durationSec  = duration2Sec % 60;
            liveApiResp.setDuration((duration2Min < 10 ? "0" + duration2Min : duration2Min) + ":"
                    + (durationSec < 10 ? "0" + durationSec : durationSec + ""));
            liveApiResp.setPlayBacks(playBacks);

        }
        return liveApiResp;
    }


    public static PlayBack assemblePlayBack(PlayBackDTO playBackDTO) {
        if (playBackDTO == null) {
            return null;
        }
        long     duration2Sec = playBackDTO.getDuration() / 1000;
        long     duration2Min = duration2Sec / 60;
        long     durationSec  = duration2Sec % 60;
        PlayBack playBack     = new PlayBack();
        playBack.setDuration((duration2Min < 10 ? "0" + duration2Min : duration2Min) + ":"
                + (durationSec < 10 ? "0" + durationSec : durationSec + ""));
        playBack.setId(playBackDTO.getId());
        playBack.setUrl(playBackDTO.getUrl());
        playBack.setDurationMillis(playBackDTO.getDuration());
        return playBack;
    }

    private LiveApiResp assembleLiveRepo(int type, Integer index, CustomerVo user, VideoCoverDTO videoCoverDTO,
            List<LiveItemResp> liveItemResps) {
        LiveApiResp liveApiResp = new LiveApiResp();
        liveApiResp.setAsid(entity.getAsid());
        liveApiResp.setAvatar(entity.getAvatar());
        liveApiResp.setCreateTime(entity.getCreateTime());
        liveApiResp.setEndTime(entity.getEndTime());
        liveApiResp.setGeneralWeight(entity.getWeight());
        if (!Utils.isEmpty(liveItemResps)) {
            liveApiResp.setItems(liveItemResps);
        }
        liveApiResp.setPreEndTime(entity.getPreEndTime());
        liveApiResp.setId(entity.getId());
        if (!Objects.isNull(user)) {
            liveApiResp.setName(user.getName());
            liveApiResp.setUtype(user.getCraftsman() == null ? 0 : 1);
        }
        liveApiResp.setAsid(entity.getAsid());
        liveApiResp.setPreStartTime(entity.getPreStartTime());
        liveApiResp.setStartTime(entity.getStartTime());
        liveApiResp.setState(entity.getState());
        liveApiResp.setStatus(entity.getStatus());
        liveApiResp.setUid(entity.getUid());
        liveApiResp.setPic(entity.getPic());
        if (index != null) {
            liveApiResp.setTitle(entity.getTitle() + "(" + LiveUtil.convert2ChineseNum(index + 1) + ")");
        } else {
            liveApiResp.setTitle(entity.getTitle());
        }
        liveApiResp.setLiveType(entity.getType());

        liveApiResp.setType(type);
        if (videoCoverDTO != null) {
            liveApiResp.setVideoCover(videoCoverDTO.getUrl());

        }
        liveApiResp.setScreenDirection(entity.getScreenDirection());
        liveApiResp.setGeneralWeight(entity.getWeight());
        return liveApiResp;
    }

    public int getLiveType() {
        return entity.getType();
    }

    public int getType() {
        LiveEnum.LiveType liveType = BaseEnum.parseInt2Enum(entity.getType(), LiveEnum.LiveType.values()).orElse(null);
        LiveEnum.LiveState liveState = BaseEnum.parseInt2Enum(entity.getState(), LiveEnum.LiveState.values())
                .orElse(null);
        // 1：纯直播未开始2：纯直播已开始3：纯直播已结束 4直播拍未开始5直播拍拍卖中6直播拍已结束
        int type = 0;
        switch (liveType) {
            case PURE_LIVE:
            case ELSE_TYPE:
            case TREASURE_TYPE:
            case CRAFTS_TALK_TYPE:
            case CRAFTS_PURCHASE_TYPE:
                switch (liveState) {
                    case UNSTART:
                        type = 1;
                        break;
                    case PLAYING:
                        type = 2;
                        break;
                    case FINISHED:
                        type = 3;
                        break;
                    default:
                        break;
                }
                break;
            case LIVE_AUCTION:
                switch (liveState) {
                    case UNSTART:
                        type = 4;
                        break;
                    case PLAYING:
                        type = 5;
                        break;
                    case FINISHED:
                        type = 6;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return type;
    }

    public PreLiveResp assembePreLiveItem(CustomerVo customerVo) {
        PreLiveResp preLiveResp = new PreLiveResp();
        preLiveResp.setAsid(entity.getAsid());
        preLiveResp.setAvatar(entity.getAvatar());
        preLiveResp.setId(entity.getId());
        preLiveResp.setName(customerVo.getName());
        preLiveResp.setTitle(entity.getTitle());
        preLiveResp.setPreStartTime(entity.getPreStartTime());
        return preLiveResp;
    }

    public LiveResp assembleLiveResp(VideoCoverDTO videoCoverDTO, CustomerVo customerVo, int count) {
        LiveResp liveResp = new LiveResp();
        liveResp.setAsid(entity.getAsid());
        liveResp.setId(entity.getId());
        liveResp.setAvatar(entity.getAvatar());
        liveResp.setCreateTime(entity.getCreateTime());
        liveResp.setGeneralWeight(entity.getWeight());
        liveResp.setEndTime(entity.getEndTime());
        liveResp.setName(customerVo.getName());
        liveResp.setTitle(entity.getTitle());
        liveResp.setPreEndTime(entity.getPreEndTime());
        liveResp.setPreStartTime(entity.getPreStartTime());
        liveResp.setPic(entity.getPic());
        if (videoCoverDTO != null) {
            liveResp.setVideoCoverId(videoCoverDTO.getId());
            liveResp.setVideoCoverDuration(videoCoverDTO.getDuration());
            liveResp.setVideoCoverUrl(videoCoverDTO.getUrl());
            liveResp.setVideoCoverPic(videoCoverDTO.getPic());
        }
        liveResp.setType(entity.getType());
        liveResp.setState(entity.getState());
        liveResp.setOnline(entity.getOnline());
        liveResp.setStatus(entity.getStatus());
        liveResp.setUid(entity.getUid());
        liveResp.setRecommendWeight(entity.getRecommendWeight());
        liveResp.setStartTime(entity.getStartTime());
        liveResp.setEndTime(entity.getEndTime());
        liveResp.setUv(count);
        liveResp.setZrc(entity.getZrc());
        liveResp.setZid(entity.getZid());
        liveResp.setResourceCount(count);
        return liveResp;
    }

    public cn.idongjia.live.api.live.pojo.LiveIndexResp assembleLiveIndexResp() {
        cn.idongjia.live.api.live.pojo.LiveIndexResp liveIndexResp = new cn.idongjia.live.api.live.pojo.LiveIndexResp();
        liveIndexResp.setAsid(entity.getAsid());
        liveIndexResp.setAvatar(entity.getAvatar());
        liveIndexResp.setCreateTime(entity.getCreateTime());
        liveIndexResp.setEndTime(entity.getEndTime());
        liveIndexResp.setGeneralWeight(entity.getWeight());
        liveIndexResp.setHasPlayback(entity.getHasPlayback());
        liveIndexResp.setId(entity.getId());
        liveIndexResp.setName(entity.getName());
        liveIndexResp.setOnline(entity.getOnline());
        liveIndexResp.setPic(entity.getPic());
        liveIndexResp.setPreEndTime(entity.getPreEndTime());
        liveIndexResp.setPreStartTime(entity.getPreStartTime());
        liveIndexResp.setScreenDirection(entity.getScreenDirection());
        liveIndexResp.setStartTime(entity.getStartTime());
        liveIndexResp.setState(entity.getState());
        liveIndexResp.setStatus(entity.getStatus());
        liveIndexResp.setTitle(entity.getTitle());
        liveIndexResp.setType(entity.getType());
        liveIndexResp.setUid(entity.getUid());
        liveIndexResp.setUtype(entity.getUtype());
        liveIndexResp.setUpdateTime(entity.getUpdateTime());
        liveIndexResp.setUv(entity.getUv());
        liveIndexResp.setVideoCoverId(entity.getVideoCoverId());
        liveIndexResp.setVideoUrl(entity.getVideo());
        liveIndexResp.setZid(entity.getZid());
        liveIndexResp.setZrc(entity.getZrc());
        liveIndexResp.setRecommendWeight(entity.getRecommendWeight());
        return liveIndexResp;
    }

    public Long getId() {
        return entity.getId();
    }

    public Long getAsid() {
        return entity.getAsid();
    }

    public Long getUserId() {
        return entity.getUid();
    }

    public String getPic() {
        return entity.getPic();
    }

    public int getLiveStatus() {
        return entity.getStatus();
    }

    public int getLiveState() {
        return entity.getState();
    }

    public String getTitle() {
        return entity.getTitle();
    }

    public Integer getOnline() {
        return entity.getOnline();
    }

    public Long getPreStartTime() {
        return entity.getPreStartTime();
    }

    public Integer getUv() {
        return entity.getUv();
    }

    public Long getZooId() {
        return entity.getZid();
    }
}

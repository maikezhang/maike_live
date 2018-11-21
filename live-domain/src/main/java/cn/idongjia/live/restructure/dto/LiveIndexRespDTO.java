package cn.idongjia.live.restructure.dto;

import cn.idongjia.article.pojos.Live;
import cn.idongjia.divine.lib.pojo.Conf;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.api.live.pojo.LiveApiResp;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.api.live.pojo.LiveItemResp;
import cn.idongjia.live.api.live.pojo.LiveResp;
import cn.idongjia.live.api.live.pojo.PlayBack;
import cn.idongjia.live.api.live.pojo.PreLiveResp;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveUtil;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 直播索引查询结果DTO
 *
 * @author lc
 * @create at 2018/6/12.
 */
public class LiveIndexRespDTO extends BaseDTO<LiveIndexResp> {
    public LiveIndexRespDTO(LiveIndexResp entity) {
        super(entity);
    }


    public List<LiveApiResp> assembleLiveItem(CustomerVo customerVo, VideoCoverDTO videoCoverDTO, List<LiveItemResp> liveItemResps, List<PlayBackDTO> playBackDTOS) {
        ConfigManager     configManager = SpringUtils.getBean("configManager", ConfigManager.class).orElseThrow(() -> LiveException.failure("获取configManager实例失败"));
        int               type          = getType();
        List<LiveApiResp> liveApiResps  = new ArrayList<>();
        if (type == 6 || type == 3) {
            if (!Utils.isEmpty(playBackDTOS)) {
                if (playBackDTOS.size() == 1) {
                    PlayBackDTO playBackDTO = playBackDTOS.get(0);
                    if (playBackDTO.getDuration() > 1000 * 5 * 60) {
                        PlayBack    playBack       = playBackDTOS.get(0).assemblePlayBack();
                        LiveApiResp tmpLiveApiResp = assembleLiveRepo(type, null, customerVo, videoCoverDTO, null);
                        tmpLiveApiResp.setDuration(playBack.getDuration());
                        tmpLiveApiResp.setPlayBacks(Collections.singletonList(playBack));
                        liveApiResps.add(tmpLiveApiResp);
                    }

                } else {
                    for (int i = 0; i < playBackDTOS.size(); i++) {
                        PlayBackDTO playBackDTO = playBackDTOS.get(i);
                        if (playBackDTO.getDuration() > 1000 * 5 * 60) {
                            PlayBack    playBack       = playBackDTOS.get(i).assemblePlayBack();
                            LiveApiResp tmpLiveApiResp = assembleLiveRepo(type, i, customerVo, videoCoverDTO, null);
                            tmpLiveApiResp.setDuration(playBack.getDuration());
                            tmpLiveApiResp.setPlayBacks(Arrays.asList(playBack));
                            liveApiResps.add(tmpLiveApiResp);
                        }
                    }
                }

            } else {
                liveApiResps.add(assembleLiveRepo(type, null, customerVo, videoCoverDTO, liveItemResps));
            }
        } else {
            liveApiResps.add(assembleLiveRepo(type, null, customerVo, videoCoverDTO, liveItemResps));
        }
        return liveApiResps;
    }

    private LiveApiResp assembleLiveRepo(int type, Integer index, CustomerVo user, VideoCoverDTO videoCoverDTO, List<LiveItemResp> liveItemResps) {
        LiveApiResp liveApiResp = new LiveApiResp();
        liveApiResp.setAsid(entity.getAsid());
        liveApiResp.setAvatar(entity.getAvatar());
        liveApiResp.setCreateTime(entity.getCreateTime());
        liveApiResp.setEndTime(entity.getEndTime());
        liveApiResp.setGeneralWeight(entity.getGeneralWeight());
        if (!Utils.isEmpty(liveItemResps)) {
            liveApiResp.setItems(liveItemResps);
        }
        liveApiResp.setPreEndTime(entity.getPreEndTime());
        liveApiResp.setId(entity.getId());
        liveApiResp.setName(user.getName());
        liveApiResp.setAsid(entity.getAsid());
        liveApiResp.setPreStartTime(entity.getPreStartTime());
        liveApiResp.setStartTime(entity.getStartTime());
        liveApiResp.setState(entity.getState());
        liveApiResp.setStatus(entity.getStatus());
        liveApiResp.setUid(entity.getUid());
        liveApiResp.setUtype(user.getCraftsman() == null ? 0 : 1);
        liveApiResp.setPic(entity.getPic());
        if (index != null) {
            liveApiResp.setTitle(entity.getTitle() + "(" + LiveUtil.convert2ChineseNum(index + 1) + ")");
        } else {
            liveApiResp.setTitle(entity.getTitle());
        }
        liveApiResp.setLiveType(entity.getType());

        liveApiResp.setType(type);
        liveApiResp.setVideoCover(videoCoverDTO.getPic());
        liveApiResp.setScreenDirection(entity.getScreenDirection());
        return liveApiResp;
    }

    public int getType() {
        LiveEnum.LiveType  liveType  = BaseEnum.parseInt2Enum(entity.getType(), LiveEnum.LiveType.values()).orElse(null);
        LiveEnum.LiveState liveState = BaseEnum.parseInt2Enum(entity.getState(), LiveEnum.LiveState.values()).orElse(null);
        // 1：纯直播未开始2：纯直播已开始3：纯直播已结束 4直播拍未开始5直播拍拍卖中6直播拍已结束
        int type = 0;
        switch (liveType) {
            case PURE_LIVE:
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
        liveResp.setGeneralWeight(entity.getGeneralWeight());
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
        liveResp.setUv(entity.getUv());
        liveResp.setZrc(entity.getZrc());
        liveResp.setZid(entity.getZid());
        liveResp.setResourceCount(count);
        return liveResp;
    }


    public static LiveResp esDataToLiveResp(GeneralLiveCO generalLiveCO, Boolean isTop, Integer realZooCount) {
        LiveResp liveResp = new LiveResp();
        if (generalLiveCO.getSessionId() != null && !Objects.equals(Conf.defaultDate.longValue(), generalLiveCO.getSessionId().longValue())) {
            liveResp.setAsid(generalLiveCO.getSessionId());
        }
        liveResp.setId(generalLiveCO.getId());
        liveResp.setAvatar(generalLiveCO.getAvatar());
        liveResp.setCreateTime(generalLiveCO.getCreateTime());
        liveResp.setGeneralWeight(generalLiveCO.getGeneralWeight());
        if (generalLiveCO.getEndTime() != null && !Objects.equals(Conf.defaultDate.longValue(), generalLiveCO.getEndTime().longValue())) {
            liveResp.setEndTime(generalLiveCO.getEndTime());
        }
        liveResp.setName(generalLiveCO.getName());
        liveResp.setTitle(generalLiveCO.getTitle());
        liveResp.setPreEndTime(generalLiveCO.getPreEndTime());
        liveResp.setPreStartTime(generalLiveCO.getPreStartTime());
        liveResp.setPic(generalLiveCO.getPic());
        liveResp.setType(generalLiveCO.getType());
        liveResp.setState(generalLiveCO.getState());
        liveResp.setOnline(generalLiveCO.getOnline());
        liveResp.setStatus(generalLiveCO.getStatus());
        liveResp.setUid(generalLiveCO.getUid());
        if (generalLiveCO.getStartTime() != null && !Objects.equals(Conf.defaultDate.longValue(), generalLiveCO.getStartTime().longValue())) {
            liveResp.setStartTime(generalLiveCO.getStartTime());
        }
        liveResp.setUv(realZooCount == null ? 0 : realZooCount);
        liveResp.setZrc(generalLiveCO.getZrc());
        liveResp.setZid(generalLiveCO.getZid());
        liveResp.setResourceCount(generalLiveCO.getResourceCount());
        if (generalLiveCO.getOnline() != null && Objects.equals(LiveEnum.LiveOnline.ONLINE.getCode(), generalLiveCO.getOnline().intValue()) && !Objects.equals(LiveEnum.LiveState.FINISHED.getCode(), generalLiveCO.getState().intValue())) {
            liveResp.setIsTop(isTop == null ? false : isTop);
        }
        return liveResp;
    }


    public cn.idongjia.live.api.live.pojo.LiveIndexResp assembleLiveIndexResp() {
        cn.idongjia.live.api.live.pojo.LiveIndexResp liveIndexResp = new cn.idongjia.live.api.live.pojo.LiveIndexResp();
        liveIndexResp.setAsid(entity.getAsid());
        liveIndexResp.setAvatar(entity.getAvatar());
        liveIndexResp.setCreateTime(entity.getCreateTime());
        liveIndexResp.setEndTime(entity.getEndTime());
        liveIndexResp.setGeneralWeight(entity.getGeneralWeight());
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
        liveIndexResp.setVideoUrl(entity.getVideoUrl());
        liveIndexResp.setZid(entity.getZid());
        liveIndexResp.setZrc(entity.getZrc());
        return liveIndexResp;
    }


}

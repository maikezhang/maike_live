package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.domain.entity.zoo.ZooCount;
import cn.idongjia.live.restructure.dto.ItemPExtDTO;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.LiveRoomDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.LiveTagDTO;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.live.restructure.dto.PureLiveDTO;
import cn.idongjia.live.restructure.dto.TimeStrategyDTO;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.pojo.co.live.LiveDetailForApiCO;
import cn.idongjia.live.support.GsonUtil;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.LiveUtil;
import cn.idongjia.live.support.enumeration.LivePlayType;
import cn.idongjia.live.support.enumeration.LiveResourceType;
import cn.idongjia.live.v2.pojo.LiveResourceDetail;
import cn.idongjia.user.lib.entity.Craftsman;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.LiveUtil.WEEK_DAYS;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component("pureLiveConvertor")
public class PureLiveConvertor implements ConvertorI<PureLive, LiveEntity, PureLiveDTO> {
    @Override
    public PureLive dataToClient(PureLiveDTO pureLiveDTO) {
        CustomerVo            customerVo        = pureLiveDTO.getCustomerVo();
        Long                  defaultTemplateId = pureLiveDTO.getDefaultTemplateId();
        String                detail            = pureLiveDTO.getDetail();
        String                h5Prefix          = pureLiveDTO.getH5Prefix();
        LivePullUrlDTO        livePullUrl       = pureLiveDTO.getLivePullUrlDTO();
        LivePureDTO           livePureDTO       = pureLiveDTO.getLivePureDTO();
        List<LiveResourceDTO> liveResourceDTOS  = pureLiveDTO.getLiveResourceDTOS();
        LiveRoomDTO           liveRoomDTO       = pureLiveDTO.getLiveRoomDTO();
        LiveShowDTO           liveShowDTO       = pureLiveDTO.getLiveShowDTO();
        VideoCoverDTO         videoCoverDTO     = pureLiveDTO.getVideoCoverDTO();
        String                shareDesc         = pureLiveDTO.getShareDesc();
        LiveZoo               liveZoo           = pureLiveDTO.getLiveZoo();
        String                h5Suffix          = pureLiveDTO.getH5Suffix();
        TimeStrategyDTO       timeStrategyDTO   = pureLiveDTO.getTimeStrategyDTO();
        LiveTagDTO            pureLiveTagRelDTO = pureLiveDTO.getPureLiveTagRelDTO();
        ItemPExtDTO           mainItem          = pureLiveDTO.getMainItem();
        boolean               isBook            = pureLiveDTO.isBook();
        List<PlayBackDTO>     playBackDTOS      = pureLiveDTO.getPlayBackDTOS();
        Long                  minDuration       = pureLiveDTO.getMinDuration();
        if (livePureDTO == null) {
            return null;
        }
        PureLive pureLive = new PureLive();
        pureLive.setId(livePureDTO.getId());
        pureLive.setPlid(livePureDTO.getId());
        pureLive.setDurationLong(livePureDTO.getDuration());
        pureLive.setExemption(livePureDTO.getExemption());
        pureLive.setDesc(livePureDTO.getDesc());
        pureLive.setStatus(livePureDTO.getStatus());
        pureLive.setTitle(liveShowDTO.getTitle());
        pureLive.setPic(livePureDTO.getPic());
        pureLive.setSharepic(livePureDTO.getPic());

        pureLive.setCreatetm(liveShowDTO.getCreateTime());
        pureLive.setPreviewtm(liveShowDTO.getPreviewTime());
        pureLive.setPrestarttm(liveShowDTO.getEstimatedStartTime());
        pureLive.setPreendtm(liveShowDTO.getEstimatedEndTime());
        pureLive.setStarttime(liveShowDTO.getStartTime());
        pureLive.setEndtime(liveShowDTO.getEndTime());
        pureLive.setStarttm(liveShowDTO.getStartTime());
        pureLive.setWeight(liveShowDTO.getGeneralWeight());
        pureLive.setState(liveShowDTO.getState());
        pureLive.setCloudType(liveRoomDTO.getCloudType());

        if (videoCoverDTO != null) {
            pureLive.setUrl(videoCoverDTO.getUrl());
        }
        pureLive.setScreenDirection(liveShowDTO.getScreenDirection());
        pureLive.setShowDesc(liveShowDTO.getShowDesc());
        pureLive.setAutoOnline(liveShowDTO.getAutoOnline());


        pureLive.setZid(liveZoo.getZid());
        pureLive.setZrc(liveZoo.getZrc());
        pureLive.setMuid(liveZoo.getUid());
        ZooCount zooCount  = liveZoo.getZooCount();
        int      userCount = pureLiveDTO.isReal() ? zooCount.getReal() : zooCount.getHot();
        pureLive.setUserCount(userCount);
        pureLive.setRtmpUrl(livePullUrl.getRtmpUrl());
        pureLive.setHlsUrl(livePullUrl.getHlsUrl());
        pureLive.setFlvUrl(livePullUrl.getFlvUrl());
        //组装用户数据
        if (!Objects.isNull(customerVo)) {
            pureLive.setHuid(customerVo.getId());
            pureLive.setHavatar(customerVo.getAvatar());
            pureLive.setHusername(customerVo.getName());
            Craftsman craftsman = customerVo.getCraftsman();
            pureLive.setHtitle(craftsman == null ? null : craftsman.getTitle());
        }
        pureLive.setSharedesc(String.format(shareDesc, customerVo.getName()));

        //组装分享数据
        pureLive.setSharetitle(liveShowDTO.getTitle());
        pureLive.setDetail(detail);
        List<LiveResourceDTO> sortedDetails = null;
        //组装详情数据
        if (!Utils.isEmpty(liveResourceDTOS)) {
            sortedDetails = liveResourceDTOS.stream().sorted(Comparator.comparingInt(LiveResourceDTO::getResourceType))
                    .collect(Collectors.toList());
            Long resourceCount = liveResourceDTOS.stream().filter(
                    x -> LiveResourceType.ITEM.getCode() == x.getResourceType()).count();
            pureLive.setResourceCount(resourceCount.intValue());
        } else {
            sortedDetails = new ArrayList<>();
            pureLive.setResourceCount(0);

        }

        if (Utils.isEmpty(sortedDetails)) {
            LiveResourceDTO liveResourceDTO = new LiveResourceDTO(null);
            liveResourceDTO.setResourceId(defaultTemplateId);
            liveResourceDTO.setResourceType(LiveResourceType.TEMPLATE.getCode());
            sortedDetails.add(liveResourceDTO);
        }
        pureLive.setDetails(sortedDetails.stream().map(LiveResourceDTO::assemblePureLiveDetailDO).collect(Collectors.toList()));
        //组装直播图文详情的url
        sortedDetails.forEach(sortedDetail -> {
            if (Objects.equals(sortedDetail.getResourceType(), LiveResourceType.TEMPLATE.getCode())) {
                pureLive.setPicConUrl(h5Prefix + sortedDetail.getResourceId() + h5Suffix);
                return;
            }

        });

        //商品数量

        if (timeStrategyDTO != null) {
            pureLive.setLiveType(timeStrategyDTO.getType());
            pureLive.setPeriodStartTm(timeStrategyDTO.getPeriodStartTime());
            pureLive.setPeriodEndTm(timeStrategyDTO.getPeriodEndTime());
        } else {
            pureLive.setLiveType(LivePlayType.ONCE.getCode());
        }

        pureLive.setLiveType(liveShowDTO.getType());

        // 小视频
        if (videoCoverDTO != null) {
            pureLive.setVideoCoverDuration(videoCoverDTO.getDuration());
            pureLive.setVideoCoverPic(videoCoverDTO.getPic());
            pureLive.setVideoCoverUrl(videoCoverDTO.getUrl());
            pureLive.setVideoCoverId(videoCoverDTO.getId());
        }


        //如果预计直播则组装开始时间字符串
        if (pureLive.getState().equals(LiveConst.STATE_LIVE_NOT_BEGIN)) {
            Date             date     = new Date(pureLive.getPrestarttm());
            SimpleDateFormat sdf      = new SimpleDateFormat("HH:mm");
            Calendar         calendar = Calendar.getInstance();
            calendar.setTime(date);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (weekDay < 0) {
                weekDay = 0;
            }
            String weekDayStr = WEEK_DAYS[weekDay];
            pureLive.setStarttmStr(weekDayStr + " " + sdf.format(date));
        }

        if (pureLiveTagRelDTO != null) {
            pureLive.setTagId(pureLiveTagRelDTO.getId());
            pureLive.setTagName(pureLiveTagRelDTO.getName());

        }

        //组装订阅数据
        pureLive.setIsBooked(isBook ? LiveConst.STATUS_ANCHOR_BOOKED : LiveConst.STATUS_ANCHOR_UNBOOKED);

        //组装主推信息

        if (null != mainItem) {
            LiveResourceDetail mainResource = new LiveResourceDetail();
            String             pic          = GsonUtil.getFirstStringInJsonArray(mainItem.getPictures());
            mainResource.setPic(pic);
            mainResource.setResourceId(mainItem.getIid());
            mainResource.setResourceType(LiveResourceType.ITEM.getCode());
            mainResource.setPrice(LiveUtil.itemPrice2Int(mainItem.getPrice()));
            mainResource.setTitle(mainItem.getTitle());
            pureLive.setMain(mainResource);
        }


        //组装直播回放数据  如果直播的状态为已结束则为有回放则组装数据
        if (liveShowDTO.getState().intValue() == LiveConst.STATE_LIVE_OVER) {
            List<cn.idongjia.live.api.live.pojo.PlayBack> playBacks      = new ArrayList<>();
            long                                          durationMillis = 0L;
            if (!Utils.isEmpty(playBackDTOS)) {
                for (PlayBackDTO playBackDTO : playBackDTOS) {
                    if (playBackDTO.getDuration() >=
                            TimeUnit.MINUTES.toMillis(minDuration)) {
                        playBacks.add(playBackDTO.assemblePlayBack());
                        durationMillis += playBackDTO.getDuration();
                    }
                }
            }

            pureLive.setPlayBacks(playBacks);
        }
        return pureLive;
    }

    public static LiveDetailForApiCO assembleLiveDetail(PureLiveDTO pureLiveDTO) {
        LiveDetailForApiCO    liveDetailForApiCO = new LiveDetailForApiCO();
        CustomerVo            customerVo         = pureLiveDTO.getCustomerVo();
        String                h5Prefix           = pureLiveDTO.getH5Prefix();
        String                h5Suffix           = pureLiveDTO.getH5Suffix();
        LivePullUrlDTO        livePullUrlDTO     = pureLiveDTO.getLivePullUrlDTO();
        LivePureDTO           livePureDTO        = pureLiveDTO.getLivePureDTO();
        List<LiveResourceDTO> liveResourceDTOS   = pureLiveDTO.getLiveResourceDTOS();
        LiveShowDTO           liveShowDTO        = pureLiveDTO.getLiveShowDTO();
        ItemPExtDTO           mainItem           = pureLiveDTO.getMainItem();
        boolean               book               = pureLiveDTO.isBook();
        String                shareDesc          = pureLiveDTO.getShareDesc();
        Integer               liveHot            = pureLiveDTO.getLiveHot();
        List<PlayBackDTO>     playBackDTOS       = pureLiveDTO.getPlayBackDTOS();
        Long                  minDuration        = pureLiveDTO.getMinDuration();

        liveDetailForApiCO.setId(liveShowDTO.getId());
        if (Objects.nonNull(livePullUrlDTO)) {
            liveDetailForApiCO.setFlvUrl(livePullUrlDTO.getFlvUrl());
            liveDetailForApiCO.setHlsUrl(livePullUrlDTO.getHlsUrl());
            liveDetailForApiCO.setRtmpUrl(livePullUrlDTO.getRtmpUrl());
        }
        if (Objects.nonNull(customerVo)) {
            liveDetailForApiCO.setHavatar(customerVo.getAvatar());
            Craftsman craftsman = customerVo.getCraftsman();
            liveDetailForApiCO.setHtitle(craftsman == null ? null : craftsman.getTitle());
            liveDetailForApiCO.setHuid(customerVo.getId());
            liveDetailForApiCO.setHusername(customerVo.getName());
            liveDetailForApiCO.setSharedesc(String.format(shareDesc, customerVo.getName()));
        }
        liveDetailForApiCO.setIsBooked(book ? LiveConst.STATUS_ANCHOR_BOOKED : LiveConst.STATUS_ANCHOR_UNBOOKED);
        if (Objects.nonNull(mainItem)) {
            LiveResourceDetail liveResourceDetail = new LiveResourceDetail();
            String             pic                = GsonUtil.getFirstStringInJsonArray(mainItem.getPictures());
            liveResourceDetail.setPic(pic);
            liveResourceDetail.setResourceType(LiveResourceType.ITEM.getCode());
            liveResourceDetail.setPrice(LiveUtil.itemPrice2Int(mainItem.getPrice()));
            liveResourceDetail.setTitle(mainItem.getTitle());
            liveResourceDetail.setResourceId(mainItem.getIid());
            liveDetailForApiCO.setMain(liveResourceDetail);
        }
        liveDetailForApiCO.setPic(livePureDTO.getPic());

        if (!CollectionUtils.isEmpty(liveResourceDTOS)) {

            liveDetailForApiCO.setDetails(liveResourceDTOS.stream().map(LiveResourceDTO::assemblePureLiveDetailDO).collect(Collectors.toList()));

            List<Long> templateId = liveResourceDTOS.stream().filter(x -> Objects.equals(LiveResourceType.TEMPLATE.getCode(), x.getResourceType().intValue()))
                    .map(LiveResourceDTO::getResourceId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(templateId)) {
                liveDetailForApiCO.setPicConUrl(h5Prefix + templateId.get(0) + h5Suffix);
                liveDetailForApiCO.setResourceCount(liveResourceDTOS.size() - templateId.size());
            } else {
                liveDetailForApiCO.setResourceCount(liveResourceDTOS.size());
            }

        } else {
            liveDetailForApiCO.setResourceCount(0);
        }
        liveDetailForApiCO.setScreenDirection(liveShowDTO.getScreenDirection());


        liveDetailForApiCO.setSharetitle(liveShowDTO.getTitle());
        if(Objects.equals(LiveConst.STATE_LIVE_NOT_BEGIN,liveShowDTO.getState().intValue())) {
            liveDetailForApiCO.setStarttm(liveShowDTO.getEstimatedStartTime());
        }else {
            liveDetailForApiCO.setStarttm(liveShowDTO.getStartTime());
        }

        //如果预计直播则组装开始时间字符串
        if (liveShowDTO.getState().equals(LiveConst.STATE_LIVE_NOT_BEGIN)) {
            Date             date     = new Date(liveShowDTO.getEstimatedStartTime());
            SimpleDateFormat sdf      = new SimpleDateFormat("HH:mm");
            Calendar         calendar = Calendar.getInstance();
            calendar.setTime(date);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (weekDay < 0) {
                weekDay = 0;
            }
            String weekDayStr = WEEK_DAYS[weekDay];
            liveDetailForApiCO.setStarttmStr(weekDayStr + " " + sdf.format(date));
        }
        liveDetailForApiCO.setState(liveShowDTO.getState());
        liveDetailForApiCO.setType(liveShowDTO.getType());
        liveDetailForApiCO.setTitle(liveShowDTO.getTitle());
        liveDetailForApiCO.setZid(liveShowDTO.getZooId());
        liveDetailForApiCO.setUserCount(liveHot);
        liveDetailForApiCO.setPrestarttm(liveShowDTO.getEstimatedStartTime());
        // 增加直播简介
        liveDetailForApiCO.setShowDesc(liveShowDTO.getShowDesc());

        //组装直播回放数据  如果直播的状态为已结束则为有回放则组装数据
        if (Objects.equals(LiveConst.STATE_LIVE_OVER,liveShowDTO.getState().intValue())) {
            List<cn.idongjia.live.api.live.pojo.PlayBack> playBacks      = new ArrayList<>();
            if (!Utils.isEmpty(playBackDTOS)) {
                for (PlayBackDTO playBackDTO : playBackDTOS) {
                    if (playBackDTO.getDuration() >=
                            TimeUnit.MINUTES.toMillis(minDuration)) {
                        playBacks.add(playBackDTO.assemblePlayBack());
                    }
                }
            }

            liveDetailForApiCO.setPlayBacks(playBacks);
        }



        return liveDetailForApiCO;

    }
}

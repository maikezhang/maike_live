package cn.idongjia.live.restructure.convert;

import cn.idongjia.divine.lib.pojo.Conf;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.divine.lib.pojo.response.live.general.ItemCO;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.LiveMPListDTO;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPDetailCO;
import cn.idongjia.live.support.LiveConst;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static cn.idongjia.live.support.LiveUtil.WEEK_DAYS;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/4
 * Time: 下午1:56
 */
@Component("liveMPConvertor")
public class LiveMPConvertor implements ConvertorI<LiveMPCO, LiveMPCO, GeneralLiveCO> {


    @Override
    public LiveMPCO dataToClient(GeneralLiveCO generalLiveCO) {
        LiveMPCO liveMPCO = new LiveMPCO();

        liveMPCO.setCraftsmanAvatar(generalLiveCO.getAvatar());
        liveMPCO.setCraftsmanTitle(generalLiveCO.getCraftsmanTitle());
        liveMPCO.setCraftsmanUserId(generalLiveCO.getUid());
        liveMPCO.setCreateTime(generalLiveCO.getCreateTime());
        if (!Objects.equals(Conf.defaultDate.longValue(), generalLiveCO.getEndTime().longValue())) {
            liveMPCO.setEndTime(generalLiveCO.getEndTime());
        }
        liveMPCO.setId(generalLiveCO.getId());
        liveMPCO.setType(generalLiveCO.getType());
        liveMPCO.setCover(generalLiveCO.getPic());
        liveMPCO.setPreEndTime(generalLiveCO.getPreEndTime());
        liveMPCO.setPreStartTime(generalLiveCO.getPreStartTime());
        liveMPCO.setStartTime(generalLiveCO.getStartTime());
        liveMPCO.setState(generalLiveCO.getState());
        liveMPCO.setTitle(generalLiveCO.getTitle());
        liveMPCO.setWeight(generalLiveCO.getGeneralWeight());
        liveMPCO.setCraftsmanName(generalLiveCO.getCraftsmanName());
        liveMPCO.setStatus(generalLiveCO.getStatus());
        liveMPCO.setCraftsmanCity(generalLiveCO.getCraftsmanCity());
        liveMPCO.setHot(generalLiveCO.getUv());
        liveMPCO.setOnline(generalLiveCO.getOnline());
        liveMPCO.setZid(generalLiveCO.getZid());
        liveMPCO.setScreenDirection(generalLiveCO.getScreenDirection());

        return liveMPCO;
    }

    public static LiveMPDetailCO dataToLiveMPDetail(LiveMPListDTO liveMPListDTO) {

        LiveMPDetailCO liveMPDetailCO = new LiveMPDetailCO();
        GeneralLiveCO generalLiveCO = liveMPListDTO.getGeneralLiveCO();
        LivePullUrlDTO livePullUrlDTO = liveMPListDTO.getLivePullUrlDTO();
        String shareDesc = liveMPListDTO.getShareDesc();

        Boolean isLike = liveMPListDTO.getIsLike();
        LiveZoo liveZoo = liveMPListDTO.getLiveZoo();

        String templateJson = liveMPListDTO.getTemplateJson();

        liveMPDetailCO.setIsLike(isLike);

        liveMPDetailCO.setId(generalLiveCO.getId());

        liveMPDetailCO.setCreateTime(generalLiveCO.getCreateTime());

        liveMPDetailCO.setEndTime(generalLiveCO.getEndTime());

        liveMPDetailCO.setHavatar(generalLiveCO.getAvatar());
        liveMPDetailCO.setHtitle(generalLiveCO.getCraftsmanTitle());

        liveMPDetailCO.setHuid(generalLiveCO.getUid());

        liveMPDetailCO.setHusername(generalLiveCO.getCraftsmanName());

        liveMPDetailCO.setLiveType(generalLiveCO.getType());

        liveMPDetailCO.setOnline(generalLiveCO.getOnline());

        liveMPDetailCO.setPic(generalLiveCO.getPic());

        liveMPDetailCO.setPreEndTime(generalLiveCO.getPreEndTime());

        liveMPDetailCO.setPreStartTime(generalLiveCO.getPreStartTime());

        int resourceCount = 0;

        List<ItemCO> items = generalLiveCO.getItems();
        if (!CollectionUtils.isEmpty(items)) {
            resourceCount = items.size();
        }
        liveMPDetailCO.setResourceCount(resourceCount);

//        liveMPDetailCO.setSharedesc();

        liveMPDetailCO.setStartTime(generalLiveCO.getStartTime());

        liveMPDetailCO.setState(generalLiveCO.getState());

        liveMPDetailCO.setStatus(generalLiveCO.getStatus());

        liveMPDetailCO.setTitle(generalLiveCO.getTitle());

        liveMPDetailCO.setUserCount(generalLiveCO.getUv());

        liveMPDetailCO.setZid(generalLiveCO.getZid());

        liveMPDetailCO.setTemplateJsonStr(templateJson);

        if (Objects.nonNull(livePullUrlDTO) && Objects.equals(LiveEnum.LiveState.PLAYING.getCode(), generalLiveCO.getState().intValue())) {
            liveMPDetailCO.setFlvUrl(livePullUrlDTO.getFlvUrl());
            liveMPDetailCO.setHlsUrl(livePullUrlDTO.getHlsUrl());
            liveMPDetailCO.setRtmpUrl(livePullUrlDTO.getRtmpUrl());

        }

        liveMPDetailCO.setSharetitle(generalLiveCO.getTitle());

        liveMPDetailCO.setSharedesc(String.format(shareDesc, generalLiveCO.getCraftsmanName()));

        liveMPDetailCO.setSharepic(generalLiveCO.getPic());

        //如果预计直播则组装开始时间字符串
        if (generalLiveCO.getState().equals(LiveConst.STATE_LIVE_NOT_BEGIN)) {
            Date date = new Date(generalLiveCO.getPreStartTime());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (weekDay < 0) {
                weekDay = 0;
            }
            String weekDayStr = WEEK_DAYS[weekDay];
            liveMPDetailCO.setStarttmStr(weekDayStr + " " + sdf.format(date));
        }

        liveMPDetailCO.setScreenDirection(generalLiveCO.getScreenDirection());

        if (Objects.nonNull(liveZoo)) {
            liveMPDetailCO.setZrc(liveZoo.getZrc());
            liveMPDetailCO.setMuid(liveZoo.getUid());
        }


        return liveMPDetailCO;


    }


}

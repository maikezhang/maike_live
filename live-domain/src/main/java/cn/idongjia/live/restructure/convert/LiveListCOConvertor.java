package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.api.live.pojo.LiveApiResp;
import cn.idongjia.live.api.live.pojo.LiveItemResp;
import cn.idongjia.live.api.live.pojo.PlayBack;
import cn.idongjia.live.pojo.live.LiveListCO;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.LiveListDTO;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.pojo.co.live.LiveResourceCO;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component("liveListCOConvertor")
public class LiveListCOConvertor implements ConvertorI<LiveListCO, LiveEntity, LiveListDTO> {
    @Override
    public LiveListCO dataToClient(LiveListDTO liveListDTO) {
        LiveCO            liveCO        = liveListDTO.getLiveCO();
        Long              leastDuration = liveListDTO.getLeastDuration();
        List<PlayBackDTO> playBackDTOS  = liveListDTO.getPlayBackDTOS();
        LiveListCO        liveListCO    = new LiveListCO();
        liveListCO.setBookState(liveCO.getBookState());
        liveListCO.setCover(liveCO.getCover());
        liveListCO.setCraftsmanAvatar(liveCO.getCraftsmanAvatar());
        liveListCO.setCraftsmanCity(liveCO.getCraftsmanCity());
        liveListCO.setCraftsmanName(liveCO.getCraftsmanName());
        liveListCO.setCraftsmanTitle(liveCO.getCraftsmanTitle());
        liveListCO.setCraftsmanUserId(liveCO.getCraftsmanUserId());
        liveListCO.setHot(liveCO.getHot());
        liveListCO.setId(liveCO.getId());
        liveListCO.setPreStartTime(liveCO.getPreStartTime());
        liveListCO.setPullURL(liveCO.getPullURL());
        liveListCO.setResources(liveCO.getResources());
        liveListCO.setSessionId(liveCO.getSessionId());
        liveListCO.setState(liveCO.getState());
        liveListCO.setTitle(liveCO.getTitle());
        liveListCO.setType(liveCO.getType());
        liveListCO.setVideoCoverUrl(liveCO.getVideoCoverUrl());

        if (Objects.equals(liveCO.getState(), LiveEnum.LiveState.FINISHED.getCode())) {
            if (!CollectionUtils.isEmpty(playBackDTOS)) {
                List<PlayBack> playBacks      = new ArrayList<>();
                long           durationMillis = 0L;
                for (int i = 0; i < playBackDTOS.size(); i++) {
                    PlayBackDTO playBackDTO = playBackDTOS.get(i);
                    if (playBackDTO.getDuration() >= TimeUnit.MINUTES.toMillis(leastDuration)) {
                        playBacks.add(SearchIndexRespDTO.assemblePlayBack(playBackDTO));
                        durationMillis += playBackDTO.getDuration();
                    }
                }
                if (!CollectionUtils.isEmpty(playBacks)) {
                    // 计算回放时长
                    long duration2Sec = durationMillis / 1000;
                    long duration2Min = duration2Sec / 60;
                    long durationSec  = duration2Sec % 60;
                    liveListCO.setDuration((duration2Min < 10 ? "0" + duration2Min : duration2Min) + ":"
                            + (durationSec < 10 ? "0" + durationSec : durationSec + ""));
                    liveListCO.setPlayBacks(playBacks);
                } else {
                    return null;
                }
            } else {
                return null;
            }

        }
        return liveListCO;
    }

    public static LiveApiResp assembleLiveApiResp(LiveListDTO liveListDTO) {

        LiveCO            liveCO        = liveListDTO.getLiveCO();
        Long              leastDuration = liveListDTO.getLeastDuration();
        List<PlayBackDTO> playBackDTOS  = liveListDTO.getPlayBackDTOS();
        LiveApiResp       liveApiResp   = new LiveApiResp();
        liveApiResp.setAsid(liveCO.getSessionId());

        liveApiResp.setAvatar(liveCO.getCraftsmanAvatar());
        liveApiResp.setId(liveCO.getId());
        liveApiResp.setLiveType(liveCO.getType());
        liveApiResp.setName(liveCO.getCraftsmanName());
        liveApiResp.setPic(liveCO.getCover());
        liveApiResp.setPreStartTime(liveCO.getPreStartTime());
        liveApiResp.setState(liveCO.getState());
        liveApiResp.setTitle(liveCO.getTitle());
        liveApiResp.setUid(liveCO.getCraftsmanUserId());

        List<LiveResourceCO> resources = liveCO.getResources();
        if (!CollectionUtils.isEmpty(resources)) {
            List<LiveItemResp> items = resources.stream().map(resource -> {
                LiveItemResp resp = new LiveItemResp();
                resp.setId(resource.getId());
                resp.setState(resource.getState());
                resp.setPrice(resource.getPrice());
                resp.setPic(resource.getCover());
                return resp;
            }).collect(Collectors.toList());
            liveApiResp.setItems(items);
        }
        if (Objects.equals(liveCO.getState(), LiveEnum.LiveState.FINISHED.getCode())) {
            if (!CollectionUtils.isEmpty(playBackDTOS)) {
                List<PlayBack> playBacks      = new ArrayList<>();
                long           durationMillis = 0L;
                for (int i = 0; i < playBackDTOS.size(); i++) {
                    PlayBackDTO playBackDTO = playBackDTOS.get(i);
                    if (playBackDTO.getDuration() >= TimeUnit.MINUTES.toMillis(leastDuration)) {
                        playBacks.add(SearchIndexRespDTO.assemblePlayBack(playBackDTO));
                        durationMillis += playBackDTO.getDuration();
                    }
                }
                if (!CollectionUtils.isEmpty(playBacks)) {
                    // 计算回放时长
                    long duration2Sec = durationMillis / 1000;
                    long duration2Min = duration2Sec / 60;
                    long durationSec  = duration2Sec % 60;
                    liveApiResp.setDuration((duration2Min < 10 ? "0" + duration2Min : duration2Min) + ":"
                            + (durationSec < 10 ? "0" + durationSec : durationSec + ""));
                    liveApiResp.setPlayBacks(playBacks);
                } else {
                    return null;
                }
            } else {
                return null;
            }

        }
        return liveApiResp;

    }
}

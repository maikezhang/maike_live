package cn.idongjia.live.restructure.dto;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.api.live.pojo.PlayBack;
import cn.idongjia.live.db.mybatis.po.PlayBackPO;
import cn.idongjia.live.pojo.purelive.playback.PlayBackAdmin;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDO;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDetail;
import cn.idongjia.live.pojo.purelive.playback.PlayBackOld;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lc
 * @create at 2018/6/12.
 */
public class PlayBackDTO extends BaseDTO<PlayBackPO> {
    public PlayBackDTO(PlayBackPO entity) {
        super(entity);
    }

    public PlayBack assemblePlayBack() {

        long duration2Sec = entity.getDuration() / 1000;
        long duration2Min = duration2Sec / 60;
        long durationSec = duration2Sec % 60;
        PlayBack playBack = new PlayBack();
        playBack.setDuration((duration2Min < 10 ? "0" + duration2Min : duration2Min) + ":"
                + (durationSec < 10 ? "0" + durationSec : durationSec + ""));
        playBack.setId(entity.getId());
        playBack.setUrl(entity.getUrl());
        playBack.setDurationMillis(entity.getDuration());
        return playBack;
    }

    public static cn.idongjia.live.pojo.purelive.playback.PlayBack assembleV2PlayBack(LiveShowDTO liveShowDTO,List<PlayBackDTO> playBackDTOS,LivePureDTO livePureDTO,User user,Long leastDuration) {


        cn.idongjia.live.pojo.purelive.playback.PlayBack playBack = new cn.idongjia.live.pojo.purelive.playback.PlayBack();
        playBack.setHavatar(user.getAvatar());
        playBack.setHusername(user.getUsername());
        //组装直播信息
        String title = liveShowDTO.getTitle();
        playBack.setTitle(title);
        playBack.setPic(livePureDTO.getPic());
        playBack.setState(liveShowDTO.getState());
        playBack.setDesc(liveShowDTO.getShowDesc());
        playBack.setLid(liveShowDTO.getId());
        playBack.setPlayBacks(new ArrayList<>());
        playBack.setScreenDirection(liveShowDTO.getScreenDirection());
        Long durationSum = 0L;
        for (PlayBackDTO playBackDO : playBackDTOS) {
            PlayBackDetail detail = new PlayBackDetail();
            detail.setId(playBackDO.getId());
            if (TimeUnit.MINUTES.toMillis(leastDuration)
                    <= playBackDO.getDuration()) {
                //组装回放链接
                detail.setUrl(playBackDO.getUrl());
                //组装回放时长
                Long duration = playBackDO.getDuration();
                detail.setDurationMillis(duration);
                Long minute = TimeUnit.MILLISECONDS.toMinutes(duration);
                duration %= TimeUnit.MINUTES.toMillis(1);
                Long second = TimeUnit.MILLISECONDS.toSeconds(duration);
                detail.setDuration(minute + ":" + (second < 10 ? "0" + second : second));
                playBack.getPlayBacks().add(detail);
                durationSum = +playBackDO.getDuration();
            }
        }
        return playBack;
    }


    public PlayBackOld assemblePlayBackOld(Integer index, Long leastDuration, CustomerVo user, LiveShowDTO liveShowDTO, LivePureDTO livePureDTO) {
        PlayBackOld playBack = null;
        if (TimeUnit.MINUTES.toMillis(leastDuration) <= entity.getDuration()) {
            playBack = new PlayBackOld();
            playBack.setId(entity.getId());
            //组装回放链接
            playBack.setUrl(entity.getUrl());
            //组装回放时长
            Long duration = entity.getDuration();
            Long minute = TimeUnit.MILLISECONDS.toMinutes(duration);
            duration %= TimeUnit.MINUTES.toMillis(1);
            Long second = TimeUnit.MILLISECONDS.toSeconds(duration);
            playBack.setDuration(minute + ":" + (second < 10 ? "0" + second : second));
            //组装主播信息
            playBack.setHavatar(user.getAvatar());
            playBack.setHusername(user.getName());
            //组装直播信息
            String title = liveShowDTO.getTitle();
            if (null != index) {
                title = liveShowDTO.getTitle() + "(" + index + ")";
            }
            playBack.setTitle(title);
            playBack.setPic(livePureDTO.getPic());
            playBack.setState(liveShowDTO.getState());
            playBack.setDesc(livePureDTO.getDesc());
        }
        return playBack;
    }

    public PlayBackDO assemblePlayBackDO() {
        PlayBackDO playBackDO = new PlayBackDO();
        playBackDO.setCreateTm(entity.getCreateTime());
        playBackDO.setDuration(entity.getDuration());
//        playBackDO.setFileId(entity.getFileId());
        playBackDO.setId(entity.getId());
        playBackDO.setLid(entity.getLiveId());
        playBackDO.setModifiedTm(entity.getModifiedTime());
        playBackDO.setStatus(entity.getStatus());
        playBackDO.setUrl(entity.getUrl());
        return playBackDO;
    }

    public PlayBackAdmin assemblePlayBackAdmin(LiveShowDTO liveShowDTO) {
        PlayBackAdmin playBackAdmin = new PlayBackAdmin();
        //组装原始基本数据
        playBackAdmin.setId(entity.getId());
        playBackAdmin.setLid(entity.getLiveId());
        playBackAdmin.setUrl(entity.getUrl());
        playBackAdmin.setDuration(entity.getDuration());
        playBackAdmin.setStatus(entity.getStatus());
        playBackAdmin.setCreateTm(entity.getCreateTime());
        playBackAdmin.setModifiedTm(entity.getModifiedTime());
        playBackAdmin.setTitle(liveShowDTO.getTitle());
        //组装人类可读的时间
        Long duration = entity.getDuration();
        Long minute = TimeUnit.MILLISECONDS.toMinutes(duration);
        duration %= TimeUnit.MINUTES.toMillis(1);
        Long second = TimeUnit.MILLISECONDS.toSeconds(duration);
        playBackAdmin.setDurationStr(minute + ":" + (second < 10 ? "0" + second : second));
        return playBackAdmin;
    }

    
    
    
    public Long getDuration() {
        return entity.getDuration();
    }

    public Long getId() {
        return entity.getId();
    }

    public String getUrl() {
        return entity.getUrl();
    }

    public Long getLiveId() {
        return entity.getLiveId();
    }

    public void setCreateTime(Timestamp timestamp) {
        entity.setCreateTime(timestamp);
    }

    public void setStatus(int status) {
        entity.setStatus(status);
    }

    public Integer getStatus(){
        return entity.getStatus();
    }

    public void setId(Long id) {
        entity.setId(id);
    }

    public void setModifiedTime(Timestamp timestamp) {
        entity.setModifiedTime(timestamp);
    }
}

package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.playback.PlayBackAdmin;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDO;
import cn.idongjia.live.query.purelive.PlayBackSearch;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.PlayBack;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.domain.manager.LiveEntityManager;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.query.PlayBackQueryHandler;
import cn.idongjia.live.restructure.query.QueryFactory;
import cn.idongjia.live.restructure.repo.PlayBackRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/13.
 */
@Component
public class PlayBackBO {
    private static final Log LOGGER = LogFactory.getLog(PlayBackBO.class);
    @Resource
    private PlayBackQueryHandler playBackQueryHandler;
    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;
    @Resource
    private LiveEntityManager liveEntityManager;

    @Resource
    private PlayBackRepo playBackRepo;

    public List<PlayBackDO> listByLiveId(Long lid) {
        DBPlayBackQuery dbPlayBackQuery = DBPlayBackQuery.builder().liveIds(Arrays.asList(lid)).status(0).build();
        try {
            List<PlayBackDTO> playBackDTOS = playBackQueryHandler.list(dbPlayBackQuery).get();
            return playBackDTOS.stream().map(PlayBackDTO::assemblePlayBackDO).collect(Collectors.toList());
        } catch (Exception e) {
            PlayBackBO.LOGGER.error("查询回放失败{}", e);
            throw LiveException.failure("查询回放失败");
        }
    }

    public BaseList<PlayBackAdmin> listForAdmin(PlayBackSearch playBackSearch) {
        DBPlayBackQuery dbPlayBackQuery = QueryFactory.getInstance(playBackSearch);
        BaseList<PlayBackAdmin> baseList = new BaseList<>();
        if (dbPlayBackQuery != null) {
            try {
                int count = playBackQueryHandler.count(dbPlayBackQuery);
                baseList.setCount(count);
                if (count > 0) {
                    List<PlayBackDTO> playBackDTOS = playBackQueryHandler.list(dbPlayBackQuery).get();
                    List<Long> liveIds = playBackDTOS.stream().map(PlayBackDTO::getLiveId).collect(Collectors.toList());
                    Map<Long, LiveShowDTO> liveShowDTOMap = liveShowQueryHandler.map(DBLiveShowQuery.builder().ids(liveIds).build()).get();
                    List<PlayBackAdmin> playBackAdmins = playBackDTOS.stream().map(playBackDTO -> {
                        return playBackDTO.assemblePlayBackAdmin(liveShowDTOMap.get(playBackDTO.getLiveId()));
                    }).collect(Collectors.toList());
                    baseList.setItems(playBackAdmins);
                }

            } catch (Exception e) {
                PlayBackBO.LOGGER.error("查询回放失败{}", e);
                throw LiveException.failure("查询回放失败");
            }
        }
        return baseList;
    }


    public List<List<PlayBackDO>> getBatchPlayBackByLiveId(PlayBackSearch playBackSearch) {

        //过滤掉直播拍的直播id
        List<Long> liveIds = playBackSearch.getLiveIds();
        DBLiveShowQuery query = DBLiveShowQuery.builder().ids(liveIds).build();
        try {
            List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(query).get();
            List<Long> ids = liveShowDTOS.stream()
                    .filter(liveShowDTO -> liveShowDTO.getType().intValue() != LiveEnum.LiveType.LIVE_AUCTION.getCode())
                    .map(LiveShowDTO::getId).collect(Collectors.toList());
            playBackSearch.setLiveIds(ids);
        } catch (Exception e) {
            LOGGER.warn("查询直播失败，{}", e);
        }


        DBPlayBackQuery dbPlayBackQuery = QueryFactory.getInstance(playBackSearch);
        try {
            List<PlayBackDTO> playBackDTOS = playBackQueryHandler.list(dbPlayBackQuery).get();
            Map<Long, List<PlayBackDO>> playBackMap = playBackDTOS.stream().map(PlayBackDTO::assemblePlayBackDO)
                    .collect(Collectors.groupingBy(PlayBackDO::getLid));
            return playBackMap.values().stream().collect(Collectors.toList());
        } catch (Exception e) {
            PlayBackBO.LOGGER.error("查询回放失败{}", e);
            throw LiveException.failure("查询回放失败");
        }
    }


//    public Long addPlayBack(Long uid,String videoUrl,Long durationMillis){
//        Long lid=liveShowQueryHandler.getLiveShowOnByUid(uid);
//        if(Objects.isNull(lid)){
//            lid=liveShowQueryHandler.getLiveShowRecentlyEnd(uid);
//        }
//        LiveEntity entity=liveEntityManager.load(lid);
//
//
//        return entity.addPlayBack(videoUrl,durationMillis);
//
//    }

    public Long addPlayBack(PlayBackDO playBackDO) {
        LiveEntity entity = liveEntityManager.load(playBackDO.getLid());
        PlayBack playBack = do2PlayBack(playBackDO);
        entity.addPlayBack(playBack);
        return playBack.getId();
    }

    private PlayBack do2PlayBack(PlayBackDO playBackDO) {
        PlayBack playBack = PlayBack.builder()
                .status(playBackDO.getStatus())
                .url(playBackDO.getUrl())
                .duration(playBackDO.getDuration())
                .lid(playBackDO.getLid())
                .fileId(playBackDO.getFileId())
                .id(playBackDO.getId())
                .build();
        return playBack;
    }


    public boolean deletePlayBack(PlayBackDO playBackDO) {

        return playBackRepo.delete(playBackDO.getId());
    }


}

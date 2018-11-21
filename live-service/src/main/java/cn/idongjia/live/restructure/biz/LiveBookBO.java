package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.pojo.purelive.book.PureLiveBookDO;
import cn.idongjia.live.query.purelive.book.PureLiveBookSearch;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.LivePureBook;
import cn.idongjia.live.restructure.dto.AnchorBlackWhiteDTO;
import cn.idongjia.live.restructure.dto.AnchorBookDTO;
import cn.idongjia.live.restructure.dto.LiveBookDTO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.domain.manager.LiveEntityManager;
import cn.idongjia.live.restructure.query.*;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/13.
 */
@Component
public class LiveBookBO {
  private static final Log LOGGER = LogFactory.getLog(LiveBookBO.class);
  @Resource private LiveBookQueryHandler liveBookQueryHandler;

  @Resource private ConfigManager configManager;

  @Resource private LiveEntityManager liveEntityManager;

  @Resource private AnchorBookQueryHandler anchorBookQueryHandler;

  @Resource private LivePureQueryHandler livePureQueryHandler;

  @Resource private PlayBackQueryHandler playBackQueryHandler;

  @Resource private AnchorBlackWhiteQueryHandler anchorBlackWhiteQueryHandler;

  public List<PureLiveBookDO> list(PureLiveBookSearch pureLiveBookSearch) {
    DBLiveBookQuery dbLiveBookQuery = QueryFactory.getInstance(pureLiveBookSearch);
    try {
      dbLiveBookQuery.setStatus(LiveConst.STATUS_BANNER_NORMAL);
      List<LiveBookDTO> liveBookDTOS = liveBookQueryHandler.list(dbLiveBookQuery).get();
      return liveBookDTOS
          .stream()
          .map(
              liveBookDTO -> {
                return liveBookDTO.assemblePureLiveBookDO();
              })
          .collect(Collectors.toList());
    } catch (Exception e) {
      LiveBookBO.LOGGER.error("查询订阅记录失败{}", e);
      throw LiveException.failure("查询订阅记录失败");
    }
  }

  public Integer count(PureLiveBookSearch pureLiveBookSearch) {
    DBLiveBookQuery dbLiveBookQuery = QueryFactory.getInstance(pureLiveBookSearch);
    return liveBookQueryHandler.count(dbLiveBookQuery);
  }

  public List<Long> listUidsByLid(Long lid) {
    DBLiveBookQuery dbLiveBookQuery =
        DBLiveBookQuery.builder()
            .status(LiveConst.STATUS_BOOK_NORMAL)
            .liveIds(Arrays.asList(lid))
            .build();
    try {
      List<LiveBookDTO> liveBookDTOS = liveBookQueryHandler.list(dbLiveBookQuery).get();
      return liveBookDTOS.stream().map(LiveBookDTO::getUserId).collect(Collectors.toList());
    } catch (Exception e) {
      LiveBookBO.LOGGER.error("查询订阅记录失败{}", e);
      throw LiveException.failure("查询订阅记录失败");
    }
  }

  /**
   * 添加直播订阅
   *
   * @param pureLiveBookDO
   * @return
   */
  public Long addPureLiveBook(PureLiveBookDO pureLiveBookDO) {
    LiveEntity entity = liveEntityManager.load(pureLiveBookDO.getLid());
    LivePureBook livePureBook = new LivePureBook();
    livePureBook.setLid(pureLiveBookDO.getLid());
    livePureBook.setUid(pureLiveBookDO.getUid());
    return entity.addLiveBook(livePureBook);
  }

  /**
   * 取消直播订阅
   *
   * @return
   */
  public boolean delete(PureLiveBookDO pureLiveBookDO) {
    LiveEntity entity = liveEntityManager.load(pureLiveBookDO.getLid());
    LivePureBook livePureBook = new LivePureBook();
    livePureBook.setLid(pureLiveBookDO.getLid());
    livePureBook.setUid(pureLiveBookDO.getUid());
    return entity.deleteLiveBook(livePureBook);
  }

  /**
   * 获取提醒文案
   *
   * @param uid
   * @param anchorId
   * @return
   */
  public Map<String, String> acquireRemindTxt(Long uid, Long anchorId) {
    Map<String, String> reMap = new HashMap<>();

    // 如果此匠人被加入黑名单返回空
    AnchorBlackWhiteDTO anchorBlackWhiteDTO = anchorBlackWhiteQueryHandler.get(anchorId);
    if (Objects.nonNull(anchorBlackWhiteDTO)
        && (Objects.equals(
                AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode(),
                anchorBlackWhiteDTO.getType().intValue())
            || Objects.equals(
                AnchorBlackWhiteEnum.AnchorBlackWhiteType.NO_PLAY_TYPE.getCode(),
                anchorBlackWhiteDTO.getType().intValue()))) {
      reMap.put("remind", null);
      return reMap;
    }

    // 根据用户ID和主播ID获取订阅关系
    DBAnchorBookQuery query =
        DBAnchorBookQuery.builder()
            .anchorIds(Arrays.asList(anchorId))
            .userIds(Arrays.asList(uid))
            .status(Arrays.asList(LiveConst.STATUS_BOOK_NORMAL))
            .build();
    List<AnchorBookDTO> anchorBookDTOS = new ArrayList<>();
    try {
      anchorBookDTOS = anchorBookQueryHandler.list(query).get();

    } catch (Exception e) {
      LOGGER.warn("查询主播订阅失败{}", e);
      throw LiveException.failure("查询主播订阅失败");
    }
    if (!CollectionUtils.isEmpty(anchorBookDTOS)) {
      // 如果有订阅关系则返回null
      reMap.put("remind", null);
      return reMap;
    } else {
      // 如果没有订阅关系则计算内容数量
      Integer opening = livePureQueryHandler.countOpeningPureLiveByAnchor(anchorId);
      // 如果有正在直播和预告则只计算这两个数量
      if (opening != null && !opening.equals(0)) {
        reMap.put("remind", String.format(configManager.getUserLiveRemind(), opening));
        return reMap;
      } else {
        // 如果只有回放，则只计算回放的数量
        List<Long> overList = livePureQueryHandler.listOverPureLiveByAnchor(anchorId);
        int playBackNum = 0;
        if (overList != null && overList.size() != 0) {
          Map<Long, List<PlayBackDTO>> map = null;
          try {
            map =
                playBackQueryHandler.map(DBPlayBackQuery.builder().liveIds(overList).build()).get();

          } catch (Exception e) {
            LOGGER.warn("查询回放数据失败{}", e);
            throw LiveException.failure("查询回放数据失败");
          }
          for (Long lid : overList) {
            List<PlayBackDTO> playBackDTOS = map.get(lid);
            int flg = 0;
            if (!Utils.isEmpty(playBackDTOS)) {
              for (PlayBackDTO playBackDTO : playBackDTOS) {
                if (TimeUnit.MINUTES.toMillis(configManager.getLeastDuration())
                    <= playBackDTO.getDuration()) {
                  // 录制时间长度大于预设长度才能记录数量
                  // playBackNum++;
                  flg++;
                }
              }
            }
            if (flg > 0) {
              playBackNum++;
            }
          }
          if (playBackNum > 0) {
            // 设置回放数量
            reMap.put("remind", String.format(configManager.getUserPlayBackRemind(), playBackNum));
          }
        }
      }
    }
    return reMap;
  }
}

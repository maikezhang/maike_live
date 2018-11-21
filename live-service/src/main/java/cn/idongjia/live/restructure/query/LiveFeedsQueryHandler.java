package cn.idongjia.live.restructure.query;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.db.mybatis.mapper.TemplateTagRelMapper;
import cn.idongjia.live.db.mybatis.po.TemplateTagRelPO;
import cn.idongjia.live.db.mybatis.query.DBLiveBannerQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLivePureQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagRelQuery;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.db.mybatis.query.DBTemplateRelQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.purelive.ForeShow;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.playback.PlayBackOld;
import cn.idongjia.live.pojo.purelive.tag.ColumnTag;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.*;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.repo.HotAnchorsRepo;
import cn.idongjia.live.restructure.repo.LiveTagRepo;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component
public class LiveFeedsQueryHandler {
  private static final Log logger = LogFactory.getLog(LiveFeedsQueryHandler.class);

  @Resource private LiveTagRepo liveTagRepo;

  @Resource private LivePureQueryHandler livePureQueryHandler;

  @Resource private PlayBackQueryHandler playBackQueryHandler;

  @Resource private UserManager userManager;

  @Resource private ConfigManager configManager;

  @Resource private LiveBannerQueryHandler liveBannerQueryHandler;

  @Resource private LiveShowQueryHandler liveShowQueryHandler;
  @Resource private LiveZooQueryHandler liveZooQueryHandler;

  @Resource private LiveTagQueryHandler liveTagQueryHandler;

  @Resource private TemplateTagRelMapper templateTagRelMapper;

  @Resource private LiveBookQueryHandler liveBookQueryHandler;

  @Resource private ConvertorI<ForeShow, LiveEntity, ForeShowDTO> foreShowConvertor;
  @Resource private HotAnchorsRepo hotAnchorsRepo;

  @Resource private AnchorBlackWhiteQueryHandler anchorBlackWhiteQueryHandler;

  public List<Object> getFeedsListByLives(Long uid, List<LiveShowDTO> liveShowDTOS) {
    if (Utils.isEmpty(liveShowDTOS)) {
      return new ArrayList<>();
    }
    List<Object> resultList = new ArrayList<>();
    List<LiveShowDTO> notEndLiveShowDTOS =
        liveShowDTOS
            .stream()
            .filter(liveShowDTO -> !liveShowDTO.getState().equals(LiveConst.STATE_LIVE_OVER))
            .collect(Collectors.toList());

    List<PureLive> pureLives =
        livePureQueryHandler.assemblePureLive(notEndLiveShowDTOS, uid, false, false);
    resultList.addAll(pureLives);
    List<Long> playBackLiveIds =
        liveShowDTOS
            .stream()
            .filter(
                liveShowDTO ->
                    liveShowDTO.getState().equals(LiveConst.STATE_LIVE_OVER)
                        && liveShowDTO.getType().intValue()
                            != LiveEnum.LiveType.LIVE_AUCTION.getCode())
            .map(LiveShowDTO::getId)
            .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(playBackLiveIds)) {
      return resultList;
    }
    Future<Map<Long, List<PlayBackDTO>>> playBackFuture =
        (playBackQueryHandler.map(
            DBPlayBackQuery.builder().liveIds(playBackLiveIds).status(0).build()));

    Future<List<LivePureDTO>> listFuture =
        livePureQueryHandler.list(DBLivePureQuery.builder().liveIds(playBackLiveIds).build());
    Map<Long, List<PlayBackDTO>> playBackDTOS = null;
    List<LivePureDTO> livePureDTOS = null;
    try {
      playBackDTOS = playBackFuture.get();
      livePureDTOS = listFuture.get();
    } catch (Exception e) {
      logger.error("查询失败{}", e);
      throw LiveException.failure("查询失败");
    }
    Map<Long, LivePureDTO> livePureDTOMap =
        livePureDTOS
            .stream()
            .collect(Collectors.toMap(LivePureDTO::getId, v1 -> v1, (v1, v2) -> v1));
    List<LiveShowDTO> endLiveShowDTOS =
        liveShowDTOS
            .stream()
            .filter(liveShowDTO -> liveShowDTO.getState().equals(LiveConst.STATE_LIVE_OVER))
            .collect(Collectors.toList());

    Map<Long, List<PlayBackDTO>> finalPlayBackDTOS = playBackDTOS;
    List<cn.idongjia.live.pojo.purelive.playback.PlayBack> playBacks =
        endLiveShowDTOS
            .stream()
            .map(
                liveShowDTO -> {
                  List<PlayBackDTO> playBackDTOS1 = finalPlayBackDTOS.get(liveShowDTO.getId());
                  if (CollectionUtils.isEmpty(playBackDTOS1)) {
                    return null;
                  }
                  LivePureDTO livePureDTO = livePureDTOMap.get(liveShowDTO.getId());
                  if (livePureDTO == null) {
                    return null;
                  }
                  cn.idongjia.live.pojo.purelive.playback.PlayBack playBack =
                      PlayBackDTO.assembleV2PlayBack(
                          liveShowDTO,
                          playBackDTOS1,
                          livePureDTO,
                          userManager.getUser(uid),
                          configManager.getLeastDuration());

                  if (CollectionUtils.isEmpty(playBack.getPlayBacks())) {
                    return null;
                  }
                  return playBack;
                })
            .collect(Collectors.toList());

    resultList.addAll(playBacks);
    return resultList.stream().filter(result -> result != null).collect(Collectors.toList());
  }

  public Map<String, Object> getFeeds(Long uid, PureLiveSearch pureLiveSearch) {
    pureLiveSearch.setUid(uid);
    DBLiveShowQuery dbLiveShowQuery = QueryFactory.getInstance(pureLiveSearch);
    Map<String, Object> reMap = new HashMap<>(13);
    Integer page = dbLiveShowQuery.getPage();
    Integer limit = dbLiveShowQuery.getLimit();
    // 第一页要包含banner和预告数据
    if (page.equals(LiveConst.FIRST_PAGE)) {
      DBLiveBannerQuery dbLiveBannerQuery =
          DBLiveBannerQuery.builder().orderBy("weight desc").build();
      try {
        List<LiveBannerDTO> liveBannerDTOS = liveBannerQueryHandler.list(dbLiveBannerQuery).get();
        List<Long> liveIds =
            liveBannerDTOS
                .stream()
                .map(LiveBannerDTO::getClassificationId)
                .collect(Collectors.toList());
        if (Utils.isEmpty(liveIds)) {
          return new HashMap<>();
        }
        Map<Long, LiveShowDTO> liveShowDTOMap =
            liveShowQueryHandler.map(DBLiveShowQuery.builder().ids(liveIds).build()).get();
        Map<Long, LivePureDTO> livePureDTOMap =
            livePureQueryHandler.map(DBLivePureQuery.builder().liveIds(liveIds).build()).get();

        List<Long> userIds =
            liveShowDTOMap
                .values()
                .stream()
                .map(LiveShowDTO::getUserId)
                .collect(Collectors.toList());
        List<Long> zooIds =
            liveShowDTOMap
                .values()
                .stream()
                .map(LiveShowDTO::getZooId)
                .collect(Collectors.toList());
        Map<Long, CustomerVo> customerVoMap = userManager.takeBatchCustomer(userIds);
        Map<Long, LiveZoo> liveZooMap = liveZooQueryHandler.map(zooIds).get();
        //                List<PureLiveBannerDO> pureLiveBannerDOS = liveBannerDTOS.stream().map(b
        // -> {
        //                    LiveShowDTO liveShowDTO = liveShowDTOMap.get(b.getClassificationId());
        //                    return b.assemblePureLiveBannerDO(liveShowDTO,
        // livePureDTOMap.get(b.getClassificationId()), customerVoMap.get(liveShowDTO.getUserId()),
        // liveZooMap.get(liveShowDTO.getZooId()));
        //                }).collect(Collectors.toList());
        //                reMap.put("banner", pureLiveBannerDOS);
      } catch (Exception e) {
        logger.error("查询失败{}", e);
        throw LiveException.failure("查询失败");
      }

      dbLiveShowQuery.setStates(Arrays.asList(LiveConst.STATE_LIVE_NOT_BEGIN));
      // 获取预告数据，未开始、按权重和预计时间排序
      dbLiveShowQuery.setLimit(configManager.getForeShowNum());
      dbLiveShowQuery.setOnline(LiveConst.STATUS_LIVE_ONLINE);
      Long startMillis = Utils.getCurrentMillis();
      dbLiveShowQuery.setMinEstimatedStartTime(new Timestamp(startMillis));
      dbLiveShowQuery.setMaxEstimatedStartTime(
          new Timestamp(
              TimeUnit.DAYS.toMillis(configManager.getMaxForeShowDays())
                  + Utils.getCurrentMillis()));
      dbLiveShowQuery.setOrderBy("prestarttm, general_weight desc");
      try {
        List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
        List<Long> userIds =
            liveShowDTOS.stream().map(LiveShowDTO::getUserId).collect(Collectors.toList());
        List<Long> liveIds =
            liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());

        Map<Long, User> userMap = userManager.takeBatchUser(userIds);
        Map<Long, LivePureDTO> livePureDTOMap =
            livePureQueryHandler.map(DBLivePureQuery.builder().liveIds(liveIds).build()).get();
        //                userManager.takeFollowerList()
        List<ForeShow> foreShows =
            liveShowDTOS
                .stream()
                .map(
                    liveShowDTO -> {
                      ForeShowDTO foreShowDTO = new ForeShowDTO();
                      foreShowDTO.setBook(false);
                      LivePureDTO livePureDTO = livePureDTOMap.get(liveShowDTO.getId());
                      foreShowDTO.setLivePureDTO(livePureDTO);
                      foreShowDTO.setLiveShowDTO(liveShowDTO);
                      User user = userMap.get(liveShowDTO.getUserId());
                      foreShowDTO.setUser(user);
                      return foreShowConvertor.dataToClient(foreShowDTO);
                    })
                .collect(Collectors.toList());
        if (foreShows != null && foreShows.size() != 0) {
          reMap.put("foreshow", foreShows);
        }
      } catch (Exception e) {
        logger.error("查询失败{}", e);
        throw LiveException.failure("查询失败");
      }
    }
    // 获取feeds数据
    List<Map<String, Object>> feedsList = new ArrayList<>();
    // 获取正在直播和回放数据
    dbLiveShowQuery.setPage(page);
    dbLiveShowQuery.setLimit(limit);
    dbLiveShowQuery.setStates(
        Arrays.asList(LiveConst.STATE_LIVE_IN_PROGRESS, LiveConst.STATE_LIVE_OVER));
    dbLiveShowQuery.setOnline(LiveConst.STATUS_LIVE_ONLINE);
    dbLiveShowQuery.setStatus(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL));
    List<Object> lives = null;
    try {
      List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
      lives = getFeedsListByLives(uid, liveShowDTOS);
    } catch (Exception e) {
      logger.error("查询失败{}", e);
      throw LiveException.failure("查询失败");
    }

    // 填充直播所需要的直播数量
    Integer columnPos = configManager.getColumnPos() - 1;
    Integer anchorsPos = configManager.getHotAnchorsPos() - 1;
    Integer liveNum = lives.size();
    // 第一段需要直播的数量
    Integer firstNeedLiveNum = columnPos > anchorsPos ? anchorsPos : columnPos;
    // 第二段需要直播的数量
    Integer secondNeedLiveNum = Math.abs(anchorsPos - columnPos - 1);
    // 直播数量小于第一段需要数量则直接添加所有直播
    if (liveNum <= firstNeedLiveNum) {
      Map<String, Object> map =
          putFeedsData2Map(configManager.getHotLiveTitle(), LiveConst.TYPE_FEEDS_LIVE, lives);
      addFeedsList(feedsList, null, map);
      if (page.equals(LiveConst.FIRST_PAGE)) {
        Map<String, Object> columnMap = acquireHotColumns();
        Map<String, Object> anchorsMap = acquireHotAnchors(uid);
        addFeedsList(feedsList, null, columnMap);
        addFeedsList(feedsList, null, anchorsMap);
      }
    } else if (liveNum <= secondNeedLiveNum + firstNeedLiveNum && liveNum > firstNeedLiveNum) {
      if (page.equals(LiveConst.FIRST_PAGE)) {
        Map<String, Object> columnMap = acquireHotColumns();
        Map<String, Object> anchorsMap = acquireHotAnchors(uid);
        if (columnMap == null && anchorsMap == null) {
          Map<String, Object> map =
              putFeedsData2Map(configManager.getHotLiveTitle(), LiveConst.TYPE_FEEDS_LIVE, lives);
          addFeedsList(feedsList, null, map);
        } else if (columnMap != null && anchorsMap == null) {
          // 直播数量大于第一段需要数量但是小于第二段数量则分成两个map存储
          Map<String, Object> map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(0, firstNeedLiveNum));
          addFeedsList(feedsList, null, map);
          map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(firstNeedLiveNum, liveNum));
          addFeedsList(feedsList, null, map);
          addFeedsList(feedsList, 1, columnMap);
        } else if (columnMap == null) {
          // 直播数量大于第一段需要数量但是小于第二段数量则分成两个map存储
          Map<String, Object> map =
              putFeedsData2Map(configManager.getHotLiveTitle(), LiveConst.TYPE_FEEDS_LIVE, lives);
          addFeedsList(feedsList, null, map);
          addFeedsList(feedsList, null, anchorsMap);
        } else {
          // 直播数量大于第一段需要数量但是小于第二段数量则分成两个map存储
          Map<String, Object> map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(0, firstNeedLiveNum));
          addFeedsList(feedsList, null, map);
          map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(firstNeedLiveNum, liveNum));
          addFeedsList(feedsList, null, map);
          addFeedsList(feedsList, 1, columnMap);
          addFeedsList(feedsList, null, anchorsMap);
        }
      } else {
        Map<String, Object> map =
            putFeedsData2Map(configManager.getHotLiveTitle(), LiveConst.TYPE_FEEDS_LIVE, lives);
        addFeedsList(feedsList, null, map);
      }
    } else if (liveNum > secondNeedLiveNum + firstNeedLiveNum) {
      if (page.equals(LiveConst.FIRST_PAGE)) {
        Map<String, Object> columnMap = acquireHotColumns();
        Map<String, Object> anchorsMap = acquireHotAnchors(uid);
        if (anchorsMap == null && columnMap == null) {
          Map<String, Object> map =
              putFeedsData2Map(configManager.getHotLiveTitle(), LiveConst.TYPE_FEEDS_LIVE, lives);
          addFeedsList(feedsList, null, map);
        } else if (columnMap != null && anchorsMap == null) {
          Map<String, Object> map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(0, firstNeedLiveNum));
          addFeedsList(feedsList, null, map);
          map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(firstNeedLiveNum, liveNum));
          addFeedsList(feedsList, null, map);
          addFeedsList(feedsList, 1, columnMap);
        } else if (columnMap == null) {
          Map<String, Object> map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(0, firstNeedLiveNum + secondNeedLiveNum));
          addFeedsList(feedsList, null, map);
          map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(firstNeedLiveNum + secondNeedLiveNum, liveNum));
          addFeedsList(feedsList, null, map);
          addFeedsList(feedsList, 1, anchorsMap);
        } else {
          // 直播数量大于第二段数量，分成三个map
          Map<String, Object> map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(0, firstNeedLiveNum));
          addFeedsList(feedsList, null, map);
          map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(firstNeedLiveNum, firstNeedLiveNum + secondNeedLiveNum));
          addFeedsList(feedsList, null, map);
          map =
              putFeedsData2Map(
                  configManager.getHotLiveTitle(),
                  LiveConst.TYPE_FEEDS_LIVE,
                  lives.subList(firstNeedLiveNum + secondNeedLiveNum, liveNum));
          addFeedsList(feedsList, null, map);
          addFeedsList(feedsList, 1, columnMap);
          addFeedsList(feedsList, 3, anchorsMap);
        }
      } else {
        Map<String, Object> map =
            putFeedsData2Map(configManager.getHotLiveTitle(), LiveConst.TYPE_FEEDS_LIVE, lives);
        addFeedsList(feedsList, null, map);
      }
    }
    reMap.put("feeds", feedsList);
    return reMap;
  }

  public List<Object> getClassifiedFeeds(Long tid, PureLiveSearch pureLiveSearch) {

    DBLiveTagRelQuery dbLiveTagRelQuery =
        DBLiveTagRelQuery.builder()
            .tagIds(Arrays.asList(tid))
            .limit(pureLiveSearch.getLimit())
            .page(pureLiveSearch.getPage())
            .status(LiveConst.STATUS_TAG_REL_NORMAL)
            .build();
    List<LiveTagRelDTO> liveTagRelDTOS = liveTagRepo.list(dbLiveTagRelQuery);
    List<Long> liveIds =
        liveTagRelDTOS.stream().map(LiveTagRelDTO::getLiveId).collect(Collectors.toList());
    List<LiveShowDTO> liveShowDTOS = null;
    try {
      liveShowDTOS =
          liveShowQueryHandler.list(DBLiveShowQuery.builder().ids(liveIds).build()).get();
    } catch (Exception e) {
      logger.error("查询失败{}", e);
      throw LiveException.failure("查询失败");
    }
    return getFeedsListByLives(null, liveShowDTOS);
  }

  private void addFeedsList(
      List<Map<String, Object>> feedsList, Integer pos, Map<String, Object> data) {
    if (data != null && pos == null) {
      feedsList.add(data);
    } else if (data != null) {
      feedsList.add(pos, data);
    }
  }

  private Map<String, Object> putFeedsData2Map(String title, Integer type, List data) {
    if (data.size() == 0) {
      // 如果数据为空则直接返回null
      return null;
    }
    Map<String, Object> map = new HashMap<>();
    map.put("title", title);
    map.put("type", type);
    map.put("data", data);
    return map;
  }

  private Map<String, Object> acquireHotColumns() {
    // 获取热门栏目数据
    DBLiveTagQuery dbPureLiveTagQuery =
        DBLiveTagQuery.builder()
            .type(LiveConst.TYPE_TAG_COLUMN)
            .status(LiveConst.STATUS_TAG_NORMAL)
            .orderBy("weight desc")
            .build();

    try {
      List<LiveTagDTO> pureLiveTagDTOS = liveTagQueryHandler.list(dbPureLiveTagQuery).get();
      List<Long> tagIds =
          pureLiveTagDTOS.stream().map(LiveTagDTO::getId).collect(Collectors.toList());
      DBTemplateRelQuery dbTemplateRelQuery = DBTemplateRelQuery.builder().tagIds(tagIds).build();
      List<TemplateTagRelPO> templateTagRelPOS = templateTagRelMapper.list(dbTemplateRelQuery);
      Map<Long, TemplateTagRelDTO> templateTagRelDTOMap =
          templateTagRelPOS
              .stream()
              .collect(
                  Collectors.toMap(
                      TemplateTagRelPO::getTagId,
                      v1 -> {
                        return new TemplateTagRelDTO(v1);
                      },
                      (v1, v2) -> v1));
      List<ColumnTag> columnTags =
          pureLiveTagDTOS
              .stream()
              .map(
                  tag -> {
                    return tag.assembleColumnTag(templateTagRelDTOMap.get(tag.getId()));
                  })
              .collect(Collectors.toList());
      return putFeedsData2Map(
          configManager.getHotColumnTitle(), LiveConst.TYPE_FEEDS_COLUMN, columnTags);
    } catch (Exception e) {
      logger.error("查询失败{}", e);
      throw LiveException.failure("查询失败");
    }
  }

  private Map<String, Object> acquireHotAnchors(Long uid) {
    // 获取热门主播数据
    List<Anchor> anchors = hotAnchorsRepo.listRandomly(uid);
    return putFeedsData2Map(
        configManager.getHotAnchorTitle(), LiveConst.TYPE_FEEDS_ANCHOR, anchors);
  }

  public List<Object> getBookCenterFeeds(Long uid, PureLiveSearch pureLiveSearch) {
    List<LiveBookDTO> liveBookDTOS = null;
    try {
      liveBookDTOS =
          liveBookQueryHandler
              .list(
                  DBLiveBookQuery.builder()
                      .status(LiveConst.STATUS_BANNER_NORMAL)
                      .userId(uid)
                      .page(pureLiveSearch.getPage())
                      .limit(pureLiveSearch.getLimit())
                      .build())
              .get();
    } catch (Exception e) {
      logger.error("查询失败{}", e);
      throw LiveException.failure("查询失败");
    }
    if (Utils.isEmpty(liveBookDTOS)) {
      return new ArrayList<>();
    }
    List<Long> liveIds =
        liveBookDTOS.stream().map(LiveBookDTO::getLiveId).collect(Collectors.toList());
    List<LiveShowDTO> liveShowDTOS = null;
    try {
      liveShowDTOS =
          liveShowQueryHandler.list(DBLiveShowQuery.builder().ids(liveIds).build()).get();
    } catch (Exception e) {
      logger.error("查询失败{}", e);
      throw LiveException.failure("查询失败");
    }
    return getFeedsListByLives(uid, liveShowDTOS);
  }

  public List<Object> listPureLiveByCraftsIdOld(Long uid, PureLiveSearch pureLiveSearch) {
    pureLiveSearch.setUid(uid);
    DBLiveShowQuery dbLiveShowQuery = QueryFactory.getInstance(pureLiveSearch);
    try {
      Map<Long, LiveShowDTO> liveShowDTOMap = liveShowQueryHandler.map(dbLiveShowQuery).get();
      List<Long> liveIds =
          liveShowDTOMap.values().stream().map(LiveShowDTO::getId).collect(Collectors.toList());
      List<Long> userIds =
          liveShowDTOMap.values().stream().map(LiveShowDTO::getUserId).collect(Collectors.toList());

      Map<Long, LivePureDTO> livePureDTOMap =
          livePureQueryHandler.map(DBLivePureQuery.builder().liveIds(liveIds).build()).get();
      Map<Long, CustomerVo> customerVoMap = userManager.takeBatchCustomer(userIds);
      return getFeedsListByLivesOld(liveShowDTOMap, livePureDTOMap, customerVoMap);

    } catch (Exception e) {
      logger.warn("获取直播失败{}", e);
      throw new LiveException(-12138, "获取直播失败");
    }
  }

  private List<Object> getFeedsListByLivesOld(
      Map<Long, LiveShowDTO> liveShowDTOMap,
      Map<Long, LivePureDTO> livePureDTOMap,
      Map<Long, CustomerVo> customerVoMap) {
    List<Object> resultList = new ArrayList<>();
    Collection<LiveShowDTO> liveShowDTOS = liveShowDTOMap.values();
    List<Long> playBackLiveIds =
        liveShowDTOS
            .stream()
            .filter(
                liveShowDTO ->
                    liveShowDTO.getState().equals(LiveConst.STATE_LIVE_OVER)
                        && liveShowDTO.getType().intValue()
                            != LiveEnum.LiveType.LIVE_AUCTION.getCode())
            .map(LiveShowDTO::getId)
            .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(playBackLiveIds)) {
      return resultList;
    }
    Future<List<PlayBackDTO>> playBackFuture =
        (playBackQueryHandler.list(
            DBPlayBackQuery.builder().liveIds(playBackLiveIds).status(0).build()));
    List<LiveShowDTO> notEndLiveShowDTOS =
        liveShowDTOS
            .stream()
            .filter(liveShowDTO -> !liveShowDTO.getState().equals(LiveConst.STATE_LIVE_OVER))
            .collect(Collectors.toList());
    List<PureLive> pureLives =
        livePureQueryHandler.assemblePureLive(notEndLiveShowDTOS, null, false, false);
    resultList.addAll(pureLives);
    List<PlayBackDTO> playBackDTOS = null;
    try {
      playBackDTOS = playBackFuture.get();
    } catch (Exception e) {
      logger.error("查询失败{}", e);
      throw LiveException.failure("查询失败");
    }
    int index = 1;
    List<PlayBackOld> playBackOlds = new ArrayList<>();
    for (int i = 0; i < playBackDTOS.size(); i++) {
      PlayBackDTO playBackDTO = playBackDTOS.get(i);
      LiveShowDTO liveShowDTO = liveShowDTOMap.get(playBackDTO.getLiveId());
      PlayBackOld playBackOld =
          playBackDTO.assemblePlayBackOld(
              index,
              configManager.getLeastDuration(),
              customerVoMap.get(liveShowDTO.getUserId()),
              liveShowDTO,
              livePureDTOMap.get(playBackDTO.getLiveId()));
      if (null != playBackOld) {
        ++index;
      }
      playBackOlds.add(playBackOld);
    }
    resultList.addAll(playBackOlds);
    return resultList;
  }

  public List<Object> listPureLiveByCraftsId(Long uid, PureLiveSearch pureLiveSearch) {

    if (Objects.isNull(uid)) {
      return new ArrayList<>();
    }
    // 检查匠人是否在黑名单中
    AnchorBlackWhiteDTO anchorBlackWhiteDTO = anchorBlackWhiteQueryHandler.get(uid);
    if (Objects.nonNull(anchorBlackWhiteDTO)
        && (Objects.equals(
                anchorBlackWhiteDTO.getType().intValue(),
                AnchorBlackWhiteEnum.AnchorBlackWhiteType.NO_PLAY_TYPE.getCode())
            || Objects.equals(
                anchorBlackWhiteDTO.getType().intValue(),
                AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode()))) {
      return new ArrayList<>();
    }

    pureLiveSearch.setUid(uid);
    DBLiveShowQuery dbLiveShowQuery = QueryFactory.getInstance(pureLiveSearch);
    dbLiveShowQuery.setOnline(LiveConst.STATUS_LIVE_ONLINE);
    dbLiveShowQuery.setOrderBy(LiveConst.CRAFTS_LIVE_SQL);
    dbLiveShowQuery.setTypes(Arrays.asList(LiveConst.TYPE_LIVE_NORMAL));
    try {
      List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
      return getFeedsListByLives(uid, liveShowDTOS);
    } catch (Exception e) {
      logger.error("查询失败{}", e);
      throw LiveException.failure("查询失败");
    }
  }
}

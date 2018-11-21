package cn.idongjia.live.restructure.query;

import cn.idongjia.divine.lib.pojo.request.live.LiveQry;
import cn.idongjia.divine.lib.pojo.request.sort.SortType;
import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveBannerQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveModuleQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagRelQuery;
import cn.idongjia.live.db.mybatis.query.DBPageTabLiveQuery;
import cn.idongjia.live.db.mybatis.query.DBPageTabQuery;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.query.live.LiveBannerSearch;
import cn.idongjia.live.query.live.LiveModuleSearch;
import cn.idongjia.live.query.live.LiveShowSearch;
import cn.idongjia.live.query.purelive.PlayBackSearch;
import cn.idongjia.live.query.purelive.book.AnchorsBookSearch;
import cn.idongjia.live.query.purelive.book.PureLiveBookSearch;
import cn.idongjia.live.query.purelive.tag.PureLiveTagRelSearch;
import cn.idongjia.live.query.purelive.tag.PureLiveTagSearch;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.pojo.query.LiveMPQry;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveQry;
import cn.idongjia.live.restructure.pojo.query.PageTabQry;
import cn.idongjia.live.restructure.pojo.query.UserStageLiveQry;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.LiveUtil;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.se.lib.engine.query.Direction;
import cn.idongjia.search.pojo.query.Sort;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.util.Utils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询条件工厂
 *
 * @author lc
 * @create at 2018/6/11.
 */
public class QueryFactory {

    public static DBLiveShowQuery getInstance(LiveShowSearch liveShowSearch) {

        List<Long> ids = new ArrayList<>();
        if (liveShowSearch.getId() != null && liveShowSearch.getLiveIds() == null) {
            ids.add(liveShowSearch.getId());
        } else if (liveShowSearch.getId() == null && liveShowSearch.getLiveIds() != null) {
            ids.addAll(liveShowSearch.getLiveIds());
        } else if (liveShowSearch.getId() != null && liveShowSearch.getLiveIds() != null
                && liveShowSearch.getLiveIds
                ().contains(liveShowSearch.getId())) {
            ids.add(liveShowSearch.getId());
        } else if (liveShowSearch.getId() != null && liveShowSearch.getLiveIds() != null
                && !liveShowSearch.getLiveIds
                ().contains(liveShowSearch.getId())) {
            return null;
        } else {
            ids = null;
        }
        List<Long> uids = null;
        if (liveShowSearch.getUid() != null) {
            uids = new ArrayList<>();
            uids.add(liveShowSearch.getUid());
        }

        List<Integer> types = null;
        if (liveShowSearch.getType() != null) {
            types = new ArrayList<>();
            types.add(liveShowSearch.getType());
        }
        String orderBy = null;
        if (liveShowSearch.getOrderBy() != null && !liveShowSearch.getOrderBy().trim().equals("")) {
            StringBuffer order = new StringBuffer();
            order.append(liveShowSearch.getOrderBy()).append(" ").append(liveShowSearch.getOrder());
            orderBy = order.toString();
        }
        List<Integer> onlineStatus = new ArrayList<>();
        onlineStatus.add(LiveConst.STATUS_LIVE_ONLINE);
        onlineStatus.add(LiveConst.STATUS_LIVE_OFFLINE);
        List<Integer> states = new ArrayList<>();
        Integer       online = null;
        if (liveShowSearch.getState() != null && Utils.isEmpty(liveShowSearch.getStateList())) {
            states.add(liveShowSearch.getState());
        } else if (liveShowSearch.getState() == null && !Utils.isEmpty(liveShowSearch.getStateList())) {
            states.addAll(liveShowSearch.getStateList());
        } else if (liveShowSearch.getState() != null && !Utils.isEmpty(liveShowSearch.getStateList())
                && liveShowSearch
                .getStateList().contains(liveShowSearch.getState())) {
            states.add(liveShowSearch.getState());
        } else {
            states = null;
        }
        return DBLiveShowQuery.builder().ids(ids).title(liveShowSearch.getTitle()).userIds(uids)
                .types(types).states
                        (states)
                .minCreateTime(liveShowSearch.getCreateTmBegin() == null ? null : new Timestamp
                        (liveShowSearch.getCreateTmBegin()))
                .maxCreateTime(liveShowSearch.getCreateTmEnd() == null ? null :
                        new Timestamp(liveShowSearch.getCreateTmEnd()))
                .minEstimatedStartTime(liveShowSearch.getPreStartTmBegin() == null ? null : new Timestamp
                        (liveShowSearch.getPreStartTmBegin()))
                .maxEstimatedStartTime(liveShowSearch.getPreStartTmEnd() == null ? null : new Timestamp
                        (liveShowSearch.getPreStartTmEnd()))
                .minEstimatedEndTime(liveShowSearch.getPreEndTimeFrom() == null ? null : new Timestamp(liveShowSearch
                        .getPreEndTimeFrom()))
                .maxEstimatedEndTime(liveShowSearch.getPreEndTimeEnd() == null ? null : new Timestamp(liveShowSearch
                        .getPreEndTimeEnd())).online(online)
                .zooId(liveShowSearch.getZid())
                .page(liveShowSearch.getPage())
                .limit(liveShowSearch.getLimit())
                .orderBy(orderBy)
                .build();
    }

    public static DBLiveShowQuery getInstance(LiveSearch liveSearch) {

        List<Long> ids = new ArrayList<>();
        if (liveSearch.getId() != null) {
            ids.add(liveSearch.getId());
        } else {
            ids = null;
        }
        List<Long> uids = null;
        if (liveSearch.getUid() != null) {
            uids = new ArrayList<>();
            uids.add(liveSearch.getUid());
        }

        List<Integer> types = null;
        if (liveSearch.getType() != null) {
            types = new ArrayList<>();
            types.add(liveSearch.getType());
        }

        List<Integer> states = null;
        if (liveSearch.getState() != null) {
            states = new ArrayList<>();
            states.add(liveSearch.getState());
        }
        // order排序，1=倒序，0=正序
        String orderBy = null;
        if (liveSearch.getOrderBy() != null) {
            StringBuffer order = new StringBuffer();
            order.append(liveSearch.getOrderBy()).append(" ").append(liveSearch.getOrder());
            orderBy = order.toString();
        }
        return DBLiveShowQuery.builder().ids(ids).title(liveSearch.getTitle()).userIds(uids).types(types)
                .states(states)
                .minCreateTime(liveSearch.getCreateTimeFrom() == null ? null : new Timestamp(liveSearch
                        .getCreateTimeFrom()))
                .maxCreateTime(liveSearch.getCreateTimeEnd() == null ? null : new
                        Timestamp(liveSearch.getCreateTimeEnd()))
                .minEstimatedStartTime(liveSearch.getPreStartTimeFrom() == null ? null : new Timestamp(liveSearch
                        .getPreStartTimeFrom()))
                .maxEstimatedStartTime(
                        liveSearch.getPreStartTimeEnd() == null ? null : new Timestamp(liveSearch
                                .getPreStartTimeEnd()))
                .minEstimatedEndTime(liveSearch.getPreEndTimeFrom() == null ? null :
                        new Timestamp(liveSearch.getPreEndTimeFrom()))
                .maxEstimatedEndTime(liveSearch
                        .getPreEndTimeEnd() == null ? null : new Timestamp(liveSearch.getPreEndTimeEnd()))
                .online(liveSearch.getOnline())
                .page(liveSearch.getPage()).limit(liveSearch.getLimit()).orderBy(orderBy)
                .build();
    }

    public static LiveQuery assembleLiveQuery(LiveSearch liveSearch) {
        LiveQuery liveQuery = new LiveQuery();
        liveQuery.setStart(liveSearch.getOffset());
        liveQuery.setNum(liveSearch.getLimit());
        List<Sort> sorts = new ArrayList<>();
        // order排序，1=倒序，0=正序
        if (liveSearch.getOrderBy() != null) {
            sorts.add(new Sort(liveSearch.getOrderBy(), "1".equals(liveSearch.getOrder()) ? SolrQuery.ORDER.desc :
                    SolrQuery.ORDER.asc));
        }
        liveQuery.setSortList(sorts);
        liveQuery.setLiveId(liveSearch.getId());
        liveQuery.setTitle(liveSearch.getTitle());
        liveQuery.setUid(liveSearch.getUid());
        liveQuery.setOnLine(liveSearch.getOnline());
        liveQuery.setCreateTimeEnd(liveSearch.getCreateTimeEnd());
        liveQuery.setCreateTimeFrom(liveSearch.getCreateTimeFrom());
        liveQuery.setPreStartTimeFrom(liveSearch.getPreStartTimeFrom());
        liveQuery.setPreStartTimeEnd(liveSearch.getPreStartTimeEnd());
        liveQuery.setPreEndTimeFrom(liveSearch.getPreEndTimeFrom());
        liveQuery.setPreEndTimeEnd(liveSearch.getPreEndTimeEnd());
        liveQuery.setStartTimeFrom(liveSearch.getStartTimeFrom());
        liveQuery.setStartTimeEnd(liveSearch.getStartTimeEnd());
        liveQuery.setEndTimeFrom(liveSearch.getEndTimeFrom());
        liveQuery.setEndTimeEnd(liveSearch.getEndTimeEnd());
        if (liveSearch.getState() != null) {
            liveQuery.setStateList(Collections.singletonList(liveSearch.getState()));
        }
        if (liveSearch.getType() != null) {
            liveQuery.setTypeList(Collections.singletonList(liveSearch.getType()));
        }
        return liveQuery;
    }

    public static DBPlayBackQuery getInstance(PlayBackSearch playBackSearch) {
        List<Long> liveIds = null;
        if (playBackSearch.getLid() == null && playBackSearch.getLiveIds() != null) {
            liveIds = playBackSearch.getLiveIds();
        } else if (playBackSearch.getLid() != null && Utils.isEmpty(playBackSearch.getLiveIds())) {
            liveIds = Arrays.asList(playBackSearch.getLid());
        } else if (playBackSearch.getLid() != null && !Utils.isEmpty(playBackSearch.getLiveIds())) {
            if (playBackSearch.getLiveIds().contains(playBackSearch.getId())) {
                liveIds = Arrays.asList(playBackSearch.getLid());
            } else {
                return null;
            }
        }
        return DBPlayBackQuery.builder().duration(playBackSearch.getDuration()).id(playBackSearch.getId()).liveIds
                (liveIds)
                .status(playBackSearch.getStatus()).page(playBackSearch.getPage()).limit(playBackSearch.getLimit())
                .build();
    }

    public static DBLiveBannerQuery getInstance(LiveBannerSearch liveBannerSearch) {
        return DBLiveBannerQuery.builder().beginTime(liveBannerSearch.getBeginTime() == null ? null : liveBannerSearch.getBeginTime().getTime())
                .id(liveBannerSearch.getId()).endTime(liveBannerSearch.getEndTime() == null ? null : liveBannerSearch.getEndTime().getTime())
                .limit(liveBannerSearch.getLimit()).page(liveBannerSearch.getPage()).status(liveBannerSearch.getStatus()).orderBy(liveBannerSearch.getOrderBy()).title(liveBannerSearch.getTitle())
                .classificationId(liveBannerSearch.getClassificationId()).build();

    }

    public static DBLiveBookQuery getInstance(PureLiveBookSearch pureLiveBookSearch) {
        return DBLiveBookQuery.builder().userId(pureLiveBookSearch.getUid()).beginTime(pureLiveBookSearch
                .getBeginTime() == null ? null : pureLiveBookSearch.getBeginTime().getTime())
                .endTime(pureLiveBookSearch.getEndTime() == null ? null : pureLiveBookSearch.getEndTime().getTime())
                .limit(pureLiveBookSearch.getLimit()).page(pureLiveBookSearch.getPage()).liveIds(pureLiveBookSearch
                        .getLid() == null ? null : Arrays.asList(pureLiveBookSearch.getLid()))
                .status(pureLiveBookSearch.getStatus()).orderBy(pureLiveBookSearch.getOrderBy()).id
                        (pureLiveBookSearch.getId()).build();
    }

    public static DBAnchorBookQuery getInstance(AnchorsBookSearch anchorsBookSearch) {
        return DBAnchorBookQuery.builder()
                .anchorIds(anchorsBookSearch.getAnchorId() == null ? null : Arrays.asList(anchorsBookSearch
                        .getAnchorId()))
                .beginTime(anchorsBookSearch.getBeginTime() == null ? null : anchorsBookSearch.getBeginTime().getTime())
                .endTime(anchorsBookSearch.getEndTime() == null ? null : anchorsBookSearch.getEndTime().getTime())
                .id(anchorsBookSearch.getId())
                .userIds(anchorsBookSearch.getUid() == null ? null : Arrays.asList(anchorsBookSearch.getUid()))
                .page(anchorsBookSearch.getPage()).limit(anchorsBookSearch.getLimit())
                .status(anchorsBookSearch.getStatus() == null ? null : Arrays.asList(anchorsBookSearch.getStatus()))
                .build();
    }

    public static DBLiveTagQuery getInstance(PureLiveTagSearch pureLiveTagSearch) {
        return DBLiveTagQuery.builder().id(pureLiveTagSearch.getId()).tagIds(Arrays.asList(pureLiveTagSearch.getId())
        ).status(pureLiveTagSearch.getStatus()).type(pureLiveTagSearch.getType()).page(pureLiveTagSearch.getPage())
                .limit(pureLiveTagSearch.getLimit()).beginTime(LiveUtil.timestampeToMills(pureLiveTagSearch
                        .getBeginTime())).endTime(LiveUtil.timestampeToMills(pureLiveTagSearch.getEndTime()))
                .name(pureLiveTagSearch.getName())
                .build();
    }

    public static DBLiveTagRelQuery getInstance(PureLiveTagRelSearch pureLiveTagRelSearch) {
        return DBLiveTagRelQuery.builder().id(pureLiveTagRelSearch.getId()).beginTime(LiveUtil.timestampeToMills
                (pureLiveTagRelSearch.getBeginTime())).endTime(LiveUtil.timestampeToMills(pureLiveTagRelSearch
                .getEndTime()))
                .tagIds(Arrays.asList(pureLiveTagRelSearch.getTagId())).relIds(pureLiveTagRelSearch.getId() == null ?
                        null : Arrays.asList(pureLiveTagRelSearch.getId()))
                .liveIds(pureLiveTagRelSearch.getLid() == null ? null : Arrays.asList(pureLiveTagRelSearch.getLid()))
                .status(pureLiveTagRelSearch.getStatus())
                .build();
    }

    public static DBLiveModuleQuery getInstance(LiveModuleSearch liveModuleSearch) {
        return DBLiveModuleQuery.builder().beginTime(liveModuleSearch.getBeginTime() == null ? null : liveModuleSearch.getBeginTime().getTime())
                .id(liveModuleSearch.getId()).endTime(liveModuleSearch.getEndTime() == null ? null : liveModuleSearch.getEndTime().getTime())
                .limit(liveModuleSearch.getLimit()).page(liveModuleSearch.getPage()).orderBy(liveModuleSearch.getOrderBy()).position(liveModuleSearch.getPosition()).startTime(liveModuleSearch.getStartTime())
                .state(liveModuleSearch.getState()).status(liveModuleSearch.getStatus()).title(liveModuleSearch.getTitle()).positions(liveModuleSearch.getPositions()).build();
    }

    public static DBUserStageLiveQuery getInstance(UserStageLiveQry userStageLiveQry) {
        return DBUserStageLiveQuery.builder().status(userStageLiveQry.getStatus()).page(userStageLiveQry.getPage()).limit(userStageLiveQry.getLimit())
                .orderBy(userStageLiveQry.getOrderBy()).stages(Arrays.asList(userStageLiveQry.getType())).build();
    }

    public static DBPageTabQuery getInstance(PageTabQry pageTabQry) {
        Long       tabQryId = pageTabQry.getId();
        List<Long> tabIds   = null;
        if (null != tabQryId) {
            tabIds = new ArrayList<>();
            tabIds.add(tabQryId);
        }
        return DBPageTabQuery.builder()
                .ids(tabIds)
                .type(pageTabQry.getType())
                .orderBy(pageTabQry.getOrderBy())
                .page(pageTabQry.getPage())
                .online(pageTabQry.getOnline())
                .status(pageTabQry.getStatus())
                .limit(pageTabQry.getLimit())
                .build();
    }

    public static Optional<DBPageTabLiveQuery> getInstance(PageTabLiveQry pageTabLiveQry) {
        Long sessionId = pageTabLiveQry.getSessionId();
        SessionQueryHandler sessionQueryHandler = SpringUtils.takeBean("sessionQueryHandler", SessionQueryHandler
                .class);
        Long liveId = null;
        if (sessionId != null) {
            Map<Long, Session4Live> sessionMap     = sessionQueryHandler.listSessionSimpleData(Arrays.asList(sessionId));
            Session4Live            auctionSession = sessionMap.get(sessionId);
            if (auctionSession != null) {
                liveId = auctionSession.getLiveId();
            }else{
                liveId=-1L;
            }
        }
        List<Long> liveIds = new ArrayList<>();
        liveIds.add(pageTabLiveQry.getLiveId());
        liveIds.add(liveId);
        liveIds = liveIds.stream().filter(id -> id != null).distinct().collect(Collectors.toList());
        if (Utils.isEmpty(liveIds)) {
            liveIds = null;
        } else if (liveIds.size() > 1) {
            return Optional.empty();
        }
        if (!Objects.isNull(pageTabLiveQry.getTitle())) {
            PageTabLiveQueryHandler pageTabLiveQueryHandler = SpringUtils.takeBean("pageTabLiveQueryHandler", PageTabLiveQueryHandler.class);
            DBPageTabLiveQuery build = DBPageTabLiveQuery.builder()
                    .liveTitle(pageTabLiveQry.getTitle())
                    .tabId(pageTabLiveQry.getTabId())
                    .limit(pageTabLiveQry.getLimit())
                    .page(pageTabLiveQry.getPage())
                    .build();
            List<PageTabLiveE> pageTabLives = pageTabLiveQueryHandler.getPageTabLives(build);
            if (!CollectionUtils.isEmpty(pageTabLives)) {
                List<Long> liveIdIndexs = pageTabLives.stream().map(PageTabLiveE::getLiveId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(liveIdIndexs)) {
                    if (!CollectionUtils.isEmpty(liveIds)) {
                        liveIds.addAll(liveIdIndexs);
                    } else {
                        liveIds = liveIdIndexs;
                    }
                }
            }else {
                return Optional.empty();
            }
        }
        DBPageTabLiveQuery pageTabLiveQuery = DBPageTabLiveQuery.builder()
                .liveIds(liveIds)
                .tabId(pageTabLiveQry.getTabId())
                .page(pageTabLiveQry.getPage())
                .limit(pageTabLiveQry.getLimit())
                .orderBy(pageTabLiveQry.getOrderBy())
                .build();
        return Optional.of(pageTabLiveQuery);
    }

    /**
     * 组装直播小程序查询参数
     *
     * @param liveMPQry
     * @param flag      0-后台 1-前端
     * @return
     */
    public static LiveQry assembleLiveQry(LiveMPQry liveMPQry, Integer flag) {
        LiveQry qry = new LiveQry();
        qry.setLimit(liveMPQry.getLimit());
        qry.setPage(liveMPQry.getPage());


        qry.setTitle(liveMPQry.getTitle());
        if (Objects.nonNull(liveMPQry.getLiveId())) {
            qry.setLiveIds(Arrays.asList(liveMPQry.getLiveId()));
        }

        qry.setShowLocations(Arrays.asList(AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode()
                , AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode()));
        qry.setMaxPreStartTime(liveMPQry.getMaxPreStartTime());
        qry.setMinPreStartTime(liveMPQry.getMinPreStartTime());
        qry.setUid(liveMPQry.getCraftsmanUserId());

        qry.setSortType(SortType.TabSortType.SELF);

        List<Integer> liveType = Arrays.asList(LiveEnum.LiveType.ELSE_TYPE.getCode()
                , LiveEnum.LiveType.CRAFTS_TALK_TYPE.getCode()
                , LiveEnum.LiveType.TREASURE_TYPE.getCode()
                , LiveEnum.LiveType.CRAFTS_PURCHASE_TYPE.getCode()
                , LiveEnum.LiveType.PURE_LIVE.getCode()
                , LiveEnum.LiveType.OPEN_MATERIAL_TYPE.getCode());

        qry.setTypes(liveType);

        if (Objects.isNull(liveMPQry.getState())) {
            qry.setStates(Arrays.asList(LiveEnum.LiveState.UNSTART.getCode(), LiveEnum.LiveState.PLAYING.getCode()));
        } else {
            qry.setStates(Arrays.asList(liveMPQry.getState()));
        }
        qry.setStatus(Arrays.asList(0, 1, 2));

        String orderBy = liveMPQry.getOrderBy();
        if (Objects.nonNull(orderBy) && orderBy.contains("weight")) {
            List<cn.idongjia.se.lib.engine.query.sort.Sort> sorts = new ArrayList<>();
            if (orderBy.contains("desc")) {
                cn.idongjia.se.lib.engine.query.sort.Sort sort1 = cn.idongjia.se.lib.engine.query.sort.Sort.builder()
                        .field("generalWeight")
                        .direction(Direction.DESC)
                        .build();
                sorts.add(sort1);
            } else if (orderBy.contains("asc")) {
                cn.idongjia.se.lib.engine.query.sort.Sort sort1 = cn.idongjia.se.lib.engine.query.sort.Sort.builder()
                        .field("generalWeight")
                        .direction(Direction.ASC)
                        .build();
                sorts.add(sort1);
            }
            qry.setSorts(sorts);
        }


        if (flag == 1) {
            if (qry.getLimit() == null) {
                qry.setLimit(20);
            }
            qry.setOnline(LiveEnum.LiveOnline.ONLINE.getCode());
            qry.setStates(Arrays.asList(LiveEnum.LiveState.UNSTART.getCode(), LiveEnum.LiveState.PLAYING.getCode()));
            List<cn.idongjia.se.lib.engine.query.sort.Sort> sorts = new ArrayList<>();
            cn.idongjia.se.lib.engine.query.sort.Sort weightSort = cn.idongjia.se.lib.engine.query.sort.Sort.builder()
                    .field("generalWeight")
                    .direction(Direction.DESC)
                    .build();
            sorts.add(weightSort);
            cn.idongjia.se.lib.engine.query.sort.Sort stateSort = cn.idongjia.se.lib.engine.query.sort.Sort.builder()
                    .field("state")
                    .direction(Direction.DESC)
                    .build();
            sorts.add(stateSort);
            cn.idongjia.se.lib.engine.query.sort.Sort uvSort = cn.idongjia.se.lib.engine.query.sort.Sort.builder()
                    .field("uv")
                    .direction(Direction.DESC)
                    .build();
            sorts.add(uvSort);
            cn.idongjia.se.lib.engine.query.sort.Sort preStartTimeSort = cn.idongjia.se.lib.engine.query.sort.Sort.builder()
                    .field("preStartTime")
                    .direction(Direction.ASC)
                    .build();
            sorts.add(preStartTimeSort);
            qry.setSorts(sorts);
        }


        return qry;


    }

    public static LiveQry assembleGeneralListQry(LiveSearch liveSearch) {
        LiveQry qry = new LiveQry();
        qry.setLimit(liveSearch.getLimit());
        qry.setPage(liveSearch.getPage());
        if (Objects.nonNull(liveSearch.getId())) {
            qry.setLiveIds(Arrays.asList(liveSearch.getId()));
        }
        if(Objects.nonNull(liveSearch.getTitle())){
            qry.setWildTitle(String.format("*%s*",liveSearch.getTitle()));
        }
//        qry.setTitle(liveSearch.getTitle());
        qry.setUid(liveSearch.getUid());
        qry.setOnline(liveSearch.getOnline());
        if (Objects.nonNull(liveSearch.getState())) {
            qry.setStates(Arrays.asList(liveSearch.getState()));
        }
        qry.setMaxCreateTime(liveSearch.getCreateTimeEnd());
        qry.setMinCreateTime(liveSearch.getCreateTimeFrom());
        qry.setMaxPreStartTime(liveSearch.getPreStartTimeEnd());
        qry.setMinPreStartTime(liveSearch.getPreStartTimeFrom());
        qry.setMaxEndTime(liveSearch.getEndTimeEnd());
        qry.setMinEndTime(liveSearch.getEndTimeFrom());
        qry.setMaxPreEndTime(liveSearch.getPreEndTimeEnd());
        qry.setMinPreEndTime(liveSearch.getPreEndTimeFrom());
        qry.setMaxStartTime(liveSearch.getStartTimeEnd());
        qry.setMinStartTime(liveSearch.getStartTimeFrom());
        qry.setStatus(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL));

        qry.setSortType(SortType.TabSortType.SELF);
        List<cn.idongjia.se.lib.engine.query.sort.Sort> sorts=new ArrayList<>();
        cn.idongjia.se.lib.engine.query.sort.Sort sort=new cn.idongjia.se.lib.engine.query.sort.Sort();
        if(Objects.equals("1",liveSearch.getOrder())){
            sort.setDirection(Direction.DESC);
        }else{
            sort.setDirection(Direction.ASC);
        }
        if (Objects.nonNull(liveSearch.getOrderBy())) {
            switch (liveSearch.getOrderBy()) {
                case "weight":
                    sort.setField("generalWeight");
                    break;
                case "create_time":
                    sort.setField("createTime");
                    break;
                case "pre_start_time":
                    sort.setField("preStartTime");
                    break;
                case "start_time":
                    sort.setField("startTime");
                    break;
                case "end_time":
                    sort.setField("endTime");
                    break;
                default:
                    break;
            }
        }
        sorts.add(sort);

        qry.setSorts(sorts);

        return qry;
    }


}

package cn.idongjia.live.restructure.impl.tab;

import cn.idongjia.divine.lib.pojo.Conf;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.api.PageTabLiveServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.api.live.pojo.response.SingleResponse;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBPageTabLiveQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.biz.PageTabBO;
import cn.idongjia.live.restructure.convert.PageTabLiveConvertor;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.manager.OutcryManager;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabLiveAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabLiveDeleteCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabLiveUpdateWeightCmd;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabLiveCO;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveApiQry;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveQry;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.query.PageTabLiveQueryHandler;
import cn.idongjia.live.restructure.query.PageTabQueryHandler;
import cn.idongjia.live.restructure.query.QueryFactory;
import cn.idongjia.live.restructure.query.SearchQueryHandler;
import cn.idongjia.live.restructure.repo.PageTabLiveRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/8.
 */
@Service("pageTabLiveService")
public class PageTabLiveServiceImpl implements PageTabLiveServiceI {
    private static final Log LOGGER = LogFactory.getLog(PageTabLiveServiceImpl.class);

    @Resource
    private PageTabLiveConvertor pageTabLiveConvertor;


    @Resource
    private PageTabLiveRepo pageTabLiveRepo;

    @Resource
    private PageTabQueryHandler pageTabQueryHandler;

    @Resource
    private PageTabLiveQueryHandler pageTabLiveQueryHandler;

    @Resource
    private PageTabBO     pageTabBO;
    @Resource
    private OutcryManager outcryManager;

    @Resource
    private DivineSearchManager divineSearchManager;


    /**
     * 添加tab关联直播
     *
     * @param addCmd
     * @return
     */
    @Override
    public SingleResponse<Integer> add(PageTabLiveAddCmd addCmd) {
        if (CollectionUtils.isEmpty(addCmd.getLiveIds())) {
            throw LiveException.failure("关联的直播必填");
        }
        List<Long> paramLiveIds = new ArrayList<>();
        addCmd.getLiveIds().forEach(liveId -> {
            paramLiveIds.add(liveId);
        });

        Long tabId = addCmd.getTabId();

        PageTabE pageTabE = pageTabQueryHandler.get(tabId);
        //过滤添加过的直播
        DBPageTabLiveQuery query         = DBPageTabLiveQuery.builder().liveIds(addCmd.getLiveIds()).tabId(tabId).build();
        List<PageTabLiveE> pageTabLiveVS = pageTabLiveQueryHandler.list(query);

        List<Long> liveIds = pageTabLiveVS.stream().map(PageTabLiveE::getLiveId).collect(Collectors.toList());
        List<Long> ids     = new ArrayList<>();
        if (!CollectionUtils.isEmpty(liveIds)) {
            liveIds.forEach(liveId -> {
                ids.add(liveId);
            });
            paramLiveIds.removeAll(ids);
        }

        addCmd.setLiveIds(paramLiveIds);

        if (CollectionUtils.isEmpty(addCmd.getLiveIds())) {
            return SingleResponse.of(0);
        }
        int insertCount = pageTabE.batchAddLive(addCmd);
        return SingleResponse.of(insertCount);

    }

    /**
     * 添加tab关联直播（直播、直播拍）
     *
     * @param addCmd
     * @return
     */
    @Override
    public SingleResponse<Integer> addByAsidLiveId(PageTabLiveAddCmd addCmd) {

        if (CollectionUtils.isEmpty(addCmd.getAsids())) {
            return SingleResponse.of(0);
        }
        List<Long> asids = new ArrayList<>();
        addCmd.getAsids().forEach(asid -> {
            asids.add(asid);
        });
        List<Session4Live> auctionSessions = outcryManager.takeLiveIdByAsid(asids);
        List<Long>         newAsids        = auctionSessions.stream().map(Session4Live::getId).collect(Collectors.toList());
        List<Long>         deleteasids     = new ArrayList<>();
        if (CollectionUtils.isEmpty(newAsids)) {
            throw LiveException.failure("直播拍id" + asids + "不存在，请修改后提交");
        }
        newAsids.forEach(asid -> {
            deleteasids.add(asid);
        });
        asids.removeAll(deleteasids);
        if (!CollectionUtils.isEmpty(asids)) {

            throw LiveException.failure("直播拍id" + asids + "不存在，请修改后提交");
        }
        List<Long> liveIds = auctionSessions.stream().map(Session4Live::getLiveId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(liveIds)) {
            liveIds = addCmd.getLiveIds();
        } else if (!CollectionUtils.isEmpty(addCmd.getLiveIds())) {
            liveIds.addAll(addCmd.getLiveIds());
        }
        if (CollectionUtils.isEmpty(liveIds)) {
            return SingleResponse.of(0);
        }
        Set<Long> setLiveId = new HashSet<>(liveIds);

        addCmd.setLiveIds(new ArrayList<>(setLiveId));
        return add(addCmd);
    }

    /**
     * 更新tab关联直播权重
     *
     * @param updateWeightCmd
     * @return
     */
    @Override
    public SingleResponse<Integer> updateWeight(PageTabLiveUpdateWeightCmd updateWeightCmd) {
        PageTabLiveE pageTabLiveE = pageTabLiveQueryHandler.get(updateWeightCmd.getId());
        pageTabLiveE.setWeight(updateWeightCmd.getWeight());
        Integer updateCount = pageTabLiveE.edit();
        return SingleResponse.of(updateCount);
    }

    /**
     * 删除tab关联直播
     *
     * @param deleteCmd
     * @return
     */
    @Override
    public SingleResponse<Integer> delete(PageTabLiveDeleteCmd deleteCmd) {
        int deleteCount = pageTabLiveRepo.delete(deleteCmd.getId());
        return SingleResponse.of(deleteCount);
    }

    /**
     * tab关联直播查询
     *
     * @param pageTabLiveQry
     * @return
     */
    @Override
    public MultiResponse<PageTabLiveCO> page(PageTabLiveQry pageTabLiveQry) {
        Optional<DBPageTabLiveQuery> optionalDbPageTabLiveQuery = QueryFactory.getInstance(pageTabLiveQry);
        if (optionalDbPageTabLiveQuery.isPresent()) {
            DBPageTabLiveQuery dbPageTabLiveQuery = optionalDbPageTabLiveQuery.get();
            int                total              = pageTabLiveQueryHandler.count(dbPageTabLiveQuery);
            if (total > 0) {
                List<PageTabLiveE>       pageTabLiveVS  = pageTabLiveQueryHandler.list(dbPageTabLiveQuery);
                List<Long>               liveIds        = pageTabLiveVS.stream().map(PageTabLiveE::getLiveId).collect(Collectors.toList());
                List<GeneralLiveCO>      generalLiveCOS = divineSearchManager.liveList(liveIds);
                Map<Long, GeneralLiveCO> mapLiveShow    = generalLiveCOS.stream().collect(Collectors.toMap(GeneralLiveCO::getId, v1 -> v1, (v1, v2) -> v1));

                List<PageTabLiveCO>      pageTabLiveCOS;
                Map<Long, GeneralLiveCO> finalMapLiveShow = mapLiveShow;
                pageTabLiveCOS = pageTabLiveVS.stream().map(pageTabLiveV -> {
                    PageTabLiveCO pageTabLiveCO = pageTabLiveConvertor.entityToClient(pageTabLiveV);
                    GeneralLiveCO generalLiveCO = finalMapLiveShow.get(pageTabLiveV.getLiveId());
                    if (!Objects.isNull(generalLiveCO)) {
                        pageTabLiveCO.setLiveType(generalLiveCO.getType());
                        pageTabLiveCO.setLivePic(generalLiveCO.getPic());
                        pageTabLiveCO.setLiveStatus(generalLiveCO.getOnline());
                        pageTabLiveCO.setLiveState(generalLiveCO.getState());
                        pageTabLiveCO.setLiveTitle(generalLiveCO.getTitle());
                        if(!Objects.equals(Conf.defaultDate.longValue(),generalLiveCO.getSessionId().longValue())){
                            pageTabLiveCO.setSessionId(generalLiveCO.getSessionId());
                        }
                        return pageTabLiveCO;
                    } else {
                        return null;
                    }
                }).collect(Collectors.toList());
                List<PageTabLiveCO> pageTabLive = pageTabLiveCOS.stream().filter(x -> x != null).collect(Collectors.toList());
                return MultiResponse.of(pageTabLive, total);
            }
        }
        return MultiResponse.buildSuccess();
    }

    /**
     * api查询数据
     *
     * @param pageTabLiveApiQry
     * @return
     */
    @Override
    public MultiResponse<LiveCO> pageApi(PageTabLiveApiQry pageTabLiveApiQry) {
        return pageTabBO.pageTabLiveApi(pageTabLiveApiQry);
    }
}

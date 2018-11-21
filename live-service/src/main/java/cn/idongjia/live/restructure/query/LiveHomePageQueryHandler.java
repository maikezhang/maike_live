package cn.idongjia.live.restructure.query;

import cn.idongjia.common.context.DJContext;
import cn.idongjia.divine.lib.pojo.request.live.LiveQry;
import cn.idongjia.divine.lib.pojo.request.sort.SortType;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.api.live.pojo.LiveSearchApiResp;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.homePage.LiveBannerCO;
import cn.idongjia.live.pojo.homePage.LiveHomePageCO;
import cn.idongjia.live.pojo.homePage.LiveModuleCO;
import cn.idongjia.live.pojo.homePage.LiveTabCO;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.query.live.LiveBannerSearch;
import cn.idongjia.live.restructure.biz.PageTabBO;
import cn.idongjia.live.restructure.cache.liveHomePage.HPBannerCache;
import cn.idongjia.live.restructure.cache.liveHomePage.HPModuleCache;
import cn.idongjia.live.restructure.cache.liveHomePage.HPTabCache;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.convert.LiveTabConvertor;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import cn.idongjia.live.restructure.dto.LiveBannerDTO;
import cn.idongjia.live.restructure.dto.LiveModuleDTO;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveApiQry;
import cn.idongjia.live.restructure.pojo.query.PageTabQry;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component
public class LiveHomePageQueryHandler {

    private static final Log logger = LogFactory.getLog(LiveHomePageQueryHandler.class);


    @Resource
    private LiveModuleQueryHander liveModuleQueryHander;

    @Resource
    private LiveBannerQueryHandler liveBannerQueryHandler;

    @Resource
    private PageTabQueryHandler pageTabQueryHandler;

    @Resource
    private LiveTabConvertor liveTabConvertor;


    @Resource
    private ConfigManager configManager;

    @Resource
    private HPModuleCache hpModuleCache;

    @Resource
    private HPBannerCache hpBannerCache;

    @Resource
    private HPTabCache hpTabCache;


    @Resource
    private DivineSearchManager divineSearchManager;

    @Resource
    private PageTabBO pageTabBO;

    @Resource
    private ConvertorI<LiveSearchApiResp, LiveEntity, LiveCO> liveSearchApiRespConvert;


    public BaseList<LiveSearchApiResp> searchLiveHomePage(String query, Integer page, Integer limit) {
        if (query == null || query.length() == 0) {
            throw LiveException.failure("搜索内容不能为空");
        }
        if (query.length() > 30) {
            throw LiveException.failure("搜索词输入过长，请不要超过30个字");
        }

        Long                        uid                   = DJContext.getContext().getUid();
        BaseList<LiveSearchApiResp> searchApiRespBaseList = new BaseList<>();
        if (null == limit) {
            limit = 10;
        }
        LiveQry qry = new LiveQry();
        qry.setPage(page);
        qry.setLimit(limit);
        qry.setStates(Lists.newArrayList(LiveConst.STATE_LIVE_NOT_BEGIN, LiveConst.STATE_LIVE_IN_PROGRESS));
        qry.setStatus(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL));
        qry.setOnline(LiveConst.STATUS_LIVE_ONLINE);
        qry.setSortType(SortType.TabSortType.SEARCH);
        qry.setTitle(query);
        qry.setShowLocations(Arrays.asList(AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode()
                , AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_APP_TYPE.getCode()));
        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveSearch(qry);
        if (CollectionUtils.isEmpty(generalLiveCOS)) {
            return searchApiRespBaseList;
        }
        cn.idongjia.live.api.live.pojo.response.MultiResponse<LiveCO> liveData = pageTabBO.assembleLive(generalLiveCOS, uid);

        List<LiveCO> liveCOS = (List<LiveCO>) liveData.getData();
        if (CollectionUtils.isEmpty(liveCOS)) {
            return searchApiRespBaseList;
        }

        List<LiveSearchApiResp> collect = liveCOS.stream().map(liveCO -> {
            return liveSearchApiRespConvert.dataToClient(liveCO);
        }).collect(Collectors.toList());

        searchApiRespBaseList.setItems(collect);

        return searchApiRespBaseList;

    }


    /**
     * 组装直播首页基本数据
     *
     * @return
     */
    public LiveHomePageCO getHomePageBase(boolean isNewVersion) {

        LiveHomePageCO co = new LiveHomePageCO();
        //获取直播module
        List<LiveModule>           liveModules;
        Optional<List<LiveModule>> modulesCache = hpModuleCache.takeRedis();
        if (modulesCache.isPresent()) {
            liveModules = modulesCache.get();
        } else {
            liveModules = liveModuleQueryHander.getOnShelfModule();
            hpModuleCache.putRedis(liveModules);
        }
        if (!CollectionUtils.isEmpty(liveModules)) {
            List<LiveModule> liveModules1 = liveModules.stream().sorted(Comparator.comparing(LiveModule::getPosition)).collect(Collectors.toList());
            List<LiveModuleCO> liveModuleCOS = liveModules1.stream().map(liveModule -> {
                return LiveModuleDTO.po2LiveModuleCO(liveModule);
            }).collect(Collectors.toList());
            co.setModule(liveModuleCOS);
        }


        //获取直播banner
        Optional<List<LiveBannerCO>> liveBannerCOS1 = hpBannerCache.takeRedis(isNewVersion);
        if (liveBannerCOS1.isPresent()) {
            co.setBanner(liveBannerCOS1.get());
        } else {
            LiveBannerSearch search = new LiveBannerSearch();
            search.setStatus(LiveConst.STATUS_BANNER_NORMAL);
            search.setOrderBy("weight desc");
            List<LiveBannerDTO> liveBannerDTOS = liveBannerQueryHandler.getBanner(search);
            if (!CollectionUtils.isEmpty(liveBannerDTOS)) {
                List<LiveBannerCO> liveBannerCOS = liveBannerDTOS.stream().map(liveBannerDTO -> {
                    return liveBannerDTO.po2LiveBannerCO(liveBannerDTO.toDO(), isNewVersion);
                }).filter(x -> Objects.nonNull(x.getCover())).collect(Collectors.toList());
                co.setBanner(liveBannerCOS);
                hpBannerCache.putRedis(liveBannerCOS, isNewVersion);
            }
        }

        //获取直播tab
        List<LiveTabCO>           liveTabCOS  = new ArrayList<>();
        Optional<List<LiveTabCO>> liveTabCOS1 = hpTabCache.takeRedis();
        if (liveTabCOS1.isPresent()) {
            liveTabCOS = liveTabCOS1.get();
        } else {
            PageTabQry qry = new PageTabQry();
            qry.setStatus(LiveEnum.TabStatus.NORMAL_STATUS.getCode());
            qry.setOnline(BaseEnum.YesOrNo.YES.getCode());
            qry.setOrderBy("weight desc");
            Integer tabSize = configManager.getLiveHomePageTabSize();
            qry.setLimit(tabSize);
            List<PageTabE> pageTabES = pageTabQueryHandler.getTabForApi(qry);
            if (!CollectionUtils.isEmpty(pageTabES)) {
                liveTabCOS = pageTabES.stream().map(pageTabE -> {
                    return liveTabConvertor.entityToClient(pageTabE);
                }).collect(Collectors.toList());
                hpTabCache.putRedis(liveTabCOS);
            }
        }
        if (CollectionUtils.isEmpty(liveTabCOS)) {
            liveTabCOS = new ArrayList<>();
        }
        LiveTabCO liveTabResponse = new LiveTabCO();
        liveTabResponse.setId(0L);
        liveTabResponse.setTitle("推荐");
        liveTabCOS.add(0, liveTabResponse);

        PageTabLiveApiQry qry=new PageTabLiveApiQry();
        qry.setLimit(10);
        qry.setTabId(1L);
        qry.setPage(1);
        MultiResponse<LiveCO> liveCOMultiResponse = pageTabBO.elseLiveTab(qry);
        if(!CollectionUtils.isEmpty(liveCOMultiResponse.getData())){
            LiveTabCO elseTab = new LiveTabCO();
            elseTab.setId(1L);
            elseTab.setTitle("其他");
            liveTabCOS.add(elseTab);
        }
        co.setTab(liveTabCOS);

        return co;
    }


}

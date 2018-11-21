package cn.idongjia.live.restructure.biz;

import cn.idongjia.common.pagination.Pagination;
import cn.idongjia.common.query.BaseSearch;
import cn.idongjia.live.db.mybatis.po.LiveBannerPO;
import cn.idongjia.live.db.mybatis.query.DBLiveBannerQuery;
import cn.idongjia.live.db.mybatis.query.DBLivePureQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.pojo.live.LiveBanner;
import cn.idongjia.live.pojo.purelive.banner.PureLiveBannerDO;
import cn.idongjia.live.query.live.LiveBannerSearch;
import cn.idongjia.live.query.purelive.banner.PureLiveBannerSearch;
import cn.idongjia.live.restructure.cache.liveHomePage.HPBannerCache;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.LiveBannerDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.query.*;
import cn.idongjia.live.restructure.repo.LiveBannerRepo;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/13.
 */
@Component
public class LiveBannerBO {

    private static final Log LOGGER = LogFactory.getLog(LiveBannerBO.class);
    @Resource
    private LiveBannerQueryHandler liveBannerQueryHandler;

   @Resource
   private LiveBannerRepo liveBannerRepo;

   @Resource
   private HPBannerCache hpBannerCache;

    public static void handleBaseSearch(BaseSearch search) {
        if (null != search) {
            Long startTime = search.getStart();
            Long endTime = search.getEnd();
            if (null != startTime) {
                search.setStart(LiveBannerBO.getAvaliableTime(startTime));
            }
            if (null != endTime) {
                search.setEnd(LiveBannerBO.getAvaliableTime(endTime));
            }
            if(Utils.isEmpty(search.getOrderBy())){
                search.setOrderBy("weight desc");
            }
        }
    }

    private static Long getAvaliableTime(Long time) {
        if (time > 9999999999L) {
            time = time / 1000;
        }
        return time;
    }

    @Deprecated
    public Pagination getBannerByPage(LiveBannerSearch liveBannerSearch) {
        LiveBannerBO.handleBaseSearch(liveBannerSearch);
        DBLiveBannerQuery dbLiveBannerQuery = QueryFactory.getInstance(liveBannerSearch);
        Pagination pagination = liveBannerSearch.getPagination();
        List<LiveBannerDTO> liveBannerDTOS = null;
        try {
            Integer count = liveBannerQueryHandler.count(dbLiveBannerQuery);
            if (count > 0) {
                liveBannerDTOS = liveBannerQueryHandler.list(dbLiveBannerQuery).get();
//                List<Long> liveIds = liveBannerDTOS.stream().map(LiveBannerDTO::getLiveId).collect(Collectors.toList());
//                Map<Long, LiveShowDTO> liveShowDTOMap = liveShowQueryHandler.map(DBLiveShowQuery.builder().ids(liveIds).build()).get();
//                Map<Long, LivePureDTO> livePureDTOMap = livePureQueryHandler.map(DBLivePureQuery.builder().liveIds(liveIds).build()).get();
//
//                List<Long> userIds = liveShowDTOMap.values().stream().map(LiveShowDTO::getUserId).collect(Collectors.toList());
//                List<Long> zooIds = liveShowDTOMap.values().stream().map(LiveShowDTO::getZooId).collect(Collectors.toList());
//                Map<Long, CustomerVo> customerVoMap = userManager.takeBatchCustomer(userIds);
//                Map<Long, LiveZoo> liveZooMap = liveZooQueryHandler.map(zooIds).get();
//                List<PureLiveBannerDO> pureLiveBannerDOS = liveBannerDTOS.stream().map(b -> {
//                    LiveShowDTO liveShowDTO = liveShowDTOMap.get(b.getLiveId());
//                    return b.assemblePureLiveBannerDO(liveShowDTO, livePureDTOMap.get(b.getLiveId()), customerVoMap.get(liveShowDTO.getUserId()), liveZooMap.get(liveShowDTO.getZooId()));
//                }).collect(Collectors.toList());
                pagination.setList(liveBannerDTOS);
            }
        } catch (Exception e) {
            LiveBannerBO.LOGGER.error("查询banner失败{}", e);
            throw LiveException.failure("查询banner失败");
        }
        return pagination;
    }



    public int count(LiveBannerSearch liveBannerSearch) {
        LiveBannerBO.handleBaseSearch(liveBannerSearch);
        DBLiveBannerQuery dbLiveBannerQuery = QueryFactory.getInstance(liveBannerSearch);
        return liveBannerQueryHandler.count(dbLiveBannerQuery);
    }

    public Long addLiveBanner(LiveBanner liveBanner){
        LiveBannerDTO liveBannerDTO=new LiveBannerDTO(new LiveBannerPO());
        liveBanner.setStatus(LiveConst.STATUS_BANNER_NORMAL);
        liveBannerDTO.buildFromReq(liveBanner);
        Long id = liveBannerRepo.addBanner(liveBannerDTO);
        vanishBannerRedis();
        return id;
    }

    public void updateLiveBanner(Long lbid,LiveBanner liveBanner){
        liveBanner.setId(lbid);
        LiveBannerDTO liveBannerDTO=new LiveBannerDTO(new LiveBannerPO());
        liveBannerDTO.buildFromReq(liveBanner);
        liveBannerRepo.updateBanner(liveBannerDTO);
        vanishBannerRedis();

    }
    public boolean removeLiveBanner(Long lbid){
        boolean delete = liveBannerRepo.delete(lbid);
        vanishBannerRedis();
        return   delete;
    }

    public void vanishBannerRedis(){
        try {
            hpBannerCache.vanishRedis();
            LOGGER.info("删除直播首页缓存banner数据成功");
        }catch (Exception e){
            LOGGER.warn("删除直播首页缓存banner数据失败：{}",e);
        }
    }
}

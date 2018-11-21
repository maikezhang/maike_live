package cn.idongjia.live.restructure.manager;

import cn.idongjia.divine.lib.pojo.request.live.CraftsmanQry;
import cn.idongjia.divine.lib.pojo.request.live.LiveQry;
import cn.idongjia.divine.lib.pojo.response.MultiResponse;
import cn.idongjia.divine.lib.pojo.response.auction.SessionAuctionRel;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.divine.lib.pojo.response.live.special.LiveSpecialCraftsmanCO;
import cn.idongjia.divine.lib.service.AuctionService;
import cn.idongjia.divine.lib.service.LiveService;
import cn.idongjia.divine.lib.service.LiveSpecialCraftsmanService;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/8/20
 * Time: 下午5:53
 */
@Component
public class DivineSearchManager {


    @Resource
    private LiveService divineLiveService;

    @Resource
    private AuctionService divineAuctionService;

    @Resource
    private LiveSpecialCraftsmanService divineLiveSpecialCraftsmanService;

    private static final Log LOGGER= LogFactory.getLog(DivineSearchManager.class);


    public List<GeneralLiveCO> liveSearch(LiveQry qry){
        MultiResponse<GeneralLiveCO> search = divineLiveService.search(qry);
        List<GeneralLiveCO> result= (List<GeneralLiveCO>) search.getData();
        if(CollectionUtils.isEmpty(result)){
            LOGGER.info("获取 cn.idongjia.divine.lib.service.LiveService.search 数据为空,请求参数：{}",qry);
        }
        return result;
    }

    public GeneralLiveCO getById(Long liveId) {

        MultiResponse<GeneralLiveCO> list   = divineLiveService.list(Arrays.asList(liveId));
        List<GeneralLiveCO>          result = (List<GeneralLiveCO>) list.getData();
        if (CollectionUtils.isEmpty(result)) {
            LOGGER.info("获取 cn.idongjia.divine.lib.service.LiveService.list 数据为空,请求参数：{}", liveId);
            return null;
        }
        return result.get(0);
    }
    public MultiResponse<GeneralLiveCO> search(LiveQry qry){
        MultiResponse<GeneralLiveCO> search = divineLiveService.search(qry);
        return search;
    }



    public List<GeneralLiveCO> liveTab(LiveQry qry){
        MultiResponse<GeneralLiveCO> tab = divineLiveService.tab(qry);
        List<GeneralLiveCO> result= (List<GeneralLiveCO>) tab.getData();
        if(CollectionUtils.isEmpty(result)){
            LOGGER.info("获取 cn.idongjia.divine.lib.service.LiveService.tab 数据为空,请求参数：{}",qry);
        }
        return result;
    }


    public List<GeneralLiveCO> liveRecommend(LiveQry qry){
        MultiResponse<GeneralLiveCO> recommend = divineLiveService.recommend(qry);
        List<GeneralLiveCO> result= (List<GeneralLiveCO>) recommend.getData();
        if(CollectionUtils.isEmpty(result)){
            LOGGER.info("获取 cn.idongjia.divine.lib.service.LiveService.recommend 数据为空,请求参数：{}",qry);
        }
        return result;

    }


    public List<GeneralLiveCO> liveList(List<Long> ids){
        MultiResponse<GeneralLiveCO> list = divineLiveService.list(ids);
        List<GeneralLiveCO> result= (List<GeneralLiveCO>) list.getData();
        if(CollectionUtils.isEmpty(result)){
            LOGGER.info("获取 cn.idongjia.divine.lib.service.LiveService.list 数据为空,请求参数：{}",ids);
        }
        return result;
    }

    public MultiResponse<SessionAuctionRel> auctionSearch(List<Long> sessionIds){
        MultiResponse<SessionAuctionRel> sessionAuctionRelMultiResponse = divineAuctionService.groupBySession(sessionIds);
        return sessionAuctionRelMultiResponse;
    }

    public Map<Long,SessionAuctionRel> mapSessionAuction(List<Long> sessionIds){
        MultiResponse<SessionAuctionRel> sessionAuctionRelMultiResponse = divineAuctionService.groupBySession(sessionIds);
        List<SessionAuctionRel> sessionAuctionRels = (List<SessionAuctionRel>)sessionAuctionRelMultiResponse.getData();
        if(CollectionUtils.isEmpty(sessionAuctionRels)){
            LOGGER.info("cn.idongjia.divine.lib.service.AuctionService.groupBySession 数据为空,请求参数：{}",sessionIds);
            return new HashMap<>();
        }

        return sessionAuctionRels.stream().collect(Collectors.toMap(SessionAuctionRel::getSessionId,sessionAuctionRel -> sessionAuctionRel));


    }

    public MultiResponse<LiveSpecialCraftsmanCO> getLiveAnchorBlackWhite(CraftsmanQry craftsmanQry){

        MultiResponse<LiveSpecialCraftsmanCO> list = divineLiveSpecialCraftsmanService.list(craftsmanQry);
        List<LiveSpecialCraftsmanCO> data = (List<LiveSpecialCraftsmanCO>) list.getData();
        if(CollectionUtils.isEmpty(data)){
            LOGGER.info("cn.idongjia.divine.lib.service.LiveSpecialCraftsmanService.list 数据为空,请求参数：{}",craftsmanQry);
            return MultiResponse.buildSuccess();
        }
        return list;


    }




}

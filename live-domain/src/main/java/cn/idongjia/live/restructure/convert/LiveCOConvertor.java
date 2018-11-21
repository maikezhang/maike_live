package cn.idongjia.live.restructure.convert;

import cn.idongjia.divine.lib.pojo.response.auction.AuctionItemCO;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.divine.lib.pojo.response.live.general.ItemCO;
import cn.idongjia.live.api.live.pojo.LiveItemResp;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.LiveForTabDTO;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.pojo.co.live.LiveResourceCO;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.pojo.co.tab.LivePullUrlCO;
import cn.idongjia.user.lib.entity.Craftsman;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Component("liveCOConvertor")
public class LiveCOConvertor implements ConvertorI<LiveCO, LiveEntity, LiveForTabDTO> {

    @Override
    public LiveCO dataToClient(LiveForTabDTO liveForTabDTO) {
        int bookState = liveForTabDTO.getBookState();
        CustomerVo customerVo = liveForTabDTO.getCustomerVo();
        List<LiveItemResp> liveItemResps = liveForTabDTO.getLiveItemResps();
        LivePullUrlDTO livePullUrlDTO = liveForTabDTO.getLivePullUrlDTO();
        SearchIndexRespDTO searchIndexRespDTO = liveForTabDTO.getSearchIndexRespDTO();
        LiveZoo liveZoo = liveForTabDTO.getLiveZoo();
        LiveCO liveCO = new LiveCO();
        liveCO.setId(searchIndexRespDTO.getId());
        liveCO.setBookState(bookState);
        liveCO.setCover(searchIndexRespDTO.getPic());
        if (customerVo != null) {
            liveCO.setCraftsmanAvatar(customerVo.getAvatar());
            liveCO.setCraftsmanName(customerVo.getName());
            Craftsman craftsman = customerVo.getCraftsman();
            if (null != craftsman) {
                liveCO.setCraftsmanCity(craftsman.getCity());
                liveCO.setCraftsmanTitle(craftsman.getTitle());
            }

            liveCO.setCraftsmanUserId(customerVo.getId());
        }

        VideoCoverDTO videoCoverDTO = liveForTabDTO.getVideoCoverDTO();
        if (null != videoCoverDTO) {
            liveCO.setVideoCoverUrl(videoCoverDTO.getUrl());

        }
        if (!Utils.isEmpty(liveItemResps)) {
            List<LiveResourceCO> liveResourceCOS = liveItemResps.stream().map(liveItemResp -> {
                LiveResourceCO liveResourceCO = new LiveResourceCO();
                liveResourceCO.setCover(liveItemResp.getPic());
                liveResourceCO.setId(liveItemResp.getId());
                liveResourceCO.setPrice(liveItemResp.getPrice());
                liveResourceCO.setState(liveItemResp.getState());
                return liveResourceCO;
            }).collect(Collectors.toList());
            liveCO.setResources(liveResourceCOS);
        }

        liveCO.setHot(new Long(liveZoo.getZooCount().getHot()));
        liveCO.setPreStartTime(searchIndexRespDTO.getPreStartTime());
        LivePullUrlCO pullUrlCO = null;
        if (null != livePullUrlDTO && Objects.equals(searchIndexRespDTO.getLiveState(), LiveEnum.LiveState.PLAYING.getCode())) {
            pullUrlCO = new LivePullUrlCO();
            pullUrlCO.setFlvUrl(livePullUrlDTO.getFlvUrl());
            pullUrlCO.setHlsUrl(livePullUrlDTO.getHlsUrl());
            pullUrlCO.setRtmpUrl(livePullUrlDTO.getRtmpUrl());
        }

        liveCO.setPullURL(pullUrlCO);
        liveCO.setSessionId(searchIndexRespDTO.getAsid());
        liveCO.setState(searchIndexRespDTO.getLiveState());
        liveCO.setTitle(searchIndexRespDTO.getTitle());
        liveCO.setType(searchIndexRespDTO.getLiveType());
        return liveCO;
    }

    public static LiveCO searchDataToClient(LiveForTabDTO liveForTabDTO) {
        LiveCO co = new LiveCO();
        GeneralLiveCO generalLiveCO = liveForTabDTO.getGeneralLiveCO();

        int bookStatus = liveForTabDTO.getBookState();

        LivePullUrlDTO livePullUrlDTO = liveForTabDTO.getLivePullUrlDTO();

        co.setId(generalLiveCO.getId());
        if (Objects.nonNull(generalLiveCO.getUv())) {
            co.setHot(Long.parseLong(generalLiveCO.getUv().toString()));
        } else {
            co.setHot(0L);
        }
        co.setBookState(bookStatus);
        co.setCover(generalLiveCO.getPic());
        co.setCraftsmanAvatar(generalLiveCO.getAvatar());
        co.setCraftsmanCity(generalLiveCO.getCraftsmanCity());
        co.setCraftsmanName(generalLiveCO.getCraftsmanName());
        co.setCraftsmanTitle(generalLiveCO.getCraftsmanTitle());
        co.setCraftsmanUserId(generalLiveCO.getUid());
        co.setPreStartTime(generalLiveCO.getPreStartTime());

        if (Objects.nonNull(livePullUrlDTO) && Objects.equals(generalLiveCO.getState().intValue(), LiveEnum.LiveState.PLAYING.getCode())) {
            LivePullUrlCO livePullUrlCO = new LivePullUrlCO();
            livePullUrlCO.setFlvUrl(livePullUrlDTO.getFlvUrl());
            livePullUrlCO.setHlsUrl(livePullUrlDTO.getHlsUrl());
            livePullUrlCO.setRtmpUrl(livePullUrlDTO.getRtmpUrl());
            co.setPullURL(livePullUrlCO);

        }

        co.setSessionId(generalLiveCO.getSessionId());
        co.setState(generalLiveCO.getState());
        co.setTitle(generalLiveCO.getTitle());
        co.setType(generalLiveCO.getType());
        co.setVideoCoverUrl(generalLiveCO.getVideoCoverUrl());

        if (Objects.equals(generalLiveCO.getType().intValue(), LiveEnum.LiveType.LIVE_AUCTION.getCode())) {
            List<AuctionItemCO> liveAuctionItems = liveForTabDTO.getLiveAuctionItems();
            if (!CollectionUtils.isEmpty(liveAuctionItems)) {
                List<LiveResourceCO> collect = liveAuctionItems.stream().map(liveAuctionItem -> {
                    LiveResourceCO liveResourceCO = new LiveResourceCO();
                    liveResourceCO.setCover(liveAuctionItem.getCover());
                    liveResourceCO.setId(liveAuctionItem.getItemId());
                    if (Objects.nonNull(liveAuctionItem.getCurrentPrice())) {
                        liveResourceCO.setPrice(liveAuctionItem.getCurrentPrice() == null ? 0 : liveAuctionItem.getCurrentPrice());
                    } else {
                        liveResourceCO.setPrice(liveAuctionItem.getPrice() == null ? 0 : liveAuctionItem.getPrice());
                    }

                    liveResourceCO.setState(liveAuctionItem.getState());
                    return liveResourceCO;
                }).collect(Collectors.toList());
                co.setResources(collect);
            }
        } else {
            List<ItemCO> items = generalLiveCO.getItems();
            if (!CollectionUtils.isEmpty(items)) {
                List<LiveResourceCO> collect = items.stream().map(itemCO -> {
                    LiveResourceCO liveResourceCO = new LiveResourceCO();
                    liveResourceCO.setCover(itemCO.getPicture());
                    liveResourceCO.setId(itemCO.getItemId());
                    liveResourceCO.setPrice(itemCO.getPrice());
                    return liveResourceCO;
                }).collect(Collectors.toList());
                co.setResources(collect);
            }
        }


        return co;
    }
}

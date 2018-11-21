package cn.idongjia.live.restructure.manager;

import cn.idongjia.essearch.lib.dto.EsResultDTO;
import cn.idongjia.essearch.lib.dto.live.LiveDTO;
import cn.idongjia.essearch.lib.query.live.LiveEsSearch;
import cn.idongjia.essearch.lib.service.live.LiveSearchService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lc
 * @create at 2018/7/13.
 */
@Component
public class ESSearchManager {
    @Resource
    private LiveSearchService esLiveSearchService;

    /**
     * 查询
     *
     * @param liveEsSearch
     * @return
     */
    public EsResultDTO<List<LiveDTO>> serarch(LiveEsSearch liveEsSearch) {
        return esLiveSearchService.search(liveEsSearch);
    }


    /**
     * 查询
     *
     * @param liveEsSearch
     * @return
     */
    public EsResultDTO<List<LiveDTO>> tabSearch(LiveEsSearch liveEsSearch) {
        return esLiveSearchService.tabSearch(liveEsSearch);
    }
    /**
     * 查询
     *
     * @param liveEsSearch
     * @return
     */
    public EsResultDTO<List<LiveDTO>> recommend(LiveEsSearch liveEsSearch) {
        return esLiveSearchService.getRecommend(liveEsSearch);

    }

}

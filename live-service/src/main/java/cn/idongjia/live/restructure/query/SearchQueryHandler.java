package cn.idongjia.live.restructure.query;

import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.manager.SearchManager;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/12.
 */
@Component
public class SearchQueryHandler {

    @Resource
    private SearchManager searchManager;

    public BaseList<LiveIndexResp> list(LiveQuery liveQuery) {
        BaseList<SearchIndexRespDTO> searchIndexRespDTOBaseList = searchManager.takeLiveFromIndex(liveQuery);
        List<SearchIndexRespDTO> items = searchIndexRespDTOBaseList.getItems();
        BaseList baseList = new BaseList();
        baseList.setCount(Long.valueOf(searchIndexRespDTOBaseList.getCount()).intValue());
        if (!Utils.isEmpty(items)) {
            List<LiveIndexResp> indexResps = items.stream().map(liveIndexResp -> {
                return liveIndexResp.assembleLiveIndexResp();
            }).collect(Collectors.toList());
            baseList.setItems(indexResps);
        } else {
            baseList.setItems(new ArrayList());
        }
        return baseList;
    }

    public List<SearchIndexRespDTO> list(List<Long> liveIds) {
        LiveQuery liveQuery = new LiveQuery();
        liveQuery.setLiveIds(liveIds);
        liveQuery.setNum(liveIds.size());
        BaseList<SearchIndexRespDTO> searchIndexRespDTOBaseList = searchManager.takeLiveFromIndex(liveQuery);
        return searchIndexRespDTOBaseList.getItems();
    }


    public Map<Long, SearchIndexRespDTO> map(List<Long> liveIds) {
        if(Utils.isEmpty(liveIds)){
            return new HashMap<>();
        }
        LiveQuery liveQuery = new LiveQuery();
        liveQuery.setLiveIds(liveIds);
        liveQuery.setNum(liveIds.size());
        BaseList<SearchIndexRespDTO> searchIndexRespDTOBaseList = searchManager.takeLiveFromIndex(liveQuery);
        List<SearchIndexRespDTO> items = searchIndexRespDTOBaseList.getItems();
        if (Utils.isEmpty(items)) {
            return new HashMap<>();
        }
        Map<Long, SearchIndexRespDTO> searchIndexRespDTOMap = items.stream().collect(Collectors.toMap
                (SearchIndexRespDTO::getId, v1
                -> v1, (v1, v2) -> v1));
        return searchIndexRespDTOMap;
    }
}

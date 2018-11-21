package cn.idongjia.live.restructure.manager;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.search.api.follow.IndexFollowService;
import cn.idongjia.search.api.live.LiveSearchService;
import cn.idongjia.search.pojo.live.LiveIndexResp;
import cn.idongjia.search.pojo.query.follow.IndexFollowQuery;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 索引查询
 *
 * @author lc
 * @create at 2017/12/19.
 */
@Component
public class SearchManager {

    private static final Log LOGGER = LogFactory.getLog(SearchManager.class);
    @Autowired
    private LiveSearchService liveSearchService;
    @Autowired
    private IndexFollowService indexFollowService;

    /**
     * 从索引查询直播数据
     *
     * @param liveQuery 查询条件
     * @return 直播列表
     */
    public BaseList<SearchIndexRespDTO> takeLiveFromIndex(LiveQuery liveQuery) {
        liveQuery.setStatusList(Arrays.asList(0,1,2));
        LOGGER.info("查询条件{}", liveQuery);
        BaseList<SearchIndexRespDTO> liveIndexRespBaseList = new BaseList<>();
        try {
            cn.idongjia.search.pojo.BaseList<LiveIndexResp> search = liveSearchService.search(liveQuery);
            if (search != null) {
                liveIndexRespBaseList.setCount(Long.valueOf(search.getCount()).intValue());
                List<LiveIndexResp> items = search.getItems();
                if (!Utils.isEmpty(items)) {
                    List<SearchIndexRespDTO> searchIndexRespDTOS = items.stream().map(SearchIndexRespDTO::new).collect(Collectors.toList());
                    liveIndexRespBaseList.setItems(searchIndexRespDTOS);
                }else{
                    liveIndexRespBaseList.setItems(new ArrayList<>());
                }

            }
        } catch (Exception e) {
            LOGGER.warn("调用搜索服务失败{}", e);
            throw new LiveException(-12138, "搜索服务失败");
        }
        return liveIndexRespBaseList;
    }

    public void followIndex(Long uid, Long liveId) {
        //dubbo async=true return=false
        LOGGER.info("cn.idongjia.search.api.follow.IndexFollowService.insert userId={} liveId={}", uid, liveId);
        indexFollowService.insert(uid, liveId, IndexFollowQuery.ContentType.LIVE.get(), System.currentTimeMillis());
    }


}

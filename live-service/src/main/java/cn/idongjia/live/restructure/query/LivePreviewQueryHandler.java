package cn.idongjia.live.restructure.query;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.common.query.BaseSearch;
import cn.idongjia.live.api.live.pojo.PreLiveResp;
import cn.idongjia.live.db.mybatis.query.DBLivePureQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.purelive.ForeShow;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.ForeShowDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.search.pojo.query.Sort;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component
public class LivePreviewQueryHandler {

    private static final Log           logger = LogFactory.getLog(LivePreviewQueryHandler.class);
    @Resource
    private              ConfigManager configManager;


    @Resource
    private ConvertorI<ForeShow, LiveEntity, ForeShowDTO> foreShowConvertor;

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;


    @Resource
    private LiveIndexQueryHandler liveIndexQueryHandler;

    @Resource
    private LivePureQueryHandler livePureQueryHandler;

    @Resource
    private UserManager userManager;

    /**
     * 这儿有bug 谁改谁负责
     * @param uid
     * @param lastDate
     * @return
     */
    public List<Map<String, Object>> getForeShows(Long uid, String lastDate) {
        List<Map<String, Object>> reList = new ArrayList<>();
        //获取配置拉取天数
        Integer          limit             = configManager.getForeShowDays();
        Long             curMillis         = Utils.getCurrentMillis();
        Long             durationMillis    = TimeUnit.SECONDS.toMillis(LiveConst.ONE_DAY_SECONDS * (limit - 1));
        Long             maxDurationMillis = TimeUnit.SECONDS.toMillis(LiveConst.ONE_DAY_SECONDS * configManager.getMaxForeShowDays() - 1);
        Long             startMillis       = curMillis;
        SimpleDateFormat sdf               = new SimpleDateFormat("yyyy-MM-dd");
        Long             aDayMillis        = TimeUnit.SECONDS.toMillis(LiveConst.ONE_DAY_SECONDS);
        try {
            Long dayMillis  = sdf.parse(sdf.format(new Date())).getTime();
            Long diffMillis = dayMillis - curMillis + aDayMillis;
            //结束时间=当前时间+时间间隔+结束当天24点时间差
            Long endMillis = startMillis + durationMillis + diffMillis;
            //获取拉取最大时间点
            Timestamp maxTime = new Timestamp(startMillis + maxDurationMillis + diffMillis);
            if (lastDate != null && !"".equals(lastDate)) {
                try {
                    startMillis = sdf.parse(lastDate).getTime() + aDayMillis;
                    endMillis = startMillis + aDayMillis * limit;
                } catch (ParseException e) {
                    logger.warn("拉取时间异常" + e.getMessage());
                    throw new LiveException(-12138, "拉取时间获取错误");
                }
            }
            Timestamp startTime   = new Timestamp(startMillis);
            Timestamp endTime     = new Timestamp(endMillis);
            int       foreShowNum = 0;
            while (!startTime.after(maxTime) && !endTime.after(maxTime)) {
                logger.info("获取预告开始时间: " + startTime + "---获取预告结束时间: " + endTime);
                List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(DBLiveShowQuery.builder().minStartTime(startTime).maxEndTime(endTime).build()).get();
                if (!Utils.isEmpty(liveShowDTOS)) {
                    //将纯直播组装为预告数据
                    List<Long> userIds = liveShowDTOS.stream().map(LiveShowDTO::getUserId).collect(Collectors.toList());
                    List<Long> liveIds = liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());

                    Map<Long, User>        userMap        = userManager.takeBatchUser(userIds);
                    Map<Long, LivePureDTO> livePureDTOMap = livePureQueryHandler.map(DBLivePureQuery.builder().liveIds(liveIds).build()).get();
                    List<ForeShow> foreShows = liveShowDTOS.stream().map(liveShowDTO -> {
                        ForeShowDTO foreShowDTO = new ForeShowDTO();
                        foreShowDTO.setBook(false);
                        LivePureDTO livePureDTO = livePureDTOMap.get(liveShowDTO.getId());
                        foreShowDTO.setLivePureDTO(livePureDTO);
                        foreShowDTO.setLiveShowDTO(liveShowDTO);
                        User user = userMap.get(liveShowDTO.getUserId());
                        foreShowDTO.setUser(user);
                        return foreShowConvertor.dataToClient(foreShowDTO);
                    }).collect(Collectors.toList());
                    Map<String, Map<String, List<ForeShow>>> dateMap = new TreeMap<>(Comparator.naturalOrder());
                    //按日期和时间分组
                    for (ForeShow f : foreShows) {
                        Map<String, List<ForeShow>> map = dateMap.get(f.getStartDate());
                        if (map == null) {
                            dateMap.put(f.getStartDate(), new TreeMap<>());
                            map = dateMap.get(f.getStartDate());
                        }
                        List<ForeShow> list = map.get(f.getStartTime());
                        if (list == null) {
                            map.put(f.getStartTime(), new ArrayList<>());
                            list = map.get(f.getStartTime());
                        }
                        list.add(f);
                        foreShowNum++;
                    }
                    //按需求组装数据
                    for (Map.Entry<String, Map<String, List<ForeShow>>> entry : dateMap.entrySet()) {
                        List<Map<String, Object>> dateList = new ArrayList<>();
                        for (Map.Entry<String, List<ForeShow>> timeEntry : entry.getValue().entrySet()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", timeEntry.getKey());
                            map.put("data", timeEntry.getValue());
                            dateList.add(map);
                        }
                        Map<String, Object> map        = new HashMap<>();
                        String              curDate    = sdf.format(new Date(Utils.getCurrentMillis()));
                        String              dateString = entry.getKey();
                        if (curDate.equals(entry.getKey())) {
                            dateString = "今天";
                        }
                        map.put("title", dateString);
                        map.put("data", dateList);
                        reList.add(map);
                    }
                    if (foreShowNum > configManager.getMaxForeShowLimit()) {
                        return reList;
                    }
                }
                startTime = new Timestamp(endTime.getTime());
                endTime = new Timestamp(endTime.getTime() + aDayMillis * 2);
            }
            return reList;
        } catch (Exception e) {
            logger.error("获取纯直播预告异常" + e.getMessage());

        }
        return reList;
    }

    public List<PreLiveResp> getPreLiveList(Integer page) {
        LiveQuery liveQuery = new LiveQuery();
        liveQuery.setNum(10);
        BaseSearch baseSearch = new BaseSearch();
        baseSearch.setLimit(10);
        baseSearch.setPage(page);
        Integer offset = baseSearch.getOffset();
        liveQuery.setStart(offset);
        liveQuery.setStateList(Collections.singletonList(LiveEnum.LiveState.UNSTART.getCode()));
        List<Sort> sorts = new ArrayList<>();
        sorts.add(new Sort("pre_start_time", SolrQuery.ORDER.desc));
        liveQuery.setSortList(sorts);
        List<SearchIndexRespDTO> searchIndexRespDTOS = liveIndexQueryHandler.listFromIndex(page, 10, null);
        List<Long>               uids                = searchIndexRespDTOS.stream().map(SearchIndexRespDTO::getUserId).collect(Collectors.toList());
        Map<Long, CustomerVo>    customerVoMap       = userManager.takeBatchCustomer(uids);

        return searchIndexRespDTOS.stream().map(searchIndexRespDTO -> {
            return searchIndexRespDTO.assembePreLiveItem(customerVoMap.get(searchIndexRespDTO.getUserId()));
        }).collect(Collectors.toList());
    }

}

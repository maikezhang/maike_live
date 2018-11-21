package cn.idongjia.live.restructure.manager;

import cn.idongjia.article.lib.service.HomepageLiveService;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/20
 * Time: 上午11:34
 */
@Component
public class ArticleManager {

    @Resource
    private HomepageLiveService articleHomepageLiveService;


    private static final Log LOGGER= LogFactory.getLog(ArticleManager.class);

    public Map<Long,Boolean> getLiveTopList(List<Long> liveIds){

        if(CollectionUtils.isEmpty(liveIds)){
            return new HashMap<>();
        }
        List<Long> topLiveLists=new ArrayList<>();
        try {
            topLiveLists = articleHomepageLiveService.searchTopList(liveIds);

        }catch (Exception e){
            LOGGER.warn("调用 cn.idongjia.article.lib.service.HomepageLiveService#searchTopList 失败,参数：{}，exception:{}",liveIds,e);
            return new HashMap<>();
        }


        return topLiveLists.stream().distinct().collect(Collectors.toMap(v1->v1,v1->true));


    }

}

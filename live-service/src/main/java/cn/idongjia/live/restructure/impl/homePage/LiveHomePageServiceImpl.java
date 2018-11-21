package cn.idongjia.live.restructure.impl.homePage;

import cn.idongjia.live.api.homePage.LiveHomePageServiceI;
import cn.idongjia.live.api.live.pojo.LiveSearchApiResp;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.homePage.LiveHomePageCO;
import cn.idongjia.live.restructure.query.LiveHomePageQueryHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/20
 * Time: 上午10:55
 */
@Component("liveHomePageServiceI")
public class LiveHomePageServiceImpl implements LiveHomePageServiceI {


    @Resource
    private LiveHomePageQueryHandler liveHomePageQueryHandler;

    @Override
    public LiveHomePageCO getHomePageBase(boolean isNewVersion) {
        return liveHomePageQueryHandler.getHomePageBase(isNewVersion);
    }

    @Override
    public BaseList<LiveSearchApiResp> searchLive(String queryStr, Integer page, Integer limit) {
        return liveHomePageQueryHandler.searchLiveHomePage(queryStr, page, limit);
    }
}

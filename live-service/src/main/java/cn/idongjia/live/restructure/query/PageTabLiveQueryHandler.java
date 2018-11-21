package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBPageTabLiveQuery;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.live.restructure.repo.PageTabLiveRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 页面tab查询
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Component
public class PageTabLiveQueryHandler {

    @Resource
    private PageTabLiveRepo pageTabLiveRepo;

    /**
     * 页面tab 关联直播查询
     *
     * @param dbPageTabLiveQuery 查询条件
     * @return List<PageTabE>
     */
    public List<PageTabLiveE> list(DBPageTabLiveQuery dbPageTabLiveQuery) {
        return pageTabLiveRepo.list(dbPageTabLiveQuery);

    }

    public PageTabLiveE get(long id) {
        return pageTabLiveRepo.get(id);
    }

    public int count(DBPageTabLiveQuery dbPageTabLiveQuery) {
        return pageTabLiveRepo.count(dbPageTabLiveQuery);
    }


    /**
     * 提供按照直播标题模糊查询
     * @return
     */
    public List<PageTabLiveE> getPageTabLives(DBPageTabLiveQuery dbPageTabLiveQuery){
        return pageTabLiveRepo.getPageTabLives(dbPageTabLiveQuery);
    }
}

package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBPageTabQuery;
import cn.idongjia.live.restructure.convert.PageTabConvertor;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabCO;
import cn.idongjia.live.restructure.pojo.query.PageTabQry;
import cn.idongjia.live.restructure.repo.PageTabRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 页面tab查询
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Component
public class PageTabQueryHandler {

    @Resource
    private PageTabRepo pageTabRepo;


    /**
     * 页面tab查询
     *
     * @param dbPageTabQuery 查询条件
     * @return List<PageTabE>
     */
    public List<PageTabE> list(DBPageTabQuery dbPageTabQuery) {
        return pageTabRepo.list(dbPageTabQuery);

    }

    public PageTabE get(long id) {
        return pageTabRepo.get(id);
    }

    public int count(DBPageTabQuery dbPageTabQuery) {
        return pageTabRepo.count(dbPageTabQuery);
    }

    public List<PageTabE> getTabForApi(PageTabQry pageTabQry) {
        List<PageTabCO> pageTabCOS=new ArrayList<>();
        DBPageTabQuery dbPageTabQuery = QueryFactory.getInstance(pageTabQry);
        List<PageTabE> pageTabES = list(dbPageTabQuery);
        return pageTabES;
    }
}

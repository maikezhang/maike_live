package cn.idongjia.live.restructure.impl.tab;

import cn.idongjia.live.api.PageTabServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.api.live.pojo.response.SingleResponse;
import cn.idongjia.live.db.mybatis.query.DBPageTabQuery;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabUpdateCmd;
import cn.idongjia.live.restructure.convert.PageTabConvertor;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabCO;
import cn.idongjia.live.restructure.pojo.query.PageTabQry;
import cn.idongjia.live.restructure.query.PageTabQueryHandler;
import cn.idongjia.live.restructure.query.QueryFactory;
import cn.idongjia.live.support.BaseEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 页面tab数据管理
 *
 * @author lc
 * @create at 2018/7/7.
 */
@Service("pageTabService")
public class PageTabServiceImpl implements PageTabServiceI {
    @Resource
    private PageTabQueryHandler pageTabQueryHandler;

    @Resource
    private PageTabConvertor pageTabConvertor;

    /**
     * 添加分页tab
     *
     * @param pageTabAddCmd
     * @return 添加id
     */
    @Override
    public SingleResponse<Long> add(PageTabAddCmd pageTabAddCmd) {
        PageTabE pageTabE = pageTabConvertor.clientToEntity(pageTabAddCmd.getPageTabCO());
        pageTabE.save();
        return SingleResponse.of(pageTabE.getId());
    }

    /**
     * 修改分页tab
     *
     * @param pageTabUpdateCmd
     * @return 添加id
     */
    @Override
    public SingleResponse<Integer> update(PageTabUpdateCmd pageTabUpdateCmd) {
        PageTabE pageTabE = pageTabConvertor.clientToEntity(pageTabUpdateCmd.getPageTabCO());
        Integer updateCount = pageTabE.update();

        return SingleResponse.of(updateCount);
    }

    /**
     * 删除分页
     *
     * @param pageTabDelCmd
     * @return 添加id
     */
    @Override
    public SingleResponse delete(PageTabDelCmd pageTabDelCmd) {
        long id = pageTabDelCmd.getId();
        PageTabE pageTabE = pageTabQueryHandler.get(id);
        int deleteCount = pageTabE.delete();
        return SingleResponse.of(deleteCount);
    }

    /**
     * 获取tab详情
     *
     * @param id
     * @return
     */
    @Override
    public SingleResponse<PageTabCO> get(long id) {
        PageTabE pageTabE = pageTabQueryHandler.get(id);
        if(pageTabE==null){
            return SingleResponse.buildFailure(110,"获取数据失败");
        }
        PageTabCO pageTabCO = pageTabConvertor.entityToClient(pageTabE);
        return SingleResponse.of(pageTabCO);
    }

    /**
     * 获取分页数据
     * @param pageTabQry
     * @return
     */
    @Override
    public MultiResponse<PageTabCO> page(PageTabQry pageTabQry) {
        DBPageTabQuery dbPageTabQuery = QueryFactory.getInstance(pageTabQry);
        int total = pageTabQueryHandler.count(dbPageTabQuery);
        List<PageTabCO> pageTabCOS=new ArrayList<>();
        if (total > 0) {
            List<PageTabE> pageTabES = pageTabQueryHandler.list(dbPageTabQuery);
            pageTabCOS = pageTabES.stream().map(pageTabE -> {
                return pageTabConvertor.entityToClient(pageTabE);
            }).collect(Collectors.toList());
        }
        return MultiResponse.of(pageTabCOS, total);
    }

    @Override
    public List<PageTabCO> getTabForApi(PageTabQry pageTabQry) {
        List<PageTabE> pageTabES = pageTabQueryHandler.getTabForApi(pageTabQry);


        return pageTabES.stream().map(pageTabE -> {
            return pageTabConvertor.entityToClient(pageTabE);
        }).collect(Collectors.toList());
    }
}

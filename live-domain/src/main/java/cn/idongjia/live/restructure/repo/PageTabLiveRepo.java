package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.PageTabLiveMapper;
import cn.idongjia.live.db.mybatis.po.PageTabLivePO;
import cn.idongjia.live.db.mybatis.query.DBPageTabLiveQuery;
import cn.idongjia.live.restructure.convert.PageTabLiveConvertor;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 页面tab仓储
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Repository
public class PageTabLiveRepo {

    @Resource
    private PageTabLiveMapper pageTabLiveMapper;


    @Resource
    private PageTabLiveConvertor pageTabLiveConvertor;

    /**
     * 添加页面tab数据
     *
     * @param pageTabLiveVS
     * @param tabId
     * @return
     */
    @Transactional
    public Integer batchAdd(List<PageTabLiveE> pageTabLiveVS, long tabId) {
        final long now = Utils.getCurrentMillis();
        List<PageTabLivePO> pageTabPOS = pageTabLiveVS.stream().map(pageTabLiveV -> {
            PageTabLivePO pageTabLivePO = pageTabLiveConvertor.entityToData(pageTabLiveV);
            pageTabLivePO.setTabId(tabId);
            pageTabLivePO.setUpdateTime(now);
            pageTabLivePO.setCreateTime(now);
            pageTabLivePO.setWeight(0);
            return pageTabLivePO;
        }).collect(Collectors.toList());
        Integer insert = pageTabLiveMapper.batchInsert(pageTabPOS);
        return insert;
    }

    public Integer edit(PageTabLiveE pageTabLiveV) {
        PageTabLivePO pageTabLivePO = pageTabLiveConvertor.entityToData(pageTabLiveV);
        return pageTabLiveMapper.update(pageTabLivePO, Utils.getCurrentMillis());
    }

    public List<PageTabLiveE> list(DBPageTabLiveQuery dbPageTabLiveQuery) {
        List<PageTabLivePO> pageTabLivePOS = pageTabLiveMapper.list(dbPageTabLiveQuery);
        if (Utils.isEmpty(pageTabLivePOS)) {
            return new ArrayList<>();
        }
        return pageTabLivePOS.stream().map(pageTabLivePO -> {
            return pageTabLiveConvertor.dataToEntity(pageTabLivePO);
        }).collect(Collectors.toList());
    }

    public Integer count(DBPageTabLiveQuery dbPageTabLiveQuery) {
        return pageTabLiveMapper.count(dbPageTabLiveQuery);

    }


    public PageTabLiveE get(long id) {
        PageTabLivePO pageTabLivePO = pageTabLiveMapper.get(id);
        return pageTabLiveConvertor.dataToEntity(pageTabLivePO);
    }

    public int delete(Long id) {
        return pageTabLiveMapper.delete(id);
    }


    public List<PageTabLiveE> getPageTabLives(DBPageTabLiveQuery dbPageTabLiveQuery){

        List<PageTabLivePO> pageTabLivePOS = pageTabLiveMapper.getPageTabLives(dbPageTabLiveQuery);
        if (Utils.isEmpty(pageTabLivePOS)) {
            return new ArrayList<>();
        }
        return pageTabLivePOS.stream().map(pageTabLivePO -> {
            return pageTabLiveConvertor.dataToEntity(pageTabLivePO);
        }).collect(Collectors.toList());
    }
}

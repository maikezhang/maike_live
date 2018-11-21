package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.PageTabMapper;
import cn.idongjia.live.db.mybatis.po.PageTabPO;
import cn.idongjia.live.db.mybatis.query.DBPageTabQuery;
import cn.idongjia.live.restructure.cache.liveHomePage.HPTabCache;
import cn.idongjia.live.restructure.convert.PageTabConvertor;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import cn.idongjia.live.restructure.dto.PageTabDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.factory.PageTabDomainFactory;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.JsonUtils;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 页面tab仓储
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Repository
public class PageTabRepo {

    @Resource
    private PageTabMapper pageTabMapper;


    @Resource
    private PageTabConvertor pageTabConvertor;

    @Resource
    private HPTabCache hpTabCache;

    private static Log LOGGER = LogFactory.getLog(PageTabE.class);
    /**
     * 添加页面tab数据
     *
     * @param pageTabE
     * @return
     */
    public Integer add(PageTabE pageTabE) {
        PageTabPO pageTabPO = pageTabConvertor.entityToData(pageTabE);
        pageTabPO.setCreateTime(Utils.getCurrentMillis());
        pageTabPO.setUpdateTime(Utils.getCurrentMillis());
        if(Objects.isNull(pageTabPO.getOnline())){
            pageTabPO.setOnline(BaseEnum.YesOrNo.YES.getCode());
        }
        if(Objects.isNull(pageTabPO.getStatus())){
            pageTabPO.setStatus(LiveEnum.TabStatus.NORMAL_STATUS.getCode());
        }
        if(Objects.isNull(pageTabPO.getRelIds())){
            pageTabPO.setRelIds(JsonUtils.toJson(new ArrayList<>()));
        }
        Integer insert = pageTabMapper.insert(pageTabPO);
        pageTabE.setId(pageTabPO.getId());
        return insert;
    }

    public Integer edit(PageTabE pageTabE) {
        PageTabPO pageTabPO = pageTabConvertor.entityToData(pageTabE);
        Integer update = pageTabMapper.update(pageTabPO, Utils.getCurrentMillis());
        vanishTabRedis();
        return update;
    }

    public List<PageTabE> list(DBPageTabQuery dbPageTabQuery) {
        List<PageTabPO> pageTabPOS = pageTabMapper.list(dbPageTabQuery);
        if (Utils.isEmpty(pageTabPOS)) {
            return new ArrayList<>();
        }
        return pageTabPOS.stream().map(pageTabPO -> {
            return pageTabConvertor.dataToEntity(pageTabPO);
        }).collect(Collectors.toList());
    }

    public Integer count(DBPageTabQuery dbPageTabQuery){
        return pageTabMapper.count(dbPageTabQuery);

    }


    public PageTabE get(long id) {
        PageTabPO pageTabPO = pageTabMapper.get(id);
        return pageTabConvertor.dataToEntity(pageTabPO);
    }

    public int delete(Long id) {
        PageTabPO pageTabPO=new PageTabPO();
        pageTabPO.setId(id);
        pageTabPO.setStatus(LiveEnum.TabStatus.DELETE_STATUS.getCode());
        Integer update = pageTabMapper.update(pageTabPO, Utils.getCurrentMillis());
        vanishTabRedis();
        return update;
    }

    public void vanishTabRedis(){
        try {
            hpTabCache.vanishRedis();
            LOGGER.info("删除直播首页缓存的tab数据成功");
        }catch (Exception e){
            LOGGER.warn("删除直播首页缓存的tab数据失败：{}",e);
        }
    }
}

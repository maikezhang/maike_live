package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.db.mybatis.po.PageTabPO;
import cn.idongjia.live.restructure.domain.entity.tab.CraftmanCategoryV;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.factory.PageTabDomainFactory;
import cn.idongjia.live.restructure.pojo.co.CategoryCO;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabCO;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Component
public class PageTabConvertor implements ConvertorI<PageTabCO, PageTabE, PageTabPO> {
    @Resource
    private PageTabDomainFactory pageTabDomainFactory;

    @Override
    public PageTabE clientToEntity(PageTabCO clientObject) {
        if (clientObject == null) {
            return null;
        }
        PageTabE pageTabE = pageTabDomainFactory.create();
        List<CategoryCO> categoryCOS = clientObject.getCategoryCOS();
        if (null == categoryCOS) {
            categoryCOS = new ArrayList<>();
        }
        pageTabE.setCraftmanCategories(categoryCOS.stream().map(categoryCO -> {
            CraftmanCategoryV craftmanCategoryV = new CraftmanCategoryV();
            craftmanCategoryV.setId(categoryCO.getId());
            craftmanCategoryV.setName(categoryCO.getName());
            return craftmanCategoryV;
        }).collect(Collectors.toList()));
        pageTabE.setCreateTime(clientObject.getCreateTime());
        pageTabE.setDesc(clientObject.getDesc());
        pageTabE.setId(clientObject.getId());
        pageTabE.setName(clientObject.getName());
        pageTabE.setOnline(BaseEnum.parseInt2Enum(clientObject.getOnline(), BaseEnum.YesOrNo.values()).orElse(null));
        pageTabE.setStatus(BaseEnum.parseInt2Enum(clientObject.getStatus(), LiveEnum.TabStatus.values()).orElse(null));
        pageTabE.setType(BaseEnum.parseInt2Enum(clientObject.getType(), LiveEnum.TabType.values()).orElse(null));
        pageTabE.setUpdateTime(clientObject.getUpdateTime());
        pageTabE.setWeight(clientObject.getWeight());
        return pageTabE;
    }

    @Override
    public PageTabE dataToEntity(PageTabPO dataObject) {
        if (dataObject == null) {
            return null;
        }
        PageTabE pageTabE = new PageTabE();
        pageTabE.setWeight(dataObject.getWeight());
        pageTabE.setUpdateTime(dataObject.getUpdateTime());
        pageTabE.setType(BaseEnum.parseInt2Enum(dataObject.getType(), LiveEnum.TabType.values()).orElse(null));
        pageTabE.setStatus(BaseEnum.parseInt2Enum(dataObject.getStatus(), LiveEnum.TabStatus.values()).orElse(null));
        pageTabE.setName(dataObject.getName());
        pageTabE.setId(dataObject.getId());
        pageTabE.setDesc(dataObject.getDesc());
        pageTabE.setCreateTime(dataObject.getCreateTime());
        String relIds = dataObject.getRelIds();
        List<CraftmanCategoryV> craftmanCategoryVS = JsonUtils.toList(relIds, CraftmanCategoryV.class);
        pageTabE.setCraftmanCategories(craftmanCategoryVS);
        pageTabE.setOnline(BaseEnum.parseInt2Enum(dataObject.getOnline(), BaseEnum.YesOrNo.values()).orElse(null));
        return pageTabE;
    }

    @Override
    public PageTabPO entityToData(PageTabE entityObject) {
        if (entityObject == null) {
            return null;
        }
        PageTabPO pageTabPO = new PageTabPO();
        pageTabPO.setWeight(entityObject.getWeight());
        pageTabPO.setUpdateTime(entityObject.getUpdateTime());
        if(!Objects.isNull(entityObject.getType())){
            pageTabPO.setType(entityObject.getType().getCode());
        }
        if(!Objects.isNull(entityObject.getStatus())){
            pageTabPO.setStatus(entityObject.getStatus().getCode());
        }
        pageTabPO.setName(entityObject.getName());
        pageTabPO.setId(entityObject.getId());
        pageTabPO.setDesc(entityObject.getDesc());
        pageTabPO.setCreateTime(entityObject.getCreateTime());
        if(!CollectionUtils.isEmpty(entityObject.getCraftmanCategories())){
            pageTabPO.setRelIds(JsonUtils.toJson(entityObject.getCraftmanCategories()));
        }
        if(!Objects.isNull(entityObject.getOnline())) {
            pageTabPO.setOnline(entityObject.getOnline().getCode());
        }
        return pageTabPO;
    }

    @Override
    public PageTabCO entityToClient(PageTabE pageTabE){
        if(Objects.isNull(pageTabE)){
            return null;
        }
        PageTabCO co=new PageTabCO();
        co.setId(pageTabE.getId());
        co.setDesc(pageTabE.getDesc());
        co.setName(pageTabE.getName());
        co.setOnline(pageTabE.getOnline().getCode());
        co.setType(pageTabE.getType().getCode());
        co.setStatus(pageTabE.getStatus().getCode());
        co.setWeight(pageTabE.getWeight());
        co.setCreateTime(pageTabE.getCreateTime());
        co.setUpdateTime(pageTabE.getUpdateTime());
        co.setDesc(pageTabE.getDesc());

        co.setCategoryCOS(pageTabE.getCraftmanCategories().stream().map(craftmanCategoryV -> {
                CategoryCO categoryCO=new CategoryCO();
                categoryCO.setId(craftmanCategoryV.getId());
                categoryCO.setName(craftmanCategoryV.getName());
                return categoryCO;
            }

        ).collect(Collectors.toList()));
        return co;

    }




}

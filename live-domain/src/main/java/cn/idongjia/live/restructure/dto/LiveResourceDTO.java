package cn.idongjia.live.restructure.dto;

import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.db.mybatis.po.LiveResourcePO;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.support.enumeration.LiveResourceType;
import cn.idongjia.live.v2.pojo.ItemResource;
import cn.idongjia.live.v2.pojo.LiveResource;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import static cn.idongjia.live.support.GsonUtil.getFirstStringInJsonArray;

/**
 * @author lc
 * @create at 2018/6/11.
 */
public class LiveResourceDTO extends BaseDTO<LiveResourcePO> {
    private static final DecimalFormat priceFormat = new DecimalFormat("0.##");

    public LiveResourceDTO(LiveResourcePO entity) {
        super(entity);
        if (entity == null) {
            entity = new LiveResourcePO();
            this.entity = entity;
        }
    }

    public ItemResource toItemResource(ItemPext itemPext, Long mainResourceId) {
        ItemResource itemResource = new ItemResource();
        itemResource.setPic(getFirstStringInJsonArray(itemPext.getPictures()));
        String priceStr = itemPext.getPrice();
        String convertPrice = LiveResourceDTO.priceFormat.format(Double.valueOf(priceStr));
        itemResource.setPrice(convertPrice);
        itemResource.setStock(itemPext.getStock());
        itemResource.setTitle(itemPext.getTitle());
        itemResource.setResourceId(itemPext.getIid());
        itemResource.setResourceType(LiveResourceType.ITEM.getCode());
        itemResource.setSaleType(itemPext.getSaletype());
        itemResource.setIsMain(entity.getResourceId().equals(mainResourceId));
        return itemResource;
    }

    public Long getResourceId() {
        return entity.getResourceId();
    }

    public void setResourceId(Long defaultTemplateId) {
        entity.setResourceId(defaultTemplateId);
    }

    public LiveResource toLiveResource(String title) {
        LiveResource liveResource = new LiveResource();
        liveResource.setId(entity.getId());
        liveResource.setResourceId(entity.getResourceId());
        liveResource.setResourceType(entity.getResourceType());
//        liveResource.setTitle(title);
        return liveResource;
    }

    public Integer getWeight() {
        return entity.getWeight();
    }

    public Long getLiveId() {
        return entity.getLiveId();
    }

    public Integer getResourceType() {
        return entity.getResourceType();
    }

    public void setResourceType(int resourceType) {
        entity.setResourceType(resourceType);
    }

    public PureLiveDetailDO assemblePureLiveDetailDO() {
        PureLiveDetailDO pureLiveDetailDO = new PureLiveDetailDO();
        pureLiveDetailDO.setCreateTm(entity.getCreateTime()==null?null:new Timestamp(entity.getCreateTime()));
        pureLiveDetailDO.setId(entity.getId());
        pureLiveDetailDO.setLid(entity.getLiveId());
        pureLiveDetailDO.setModifiedTm(entity.getModifiedTime()==null?null:new Timestamp(entity.getModifiedTime()));
        pureLiveDetailDO.setResourceId(entity.getResourceId());
        pureLiveDetailDO.setStatus(entity.getStatus());
        pureLiveDetailDO.setWeight(entity.getWeight());
        pureLiveDetailDO.setResourceType(entity.getResourceType());
        return pureLiveDetailDO;
    }
}

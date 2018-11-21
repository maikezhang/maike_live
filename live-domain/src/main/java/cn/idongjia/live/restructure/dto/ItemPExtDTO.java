package cn.idongjia.live.restructure.dto;

import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.support.enumeration.LiveResourceType;
import cn.idongjia.live.v2.pojo.ItemResource;

import java.text.DecimalFormat;

import static cn.idongjia.live.support.GsonUtil.getFirstStringInJsonArray;

/**
 * 商品DTO
 *
 * @author lc
 * @create at 2018/6/11.
 */
public class ItemPExtDTO extends BaseDTO<ItemPext> {
    private static final DecimalFormat priceFormat = new DecimalFormat("0.##");

    public ItemPExtDTO(ItemPext entity) {
        super(entity);
    }

    private void setCommonFields(ItemResource itemResource) {
        itemResource.setPic(getFirstStringInJsonArray(entity.getPictures()));
        String priceStr = entity.getPrice();
        String convertPrice = ItemPExtDTO.priceFormat.format(Double.valueOf(priceStr));
        itemResource.setPrice(convertPrice);
        itemResource.setStock(entity.getStock());
        itemResource.setTitle(entity.getTitle());
        itemResource.setResourceId(entity.getIid());
        itemResource.setResourceType(LiveResourceType.ITEM.getCode());
        itemResource.setSaleType(entity.getSaletype());
    }

    public ItemResource toItemResources() {
        ItemResource itemResource = new ItemResource();
        setCommonFields(itemResource);
        return itemResource;
    }

    public String getPictures() {
        return entity.getPictures();
    }

    public Long getIid() {
        return entity.getIid();
    }

    public String getPrice() {
        return entity.getPrice();
    }

    public String getTitle() {
        return entity.getTitle();
    }
}

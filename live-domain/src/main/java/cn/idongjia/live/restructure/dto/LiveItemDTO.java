package cn.idongjia.live.restructure.dto;

import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.api.live.pojo.LiveItemResp;

import java.math.BigDecimal;

/**
 * 直播商品数据
 *
 * @author lc
 * @create at 2018/6/12.
 */
public class LiveItemDTO extends BaseDTO<ItemPext> {
    public LiveItemDTO(ItemPext entity) {
        super(entity);
    }

    public LiveItemResp toLiveItemResp(LiveResourceDTO liveResourceDTO, Integer state) {
        LiveItemResp liveItemRes = new LiveItemResp();
        liveItemRes.setId(entity.getIid());
        if(liveResourceDTO!=null){
            liveItemRes.setIdx(liveResourceDTO.getWeight());
        }
        liveItemRes.setPrice(new BigDecimal(entity.getPrice()).multiply(new BigDecimal(100)).longValue());
        liveItemRes.setState(state);
        liveItemRes.setPic(entity.getPic());
        return liveItemRes;
    }


}

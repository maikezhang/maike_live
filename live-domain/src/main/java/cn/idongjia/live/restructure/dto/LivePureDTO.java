package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LivePurePO;
import cn.idongjia.live.pojo.purelive.PureLiveDO;
import cn.idongjia.outcry.pojo.AuctionLiveDetail;

/**
 * @author lc
 * @create at 2018/6/8.
 */
public class LivePureDTO extends BaseDTO<LivePurePO> {
    public LivePureDTO(LivePurePO entity) {
        super(entity);
    }


    public String getPic() {
        return entity.getPic();
    }

    public Integer getWeight() {
        return entity.getWeight();
    }

    public String getDesc() {
        return entity.getDesc();
    }

    public Integer getStatus() {
        return entity.getStatus();
    }

    public Long getTimeStrategy() {
        return entity.getTimeStrategy();
    }


    public Long getDuration() {
        return entity.getDuration();
    }

    public Integer getExemption() {
        return entity.getExemption();
    }

    public Long getId() {
        return entity.getId();
    }

    public PureLiveDO assemblePureLiveDO() {
        PureLiveDO pureLiveDO = new PureLiveDO();
        pureLiveDO.setDesc(entity.getDesc());
        pureLiveDO.setDuration(entity.getDuration());
        pureLiveDO.setExemption(entity.getExemption());
        pureLiveDO.setPic(entity.getPic());
        pureLiveDO.setStatus(entity.getStatus());
        pureLiveDO.setTimeStrategy(entity.getTimeStrategy());
        pureLiveDO.setWeight(entity.getWeight());
        return pureLiveDO;
    }


    public void setId(Long id) {
        entity.setId(id);
    }

    public void setPic(String pic) {
        entity.setPic(pic);
    }

    public void setStatus(int status) {
        entity.setStatus(status);
    }
}

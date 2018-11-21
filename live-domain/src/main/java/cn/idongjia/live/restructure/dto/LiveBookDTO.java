package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveBookPO;
import cn.idongjia.live.pojo.purelive.book.PureLiveBookDO;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/13.
 */
@Getter
@Setter
public class LiveBookDTO extends BaseDTO<LiveBookPO> {
    public LiveBookDTO(LiveBookPO entity) {
        super(entity);
    }

    public Long getLiveId() {
        return entity.getLiveId();
    }

    public PureLiveBookDO assemblePureLiveBookDO() {
        PureLiveBookDO pureLiveBookDO = new PureLiveBookDO();
        pureLiveBookDO.setCreateTm(entity.getCreateTime());
        pureLiveBookDO.setId(entity.getId());
        pureLiveBookDO.setLid(entity.getLiveId());
        pureLiveBookDO.setStatus(entity.getStatus());
        pureLiveBookDO.setModifiedTm(entity.getModifiedTime());
        pureLiveBookDO.setUid(entity.getUserId());
        return pureLiveBookDO;
    }

    public Long getUserId() {
        return entity.getUserId();
    }

    public void setCreateTime(Timestamp timestamp) {
        entity.setCreateTime(timestamp);
    }

    public void setModifiedTime(Timestamp modifiedTime) {
        entity.setModifiedTime(modifiedTime);
    }

    public Timestamp getModifiedTime() {
        return entity.getModifiedTime();
    }
}

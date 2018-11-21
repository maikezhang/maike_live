package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveTagRelPO;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagRelDO;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/18.
 */
public class LiveTagRelDTO extends BaseDTO<LiveTagRelPO> {
    public LiveTagRelDTO(LiveTagRelPO entity) {
        super(entity);
    }

    public Long getId() {
        return entity.getId();
    }

    public Long getLiveId() {
        return entity.getLiveId();
    }

    public void setId(Long tagRelId) {
        entity.setId(tagRelId);
    }

    public void setStatus(int status) {
        entity.setStatus(status);
    }

    public void setModifiedTime(Timestamp timestamp) {
        entity.setModifiedTime(timestamp);
    }

    public PureLiveTagRelDO assemblePureLiveTagRelDO(LiveTagDTO liveTagDTO) {
        PureLiveTagRelDO pureLiveTagRelDO = new PureLiveTagRelDO();
        pureLiveTagRelDO.setCreateTm(entity.getCreateTime());
        pureLiveTagRelDO.setId(liveTagDTO.getId());
        pureLiveTagRelDO.setLid(entity.getLiveId());
        pureLiveTagRelDO.setModifiedTm(entity.getModifiedTime());
        pureLiveTagRelDO.setStatus(entity.getStatus());
        pureLiveTagRelDO.setTagId(entity.getId());
        return pureLiveTagRelDO;
    }

    public Long getTagId() {
        return entity.getTagId();
    }
}

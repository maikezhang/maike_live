package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.VideoCoverPO;
import cn.idongjia.live.pojo.live.LiveVideoCover;

/**
 * 封面图
 *
 * @author lc
 * @create at 2018/6/11.
 */
public class VideoCoverDTO extends BaseDTO<VideoCoverPO> {
    public VideoCoverDTO(VideoCoverPO entity) {
        super(entity);
    }

    public String getUrl() {
        return entity.getUrl();
    }

    public Integer getDuration() {
        return entity.getDuration();
    }

    public String getPic() {
        return entity.getPic();
    }

    public Long getLiveId() {
        return entity.getLiveId();
    }

    public Long getId() {
        return entity.getId();
    }

    public void setUrl(String url) {
        entity.setUrl(url);
    }

    public void setPic(String pic) {
        entity.setPic(pic);
    }

    public void setCreateTime(Long createTime) {
        entity.setCreateTime(createTime);
    }

    public void setDuration(Integer duration) {
        entity.setDuration(duration);
    }

    public void setUpdateTime(Long updateTime) {
        entity.setUpdateTime(updateTime);
    }

    public void setId(Long id) {
        entity.setId(id);
    }

    public LiveVideoCover assembleLiveVideoCover() {
        if (entity == null) {
            return null;
        }
        LiveVideoCover liveVideoCover = new LiveVideoCover();
        liveVideoCover.setCreateTime(entity.getCreateTime());
        liveVideoCover.setDuration(entity.getDuration());
        liveVideoCover.setId(entity.getId());
        liveVideoCover.setPic(entity.getPic());
        liveVideoCover.setUpdateTime(entity.getUpdateTime());
        liveVideoCover.setUrl(entity.getUrl());
        return liveVideoCover;

    }
}

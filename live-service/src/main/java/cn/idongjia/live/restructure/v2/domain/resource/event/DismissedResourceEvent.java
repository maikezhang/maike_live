package cn.idongjia.live.restructure.v2.domain.resource.event;

import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.restructure.event.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 删除直播资源事件
 * @author zhang created on 2018/1/18 下午4:41
 * @version 1.0
 */
@Setter
@Getter
@ToString
public class DismissedResourceEvent extends BaseEvent {

    /**
     * 直播id
     */
    private Long liveId;
    /**
     * 解除关联资源列表
     */
    private List<PureLiveDetailDO> resources;

    public DismissedResourceEvent(Long liveId, List<PureLiveDetailDO> resources) {
        super(resources);
        this.setLiveId(liveId);
        this.setResources(resources);
    }
}

package cn.idongjia.live.restructure.v2.domain.resource.event;

import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.restructure.event.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 关联资源事件
 * @author zhang created on 2018/1/22 下午3:57
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class RelatedResourceEvent extends BaseEvent {

    /**
     * 直播id
     */
    private Long liveId;
    /**
     * 关联资源
     */
    private List<PureLiveDetailDO> details;

    public RelatedResourceEvent(Long liveId, List<PureLiveDetailDO> detailDOS) {
        super(liveId);
        this.setLiveId(liveId);
        this.setDetails(detailDOS);
    }
}

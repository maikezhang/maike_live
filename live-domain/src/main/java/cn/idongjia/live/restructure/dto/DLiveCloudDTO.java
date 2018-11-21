package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.DLiveCloudPO;
import cn.idongjia.live.pojo.live.LivePullUrl;

/**
 * @author lc
 * @create at 2018/6/17.
 */
public class DLiveCloudDTO extends BaseDTO<DLiveCloudPO> {
    public DLiveCloudDTO(DLiveCloudPO entity) {
        super(entity);
    }

    public LivePullUrl assembleDLiveCloudDTO() {
        LivePullUrl livePullUrl = new LivePullUrl();
        livePullUrl.setRtmpUrl(entity.getRtmpUrl());
        livePullUrl.setHlsUrl(entity.getHlsUrl());
        livePullUrl.setFlvUrl(entity.getFlvUrl());
        return livePullUrl;
    }
}

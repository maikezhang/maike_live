package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.v2.pojo.query.CraftsLivePPUrl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivePullUrlDTO extends BaseDTO<LivePullUrl> {
    public LivePullUrlDTO(LivePullUrl entity) {
        super(entity);
    }

    public CraftsLivePPUrl toCraftsLivePPUrl(String pushUrl) {
        CraftsLivePPUrl craftsLivePPUrl = new CraftsLivePPUrl();
        craftsLivePPUrl.setPushUrl(pushUrl);
        craftsLivePPUrl.setFlvUrl(entity.getFlvUrl());
        craftsLivePPUrl.setHlsUrl(entity.getHlsUrl());
        craftsLivePPUrl.setRtmpUrl(entity.getRtmpUrl());
        return craftsLivePPUrl;
    }

    public String getFlvUrl() {
        return entity.getFlvUrl();
    }

    public String getRtmpUrl() {
        return entity.getRtmpUrl();
    }

    public String getHlsUrl() {
        return entity.getHlsUrl();
    }
}

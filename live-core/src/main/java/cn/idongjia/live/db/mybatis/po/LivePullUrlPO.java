package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivePullUrlPO extends BasePO {

    //拉流地址
    private String rtmpUrl;
    private String hlsUrl;
    private String flvUrl;
}

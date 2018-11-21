package cn.idongjia.live.pojo.live;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhang on 2017/3/9.
 * 直播对外类
 */
@Setter
@Getter
public class LiveShow extends LiveShowDO {
    private static final long serialVersionUID = 8070365807020082669L;

    //基准随机数
    private Integer zrc;
    //聊天室管理员

    private Long suid;
    //rtmpUrl

    private String rtmpUrl;
    //hlsUrl

    private String hlsUrl;
    //flvUrl

    private String flvUrl;
    //直播云类型
    private Integer cloudType;

    public LiveShow() {
    }
}

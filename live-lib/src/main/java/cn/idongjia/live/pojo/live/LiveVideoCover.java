package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * 直播视频封面
 * Created by 岳晓东 on 2017/12/21.
 */
@Setter
@Getter
public class LiveVideoCover extends Base {

    private Long    id;
    //视频地址，videoCover用这个
    private String  url;
    //视频首图
    private String  pic;
    //视频长度
    private Integer duration;
    //创建时间
    private Long createTime;
    //修改时间
    private Long updateTime;
}

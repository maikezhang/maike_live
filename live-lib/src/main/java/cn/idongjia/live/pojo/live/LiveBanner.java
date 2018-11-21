package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveBanner extends Base {
    //主键
    private Long id;
    //封面
    private String pic;
    //跳转类型
    private Integer jumpType;
    //跳转地址
    private String jumpAddr;
    //权重
    private Integer weight;
    //状态
    private Integer status;
    //分类id
    private Long classificationId;

    /**
     * 4.7.0以后的版本才使用这个图片
     */
    private String newVersionPic;

    public LiveBanner(){}
}

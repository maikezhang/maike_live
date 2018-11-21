package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveModule extends Base {
    //主键
    private Long id;
    //主标题
    private String title;
    //副标题
    private String subTitle;
    //封面
    private String pic;
    //跳转类型
    private Integer jumpType;
    //跳转地址
    private String jumpAddr;
    //描述
    private String desc;
    //开始时间
    private Long startTime;
    //位置
    private Integer position;
    //状态
    private Integer status;
    //进程
    private Integer state;
    //权重
    private Integer weight;
    //创建时间
    private Long createTime;
    //更新时间
    private Long updateTime;

    public LiveModule(){}

}

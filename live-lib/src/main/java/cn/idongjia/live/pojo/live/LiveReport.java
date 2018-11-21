package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveReport extends Base {

    //主键id
    private long id;
    //直播id
    private long lid;
    //举报人id
    private Long reportUid;
    //举报人名
    private String reportName;
    //举报类型 1:直播
    private int type;
    //举报内容
    private String content;
    //备注
    private String desc;
    //被举报人id
    private long  hostUid;
    //被举报人名
    private String hostName;
    //直播标题
    private String title;
    //状态
    private int status;
    //创建时间
    private long createTime;
    //修改时间
    private long updateTime;
    public LiveReport(){}
}

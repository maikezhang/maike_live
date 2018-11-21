package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/26
 * Time: 下午5:07
 */
@Getter
@Setter
public class LiveReportPO {
     //主键id
    private Long id;
    //直播id
    private Long lid;
    //举报人id
    private Long reportUid;
    //举报人名
    private String reportName;
    //举报类型 1:直播
    private Integer type;
    //举报内容
    private String content;
    //备注
    private String desc;
    //被举报人id
    private Long  hostUid;
    //被举报人名
    private String hostName;
    //直播标题
    private String title;
    //状态
    private Integer status;
    //创建时间
    private Long createTime;
    //修改时间
    private Long updateTime;
    public LiveReportPO(){

    }
}

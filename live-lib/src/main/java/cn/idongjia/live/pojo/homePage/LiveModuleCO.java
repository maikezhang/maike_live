package cn.idongjia.live.pojo.homePage;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/20
 * Time: 上午10:42
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveModuleCO extends Base {

    //主键
    private Long id;
    //主标题
    private String title;
    //副标题
    private String subTitle;
    //封面
    private String cover;
    //跳转类型
    private Integer type;
    //跳转地址
    private String addr;
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
}

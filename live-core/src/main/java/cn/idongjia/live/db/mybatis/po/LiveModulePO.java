package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class LiveModulePO extends BasePO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 封面
     */
    private String pic;
    /**
     * 跳转类型
     */
    private Integer jumpType;
    /**
     * 跳转地址
     */
    private String jumpAddr;
    /**
     * 权重
     */
    private Integer weight;
    /**
     * 状态
     */
    private  Integer status;
    /**
     * 主标题
     */
    private String title;
    /**
     * 副标题
     */
    private String subTitle;
    /**
     * 描述
     */
    private String desc;
    /**
     * 生效时间
     */
    private Long startTime;
    /**
     * 位置
     */
    private Integer position;
    /**
     * 进程
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Long  createTime;
    /**
     * 修改时间
     */
    private Long updateTime;

}

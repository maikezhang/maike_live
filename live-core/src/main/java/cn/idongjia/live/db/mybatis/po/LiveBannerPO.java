package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * 直播banner对象
 * Created by dongjia_lj on 17/3/9.
 */
@Getter
@Setter
public class LiveBannerPO extends BasePO {

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
     * 分类id  首页0
     */
    private Long classificationId;
    /**
     * 创建时间
     */
    private Long  createTime;
    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 4.7.0以后的版本才使用此字段
     */
    private String newVersionPic;


}

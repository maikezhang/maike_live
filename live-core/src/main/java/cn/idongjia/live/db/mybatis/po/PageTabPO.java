package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 页面tab
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Getter
@Setter
@ToString
public class PageTabPO extends BasePO {
    /**
     * id
     */
    private Long id;

    /**
     * tab名称
     */
    private String name;

    /**
     * tab权重
     */
    private Integer weight;


    /**
     * tab类型 1普通类型 2 自定义
     */
    private Integer type;

    /**
     * 数据状态 0 正常 -1删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 描述
     */
    private String desc;

    /***
     * 关联id
     */
    private String relIds;

    /**
     * 上架状态
     */
    private Integer online;
}

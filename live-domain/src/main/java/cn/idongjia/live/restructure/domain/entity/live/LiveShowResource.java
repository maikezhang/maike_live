package cn.idongjia.live.restructure.domain.entity.live;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * 直播资源
 * @author zhang created on 2018/1/17 下午12:57
 * @version 1.0
 */
@Setter
@Getter
@ToString
public class LiveShowResource {


    /**
     * 纯直播详情ID
     */
    private Long id;
    /**
     * 资源ID
     */
    private Long resourceId;
    /**
     * 资源类型
     */
    private Integer resourceType;
    /**
     * 详情权重
     */
    private Integer weight;
    /**
     * 详情状态
     */
    private Integer status;
    /**
     * 对应纯直播ID
     */
    private Long lid;
    /**
     * 详情创建时间
     */
    private Timestamp createTm;
    /**
     * 最后更新时间
     */
    private Timestamp modifiedTm;


}

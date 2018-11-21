package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午2:12
 */
@Getter
@Setter
@ToString
public class AnchorBlackWhitePO extends BasePO {


    /**
     * 主键id
     */
    private Long id;

    /**
     * 主播id
     */
    private Long anchorId;

    /**
     * 类型 0 都不显示 1 app显示小程序不显示 2 小程序显示 app不显示  3 小程序和app都显示
     */
    private Integer type;


    /**
     * 创建时间 13位
     */
    private Long createTime;

    /**
     * 更新时间 13位
     */
    private Long updateTime;
}

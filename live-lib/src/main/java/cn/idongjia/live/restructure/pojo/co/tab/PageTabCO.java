package cn.idongjia.live.restructure.pojo.co.tab;

import cn.idongjia.live.restructure.pojo.ClientObject;
import cn.idongjia.live.restructure.pojo.co.CategoryCO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * 页面分页
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor

public class PageTabCO extends ClientObject {
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
     * tab类型
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
    private List<CategoryCO> categoryCOS;

    /**
     * 上架状态
     */
    private Integer online;
}

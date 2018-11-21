package cn.idongjia.live.v2.pojo;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * [开播时间] 附近直播
 *
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveAround extends Base {

    /**
     * 直播id
     */
    private Long         id;
    /**
     * 直播标题
     */
    private String       title;
    /**
     * 匠人名称
     */
    private String       craftsName;
    /**
     * 预计开始时间
     */
    private Long         preStartTime;
    /**
     * 匠人所属类目名称
     */
    private List<String> craftsCategoryNames;

}

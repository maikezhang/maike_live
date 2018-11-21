package cn.idongjia.live.pojo.homePage;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/20
 * Time: 上午10:37
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveHomePageCO extends Base {

    private List<LiveBannerCO> banner;

    private List<LiveModuleCO> module;

    private List<LiveTabCO> tab;

}

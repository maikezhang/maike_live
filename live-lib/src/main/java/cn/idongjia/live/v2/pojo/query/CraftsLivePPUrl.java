package cn.idongjia.live.v2.pojo.query;

import cn.idongjia.live.pojo.live.LivePullUrl;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 推拉流地址
 * Created by YueXiaodong on 2018/01/26.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CraftsLivePPUrl extends LivePullUrl {

    //推流地址
    private String pushUrl;

}

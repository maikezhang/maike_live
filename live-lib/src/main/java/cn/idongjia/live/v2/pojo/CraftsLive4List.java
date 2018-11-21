package cn.idongjia.live.v2.pojo;

import cn.idongjia.live.api.live.pojo.PlayBack;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * 用户匠人端列表
 * Created by YueXiaodong on 2018/01/18.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CraftsLive4List extends CraftsLive {

    //回放
    private List<PlayBack> playBacks;
    //创建时间
    private Long           createTime;
    //是否调试
    private Integer        isDebug;
    

}

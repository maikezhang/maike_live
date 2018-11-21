package cn.idongjia.live.restructure.pojo.co.tab;

import cn.idongjia.live.restructure.pojo.ClientObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor

public class LivePullUrlCO extends ClientObject {

    //拉流地址
    private String rtmpUrl;
    private String hlsUrl;
    private String flvUrl;

}

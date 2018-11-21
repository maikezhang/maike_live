package cn.idongjia.live.v2.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 匠人端直播详情
 * Created by YueXiaodong on 2018/01/19.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CraftsLiveDetail extends CraftsLive {
    //主播头像
    private String             hostAvatar;
    //主播名字
    private String             hostName;
    //主推资源
    private LiveResourceDetail main;
    //直播人数
    private Integer            userCount;
    //下一场直播id
    private Long               nextLiveId;
    //下一场直播预计开播时间
    private Long               nextPreStartTime;
}

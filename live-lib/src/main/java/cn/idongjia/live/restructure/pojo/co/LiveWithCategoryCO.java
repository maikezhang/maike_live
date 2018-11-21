package cn.idongjia.live.restructure.pojo.co;

import cn.idongjia.gem.lib.vo.CategoryVO;
import cn.idongjia.live.restructure.pojo.ClientObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor

public class LiveWithCategoryCO extends ClientObject {

    /**
     * id
     */
    private Long id;

    /**
     * 专场id
     */
    private Long asid;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 名称
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 纯直播状态(-5修改中-4待审核-3审核中-2审核结束-1删除0未上线1已上线)
     */
    private int status;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * uv数据
     */
    private int uv;

    /**
     * 权重
     */
    private int generalWeight;

    /**
     * 封面图地址
     */
    private String pic;

    /**
     * 短视频id
     */
    private Long videoCoverId;

    /**
     * 短视频地址
     */
    private String videoUrl;

    /**
     * 直播类型 1、纯直播2、拍卖直播
     */
    private int type;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 直播进程1未开始2已开始3已结束
     */
    private int state;

    /**
     * 主播id
     */
    private Long uid;

    /**
     * 用户类型 1匠人0普通用户
     */
    private int utype;

    /**
     * 直播预计开始时间
     */
    private Long preStartTime;

    /**
     * 直播预计结束时间
     */
    private Long preEndTime;

    /**
     * 直播创建时间
     */
    private Long createTime;

    /**
     * 直播上下线 0=上线，1=下线
     */
    private int online;

    /**
     * 是否存在回放，0=不存在，1=存在
     */
    private int hasPlayback;

    /**
     * 直播基础随机数
     */
    private int zrc;

    /**
     * 直播屏幕方向
     */
    private Integer screenDirection;

    /**
     * 推荐权重
     */
    private Integer recommendWeight;

    /**
     * 聊天室id
     */
    private Long zid;

    /**
     * 直播类目
     */
    private List<CategoryCO> categories;

    /**
     * 订阅人数
     */
    private int bookCount;


    /**
     * 0 不是推荐数据，1-新用户推荐数据，2-老用户推荐数据
     */
    private List<UserStageCO> userStages;

}

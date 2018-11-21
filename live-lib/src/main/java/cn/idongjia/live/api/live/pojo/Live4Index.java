package cn.idongjia.live.api.live.pojo;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 直播索引数据
 * <p>
 * 直播搜索改版新增
 *
 * @author yuexiaodong@idongjia.cn
 * @date 2018/07/04
 */
@Setter
@Getter
public class Live4Index extends Base {
    /****************************匠人属性***************************/
    /**
     * 匠人用户id
     */
    private Long       craftsmanUserId;
    /**
     * 匠人名字
     */
    private String     craftsmanName;
    /**
     * 匠人头衔
     */
    private String     craftsmanTitle;
    /**
     * 匠人所在城市
     */
    private String     craftsmanCity;
    /**
     * 匠人头像
     */
    private String     craftsmanAvatar;
    /**
     * 匠人所属类目id
     */
    private List<Long> categoryIdList;
    /****************************直播属性***************************/
    /**
     * 直播id
     */
    private long       id;
    /**
     * 直播封面福
     */
    private String     cover;
    /**
     * 视频封面地址
     */
    private String     videoCoverUrl;
    /**
     * 直播类型
     */
    private int        type;
    /**
     * 直播标题
     */
    private String     title;
    /**
     * 直播开始时间
     */
    private Long       startTime;
    /**
     * 直播结束时间
     */
    private Long       endTime;
    /**
     * 预计开始时间
     */
    private Long       planStartTime;
    /**
     * 直播结束时间
     */
    private Long       planEndTime;
    /**
     * uv数据
     */
    private int        uv;
    /**
     * 权重
     */
    private int        generalWeight;
    /**
     * 更新时间
     */
    private Long       updateTime;
    /**
     * 直播进程1未开始2已开始3已结束
     */
    private int        state;
    /**
     * 直播上下线 0=上线，1=下线
     */
    private int        online;
    /**
     * 用户类型 1匠人0普通用户
     */
    private int        utype;
    /**
     * 纯直播状态(-5修改中-4待审核-3审核中-2审核结束-1删除0未上线1已上线)
     */
    private int        status;
    /**
     * 直播的关联专场id
     */
    private Long       sessionId;
    /**
     * 直播关联的聊天室id
     */
    private Long       zooId;

}

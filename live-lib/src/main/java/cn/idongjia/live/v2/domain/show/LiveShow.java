package cn.idongjia.live.v2.domain.show;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveEnum;
import cn.idongjia.live.v2.domain.chat.ChatRoom;
import cn.idongjia.live.v2.support.enumeration.LiveOnlineStatus;
import cn.idongjia.live.v2.support.enumeration.LivePlayType;
import cn.idongjia.live.v2.support.enumeration.LiveStatus;
import cn.idongjia.live.v2.support.enumeration.LiveType;
import cn.idongjia.live.v2.support.enumeration.ScreenDirection;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.v2.domain.user.Anchor;
import cn.idongjia.live.v2.pojo.LiveResource;
import cn.idongjia.live.v2.support.Entity;
import cn.idongjia.util.Utils;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 直播基本实体
 * @author zhang created on 2018/1/17 下午1:17
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class LiveShow extends Entity {

    /**
     * 直播id
     */
    private Long               id;
    /**
     * 直播标题
     */
    private String             title;
    /**
     * 主播
     */
    private Anchor             anchor;
    /**
     * 直播类型
     */
    @Deprecated
    private LiveType           liveType;
    /**
     * 直播图片
     */
    private String             pic;
    /**
     * 直播介绍
     */
    private String             desc;
    /**
     * 直播简介
     */
    private String             showDesc;
    /**
     * 纯直播权重
     */
    @Deprecated
    private Integer            weight;
    /**
     * 直播上线状态
     */
    private LiveOnlineStatus   onlineStatus;
    /**
     * 直播状态
     */
    private LiveStatus         status;
    /**
     * 直播时间策略
     */
    private TimeStrategy       timeStrategy;
    /**
     * 直播播放类型
     */
    private LivePlayType       livePlayType;
    /**
     * 直播聊天室
     */
    private ChatRoom           chatRoom;
    /**
     * 直播时间
     */
    private LiveTime           liveTime;
    /**
     * 直播房间
     */
    private LiveRoom           liveRoom;
    /**
     * 直播进程
     */
    private LiveEnum.LiveState state;
    /**
     * 直播小视频封面
     */
    private VideoCover         videoCover;
    /**
     * 直播总权重
     */
    private Integer            generalWeight;
    /**
     * 直播屏幕方向
     */
    private ScreenDirection    direction;
    /**
     * 是否自动上线
     */
    private BaseEnum.YesOrNo   autoOnline;
    /**
     * 直播资源列表
     */
    private List<LiveResource> resources;

    /**
     * 直播标题设置 标题必填
     * @param title 标题
     */
    public void setTitle(String title, int maxLength){
        if (Strings.isNullOrEmpty(title)){
            throw LiveException.failure("直播标题必填");
        }
        if (title.length() > maxLength){
            throw LiveException.failure("直播标题不能超过" + maxLength + "字");
        }
        this.setTitle(title);
    }

    /**
     * 设置直播标题
     * @param title 直播标题
     */
    private void setTitle(String title){
        this.title = title;
    }

    /**
     * 直播封面图设置 必填
     * @param pic 图片
     */
    public void setPic(String pic){
        if (Strings.isNullOrEmpty(pic)){
            throw LiveException.failure("直播封面必填");
        }
        this.pic = pic;
    }

    /**
     * 直播主播设置 主播id不能为空
     * @param anchor 主播类
     */
    public void setAnchor(Anchor anchor){
        if (Objects.isNull(anchor)){
            throw LiveException.failure("直播主播必填");
        }
        this.anchor = anchor;
    }

    /**
     * 直播简介设置 必填
     * @param showDesc 直播简介
     */
    public void setShowDesc(String showDesc, long maxLength){
        if (Strings.isNullOrEmpty(showDesc)){
            throw LiveException.failure("直播简介必填");
        }
        if (showDesc.length() > maxLength){
            throw LiveException.failure("直播简介不能超过" + maxLength + "字");
        }
        this.setShowDesc(showDesc);
    }

    /**
     * 设置直播简介
     * @param showDesc 直播简介
     */
    private void setShowDesc(String showDesc){
        this.showDesc = showDesc;
    }

    /**
     * 直播时间设置
     * @param liveTime 直播时间
     */
    public void setLiveTime(LiveTime liveTime){
        if (Objects.isNull(liveTime)){
            throw LiveException.failure("直播预计开始时间必填");
        }
        this.liveTime = liveTime;
    }

    /**
     * 直播资源设置
     * @param resources 直播资源
     */
    public void setResources(List<LiveResource> resources, int maxNum){
        if (! Utils.isEmpty(resources) && resources.size() > maxNum){
            throw LiveException.failure("直播最多能关联" + maxNum + "商品");
        }
        this.setResources(resources);
    }

    /**
     * 直播资源设置
     * @param resources 直播资源
     */
    private void setResources(List<LiveResource> resources){
        if (Utils.isEmpty(resources)){
            resources = new ArrayList<>();
        }
        this.resources = new ArrayList<>(resources);
    }
}

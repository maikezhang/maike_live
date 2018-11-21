package cn.idongjia.live.db.mybatis.po;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 纯直播
 *
 * @author lc
 * @create at 2018/6/8.
 */
@Getter
@Setter
public class LivePurePO extends BasePO {

    //纯直播图片
    private String  pic;
    //纯直播权重
    private Integer weight;
    //纯直播描述
    private String  desc;
    //纯直播状态(-4修改中-3待审核-2审核中-1删除0未上线1上线)
    private Integer status;
    //直播播出时间策略ID
    private Long    timeStrategy;

    //录制时长
    private Long duration;

    //纯直播是否免审
    private Integer exemption;

    private Long id;

    public LivePurePO() {

    }

    @Builder
    public LivePurePO(String pic, Integer weight, String desc, Integer status, Long timeStrategy, Long duration, Integer exemption, Long id) {
        this.pic = pic;
        this.weight = weight;
        this.desc = desc;
        this.status = status;
        this.timeStrategy = timeStrategy;
        this.duration = duration;
        this.exemption = exemption;
        this.id = id;
    }
}

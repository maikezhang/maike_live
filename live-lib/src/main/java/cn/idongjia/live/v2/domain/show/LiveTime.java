package cn.idongjia.live.v2.domain.show;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.v2.support.ValueObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 直播时间类
 * @author zhang created on 2018/1/17 下午1:52
 * @version 1.0
 */
@Setter
@Getter
@ToString
public class LiveTime extends ValueObject {

    /**
     * 预展时间
     */
    private Long preViewTime;
    /**
     * 预计开始时间
     */
    private Long preStartTime;
    /**
     * 预计结束时间
     */
    private Long preEndTime;
    /**
     * 实际开始时间
     */
    private Long startTime;
    /**
     * 实际结束时间
     */
    private Long endTime;

    /**
     * 设置预计开播时间
     * @param preStartTime 预计开播时间
     */
    public void setPreStartTime(Long preStartTime){
        if (Objects.isNull(preStartTime)){
            throw LiveException.failure("直播预计开播时间必填");
        }
        if (preStartTime < 0) {
            throw LiveException.failure("直播预计开播时间不合理");
        }
        this.preStartTime = preStartTime;
    }
}

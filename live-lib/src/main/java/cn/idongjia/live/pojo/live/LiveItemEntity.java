package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 直播关联商品实体
 *
 * @author lc
 * @create at 2017/12/21.
 */
@Getter
@Setter
@NoArgsConstructor
public class LiveItemEntity extends Base implements Comparable<LiveItemEntity> {

    // id
    private Long    id;

    // 价格
    private Long    price;

    // 状态 0未开始 1已开始 2已结束
    private Integer state;

    // 封面图地址
    private String  pic;

    // 排序
    private Integer weight;

    //类型 1：纯直播 2：拍卖直播
    private Integer type;

    @Override
    public int compareTo(LiveItemEntity liveItemEntity) {
        if(null==type || type==2) {
            Integer thisState = this.state;
            if (thisState == 2) {
                thisState = -1;
            }
            Integer otherState = liveItemEntity.getState();
            if (otherState == 2) {
                otherState = -1;
            }
            int compareValue = thisState.compareTo(otherState);
            if (compareValue == 0) {
                compareValue = weight.compareTo(liveItemEntity.getWeight());
            }
            return -compareValue;
        }else{
           return weight.compareTo(liveItemEntity.getWeight());
        }
    }
}

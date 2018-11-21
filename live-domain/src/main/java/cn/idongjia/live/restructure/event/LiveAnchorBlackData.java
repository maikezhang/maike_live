package cn.idongjia.live.restructure.event;

import cn.idongjia.live.restructure.domain.entity.live.*;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.enumeration.LivePlayType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午11:20
 */
@Getter
@Setter
@ToString
@Builder
public class LiveAnchorBlackData {


    /**
     *  主播ids
     */
   private List<Long> anchorIds;

}

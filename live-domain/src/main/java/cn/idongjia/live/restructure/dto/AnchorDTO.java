package cn.idongjia.live.restructure.dto;

import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Getter
@Setter
public class AnchorDTO {
    private CustomerVo    customerVo;
    private int           followerCount;
    private Integer       isBooked;
    private AnchorBookDTO anchorBookDTO;

}

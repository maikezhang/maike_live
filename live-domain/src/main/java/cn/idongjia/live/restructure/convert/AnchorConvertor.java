package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.dto.AnchorBookDTO;
import cn.idongjia.live.restructure.dto.AnchorDTO;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import org.springframework.stereotype.Component;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component("anchorConvertor")
public class AnchorConvertor implements ConvertorI<Anchor,LiveAnchor,AnchorDTO> {
    @Override
    public Anchor dataToClient(AnchorDTO anchorDTO) {
        CustomerVo customerVo = anchorDTO.getCustomerVo();
        int        followerCount = anchorDTO.getFollowerCount();
        Integer    isBooked = anchorDTO.getIsBooked();
        AnchorBookDTO anchorBookDTO = anchorDTO.getAnchorBookDTO();
        Anchor anchor = new Anchor();
        anchor.setHavatar(customerVo.getAvatar());
        anchor.setFans(followerCount);
        anchor.setHuid(anchorBookDTO.getAnchorId());
        anchor.setHtitle(customerVo.getCraftsman().getTitle());
        anchor.setHusername(customerVo.getName());
        anchor.setIsBooked(isBooked);
        return anchor;
    }
}

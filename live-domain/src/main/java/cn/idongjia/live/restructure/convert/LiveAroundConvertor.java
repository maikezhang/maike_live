package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.user.Category;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.dto.LiveAroundDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.v2.pojo.LiveAround;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component("liveAroundConvertor")
public class LiveAroundConvertor implements ConvertorI<LiveAround, LiveEntity, LiveAroundDTO> {
    @Override
    public  LiveAround dataToClient(LiveAroundDTO liveAroundDTO) {
        LiveAnchor liveAnchor = liveAroundDTO.getLiveAnchor();
        LiveShowDTO liveShowDTO = liveAroundDTO.getLiveShowDTO();
        if (liveShowDTO == null || liveShowDTO.getId() == null) {
            return null;
        }
        LiveAround liveAround = new LiveAround();
        liveAround.setTitle(liveShowDTO.getTitle());
        liveAround.setPreStartTime(liveShowDTO.getEstimatedStartTime());
        if (liveAround == null) {
            return null;
        }
        liveAround.setId(liveShowDTO.getId());
        liveAround.setPreStartTime(liveShowDTO.getEstimatedStartTime());
        liveAround.setTitle(liveShowDTO.getTitle());
        if (liveAnchor != null) {
            liveAround.setCraftsName(liveAnchor.getUsername());
            liveAround.setCraftsCategoryNames(liveAnchor.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.toList()));
        }
        return liveAround;
    }
}

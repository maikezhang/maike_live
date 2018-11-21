package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.restructure.domain.entity.live.LiveShowResource;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import org.springframework.stereotype.Component;

/**
 * @author lc
 * @create at 2018/7/18.
 */
@Component("pureLiveDetailConvert")
public class PureLiveDetailConvert implements ConvertorI<PureLiveDetailDO, LiveShowResource, LiveResourceDTO> {
    @Override
    public PureLiveDetailDO dataToClient(LiveResourceDTO liveResourceDTO) {
        PureLiveDetailDO detailDO = new PureLiveDetailDO();
        detailDO.setResourceType(liveResourceDTO.getResourceType());
        detailDO.setResourceId(liveResourceDTO.getResourceId());
        detailDO.setWeight(liveResourceDTO.getWeight());
        return detailDO;
    }
}

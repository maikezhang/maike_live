package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.HotAnchorsService;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.restructure.biz.AnchorBO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("hotAnchorsServiceImpl")
public class HotAnchorsServiceImpl implements HotAnchorsService {

    @Resource
    private AnchorBO anchorBO;

    @Override
    public boolean addHotAnchors(List<Long> uids) {
        return uids != null && anchorBO.addHotAnchors(uids);
    }

    @Override
    public List<Long> listHotAnchors() {
        return anchorBO.hotAnchors();
    }

    @Override
    public List<Anchor> listHotAnchorsRandomly(Long uid) {
        return anchorBO.listRandomly(uid);
    }

    @Override
    public boolean deleteHotAnchor(Long uid) {
        return anchorBO.deleteHotAnchor(uid);
    }

    @Override
    public boolean addHotAnchor(Long uid) {
        return anchorBO.addHotAnchor(uid);
    }
}

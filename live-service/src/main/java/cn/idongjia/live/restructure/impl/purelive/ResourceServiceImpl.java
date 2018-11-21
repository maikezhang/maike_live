package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.ResourceService;
import cn.idongjia.live.restructure.biz.LiveShowBO;
import cn.idongjia.live.restructure.repo.LiveResourceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资源管理
 * @author dongjia_lj
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private LiveShowBO liveShowBO;


    @Override
    public boolean mainResource(long lid, long rid, int rtype, int status) {
        return liveShowBO.mainResource(lid,rid,rtype,status);
    }

    @Override
    public Long mainResource(long lid, int rtype) {
        return liveShowBO.mainResource(lid,rtype);
    }


}

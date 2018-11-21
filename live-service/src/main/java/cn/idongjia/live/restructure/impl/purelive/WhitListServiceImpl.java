package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.WhiteListService;
import cn.idongjia.live.restructure.repo.WhiteListRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("whiteListServiceImpl")
public class WhitListServiceImpl implements WhiteListService {

    @Resource
    private WhiteListRepo whiteListRepo;

    @Override
    public boolean addAnchors(List<Long> uids){
        return whiteListRepo.addUids(uids);
    }

    @Override
    public List<Long> listWhitList(){
        return whiteListRepo.listUids();
    }

    @Override
    public boolean deleteAnchor(Long uid) {
        return whiteListRepo.deleteAnchor(uid);
    }

    @Override
    public boolean addAnchor(Long uid) {
        return whiteListRepo.addAnchor(uid);
    }
}

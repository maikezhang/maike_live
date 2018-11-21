package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PureLiveAuditService;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.audit.PureLiveAudit;
import cn.idongjia.live.pojo.purelive.audit.PureLiveAuditDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.query.purelive.audit.PureLiveAuditSearch;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("pureLiveAuditServiceImpl")
public class PureLiveAuditServiceImpl implements PureLiveAuditService{


    @Override
    public Long addPureLiveAudit(PureLiveAuditDO pureLiveAuditDO){
//        return pureLiveAuditRepo.addAudit(pureLiveAuditDO);
        return null;

    }
    @Override
    public boolean deletePureLiveAudit(Long aid){
//        return pureLiveAuditRepo.deleteAudit(aid);
        return false;
    }
    @Override
    public boolean updatePureLiveAudit(Long aid, PureLiveAuditDO pureLiveAuditDO){
//        return pureLiveAuditRepo.updateAudit(aid, pureLiveAuditDO);
        return false;
    }
    @Override
    public BaseList<PureLiveAuditDO> listPureLiveAudits(PureLiveAuditSearch pureLiveAuditSearch){
//        return pureLiveAuditRepo.listAuditsWithCount(pureLiveAuditSearch);
        return null;
    }
    @Override
    public Integer countPureLiveAudits(PureLiveAuditSearch pureLiveAuditSearch){
//        return pureLiveAuditRepo.countAudits(pureLiveAuditSearch);
        return null;
    }
    @Override
    public boolean finishPureLiveAudit(PureLiveAuditDO pureLiveAuditDO){
//        return pureLiveAuditRepo.finishAudit(pureLiveAuditDO);
        return false;
    }
    @Override
    public List<PureLive> listPureLivesAudit(PureLiveSearch pureLiveSearch){
//        return pureLiveAuditRepo.listPureLives(pureLiveSearch);
        return null;
    }

    @Override
    public Integer countPureLiveAudit(PureLiveSearch pureLiveSearch) {
//        return pureLiveAuditRepo.countPureLives(pureLiveSearch);
        return null;
    }
    @Override
    public PureLive getPureLiveAudit(Long suid, Long lid){
//        return pureLiveAuditRepo.getPureLive(suid, lid);
        return null;
    }
    @Override
    public BaseList<PureLive> listPureLivesAuditWithCount(PureLiveSearch pureLiveSearch){
//        return pureLiveAuditRepo.listPureLivesWithCount(pureLiveSearch);
        return null;
    }
    @Override
    public PureLiveAudit getAuditInfoByLid(Long lid){
//        return pureLiveAuditRepo.getAuditByLid(lid);
        return null;

    }
}

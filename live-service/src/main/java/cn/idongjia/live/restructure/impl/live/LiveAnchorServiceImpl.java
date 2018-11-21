package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.LiveAnchorEnum;
import cn.idongjia.live.restructure.biz.AnchorBO;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.v2.pojo.LiveAnchorBan;
import cn.idongjia.live.v2.pojo.LiveAnchorBanRecord;
import cn.idongjia.live.v2.pojo.query.LiveAnchorBanRecordQuery;
import cn.idongjia.live.v2.service.LiveAnchorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
@Service("liveAnchorServiceImpl")
public class LiveAnchorServiceImpl implements LiveAnchorService {

    @Resource
    private AnchorBO anchorBO;

    @Override
    public void banAnchor(LiveAnchorBan anchorBan) {
        if (anchorBan == null) {
            throw LiveException.failure("参数不能为null");
        }
        if (anchorBan.getUserId() == null) {
            throw LiveException.failure("userId非法");
        }
        if (anchorBan.getAdminId() == null || anchorBan.getAdminId() < 1) {
            throw LiveException.failure("adminId非法");
        }
        LiveAnchorEnum.BanOperationType operation = BaseEnum.parseInt2Enum(anchorBan.getOperation(),
                LiveAnchorEnum.BanOperationType.values())
                .orElseThrow(() -> LiveException.failure("operation错误"));
        anchorBO.banAnchor(anchorBan);
    }

    @Override
    public BaseList<LiveAnchorBanRecord> listBannedRecord(LiveAnchorBanRecordQuery query) {
        if (query == null) {
            query = new LiveAnchorBanRecordQuery();
        }
        if (query.getLimit() == null || query.getLimit() < 1) {
            query.setLimit(15);
        }
        if (query.getPage() == null || query.getPage() < 1) {
            query.setPage(1);
        }
        return anchorBO.listBannedRecord(query);
    }
}


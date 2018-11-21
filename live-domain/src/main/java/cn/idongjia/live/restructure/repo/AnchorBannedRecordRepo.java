package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.LiveAnchorBannedRecordMapper;
import cn.idongjia.live.db.mybatis.po.AnchorBannedRecordPO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveAnchorEnum;
import cn.idongjia.live.restructure.dto.AnchorBannedRecordDTO;
import cn.idongjia.live.v2.pojo.query.LiveAnchorBanRecordQuery;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 主播禁播记录
 *
 * @author lc
 * @create at 2018/6/28.
 */
@Repository
public class AnchorBannedRecordRepo {

    @Resource
    private LiveAnchorBannedRecordMapper liveAnchorBannedRecordMapper;

    public void insert(AnchorBannedRecordDTO anchorBannedRecordDTO) {
        if (anchorBannedRecordDTO == null) {
            throw LiveException.failure("插入禁播数据不能为空");
        }
        AnchorBannedRecordPO anchorBannedRecordPO = anchorBannedRecordDTO.toDO();
        liveAnchorBannedRecordMapper.insert(anchorBannedRecordPO);
    }

    public List<AnchorBannedRecordDTO> select(LiveAnchorBanRecordQuery query) {
        List<AnchorBannedRecordPO> anchorBannedRecordPOS = liveAnchorBannedRecordMapper.select(query);
        List<AnchorBannedRecordDTO> anchorBannedRecordDTOS = null;
        if (!Utils.isEmpty(anchorBannedRecordPOS)) {
            anchorBannedRecordDTOS = anchorBannedRecordPOS.stream().map(AnchorBannedRecordDTO::new).collect(Collectors.toList());
        } else {
            anchorBannedRecordDTOS = new ArrayList<>();
        }
        return anchorBannedRecordDTOS;
    }

    public Integer countWithAnchor(LiveAnchorBanRecordQuery query) {
        return liveAnchorBannedRecordMapper.countWithAnchor(query);
    }

    public List<AnchorBannedRecordPO> selectWithAnchor(LiveAnchorBanRecordQuery query) {
        return liveAnchorBannedRecordMapper.selectWithAnchor(query);
    }

    public AnchorBannedRecordDTO takeLastBannedRecord(Long userId) {
        if (userId == null) {
            return null;
        }
        LiveAnchorBanRecordQuery query = new LiveAnchorBanRecordQuery();
        query.setUserId(userId);
        query.setOrderBy("id desc");
        query.setLimit(1);
        query.setOperation(LiveAnchorEnum.BanOperationType.BANNED.getCode());
        final List<AnchorBannedRecordDTO> records = select(query);
        if (Utils.isEmpty(records)) {
            return null;
        }
        return records.get(0);
    }
}

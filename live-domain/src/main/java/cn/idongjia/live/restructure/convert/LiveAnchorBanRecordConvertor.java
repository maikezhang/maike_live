package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.db.mybatis.po.AnchorBannedRecordPO;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.v2.pojo.LiveAnchorBanRecord;
import org.springframework.stereotype.Component;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component("liveAnchorBanRecordConvertor")
public class LiveAnchorBanRecordConvertor implements ConvertorI<LiveAnchorBanRecord, LiveAnchor, AnchorBannedRecordPO> {
    @Override
    public LiveAnchorBanRecord dataToClient(AnchorBannedRecordPO anchorBannedRecordPO) {
        return LiveAnchorBanRecord.builder()
                .id(anchorBannedRecordPO.getId())
                .userId(anchorBannedRecordPO.getUserId())
                .adminId(anchorBannedRecordPO.getAdminId())
                .operation(anchorBannedRecordPO.getOperation())
                .durationDay(anchorBannedRecordPO.getDurationDay())
                .message(anchorBannedRecordPO.getMessage())
                .updateTime(anchorBannedRecordPO.getUpdateTime())
                .createTime(anchorBannedRecordPO.getCreateTime())
                .build();
    }
}

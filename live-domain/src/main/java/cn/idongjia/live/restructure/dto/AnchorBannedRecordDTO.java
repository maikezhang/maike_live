package cn.idongjia.live.restructure.dto;


import cn.idongjia.live.db.mybatis.po.AnchorBannedRecordPO;
import cn.idongjia.live.v2.pojo.LiveAnchorBanRecord;

/**
 * @author YueXiaodong, yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
public class AnchorBannedRecordDTO extends BaseDTO<AnchorBannedRecordPO> {

    public AnchorBannedRecordDTO(AnchorBannedRecordPO entity) {
        super(entity);
    }




    public AnchorBannedRecordDTO(LiveAnchorBanRecord liveAnchorBanRecord) {
        super(null);
        AnchorBannedRecordPO anchorBannedRecordPO = AnchorBannedRecordPO.builder()
                .id(liveAnchorBanRecord.getId())
                .userId(liveAnchorBanRecord.getUserId())
                .adminId(liveAnchorBanRecord.getAdminId())
                .operation(liveAnchorBanRecord.getOperation())
                .durationDay(liveAnchorBanRecord.getDurationDay())
                .message(liveAnchorBanRecord.getMessage())
                .updateTime(liveAnchorBanRecord.getUpdateTime())
                .createTime(liveAnchorBanRecord.getCreateTime())
                .build();

        super.entity = anchorBannedRecordPO;
    }

    public Integer getDurationDay() {
        return entity.getDurationDay();

    }
}

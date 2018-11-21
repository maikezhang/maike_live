package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.LiveAnchorMapper;
import cn.idongjia.live.db.mybatis.po.LiveAnchorPO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveAnchorEnum;
import cn.idongjia.live.restructure.dto.AnchorBannedRecordDTO;
import cn.idongjia.live.restructure.dto.LiveAnchorDTO;
import cn.idongjia.live.v2.pojo.query.LiveAnchorBanRecordQuery;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lc
 * @create at 2018/6/28.
 */
@Repository
public class LiveAnchorRepo {
    @Resource
    private LiveAnchorMapper liveAnchorMapper;

    public LiveAnchorDTO getByUserId(long userId) {
        LiveAnchorPO liveAnchorPO = liveAnchorMapper.getByUserId(userId);
        return liveAnchorPO==null?null:new LiveAnchorDTO(liveAnchorPO);
    }

    public void insert(LiveAnchorDTO liveAnchorDTO) {
        if(liveAnchorDTO==null){
            throw LiveException.failure("主播不能为空");
        }
        LiveAnchorPO liveAnchorPO = liveAnchorDTO.toDO();
        liveAnchorMapper.insert(liveAnchorPO);
    }

    public void update(LiveAnchorDTO liveAnchorDTO) {
        if(liveAnchorDTO==null){
            throw LiveException.failure("主播不能为空");
        }
        liveAnchorMapper.update(liveAnchorDTO.toDO());
    }

    /**
     * @param userId
     * @return 禁播=是
     */
    public boolean isBanned(Long userId) {
        if (userId == null) {
            return false;
        }
        final LiveAnchorDTO liveAnchorDTO = getByUserId(userId);
        if (liveAnchorDTO == null) {
            return false;
        }
        if (LiveAnchorEnum.AnchorState.BANNED.getCode() == liveAnchorDTO.getAnchorState()) {
            return true;
        }
        return false;
    }



}

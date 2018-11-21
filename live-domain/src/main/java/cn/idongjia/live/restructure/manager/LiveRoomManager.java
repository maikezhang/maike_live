package cn.idongjia.live.restructure.manager;

import cn.idongjia.live.restructure.domain.entity.room.LiveRoom;
import cn.idongjia.live.restructure.dto.LiveRoomDTO;
import cn.idongjia.live.restructure.repo.LiveRoomRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 直播间模型管理
 *
 * @author lc
 * @create at 2018/6/8.
 */
@Component
public class LiveRoomManager {

    @Resource
    private LiveRoomRepo liveRoomRepo;

    public Optional<LiveRoom> load(Long liveRoomId) {
        if(liveRoomId == null) {
            return Optional.empty();
        }
        LiveRoomDTO liveRoomDTO    = liveRoomRepo.getById(liveRoomId);
        LiveRoom    liveRoomEntity = new LiveRoom();
        liveRoomEntity.setCloudId(liveRoomDTO.getCloudId());
        liveRoomEntity.setCloudType(liveRoomDTO.getCloudType());
        liveRoomEntity.setCreateTime(liveRoomDTO.getCreateTime());
        liveRoomEntity.setModifiedTime(liveRoomDTO.getModifiedTime());
        liveRoomEntity.setStatus(liveRoomDTO.getStatus());
        liveRoomEntity.setId(liveRoomDTO.getId());
        liveRoomEntity.setUid(liveRoomDTO.getUserId());
        return Optional.of(liveRoomEntity);
    }
}

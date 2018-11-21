package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.VideoCoverMapper;
import cn.idongjia.live.db.mybatis.po.VideoCoverPO;
import cn.idongjia.live.db.mybatis.query.DBLiveVideoCoverQuery;
import cn.idongjia.live.pojo.live.LiveVideoCover;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by 岳晓东 on 2017/12/22.
 */
@Repository
public class VideoCoverRepo {
    @Resource
    private VideoCoverMapper mapper;

    public Long add(VideoCoverDTO videoCoverDTO) {
        VideoCoverPO videoCoverPO = videoCoverDTO.toDO();
        mapper.add(videoCoverPO);
        return videoCoverPO.getId();
    }

    public VideoCoverDTO getByLiveShowID(long id) {
        List<VideoCoverPO> byLiveId = mapper.getByLiveId(id);
        if(CollectionUtils.isEmpty(byLiveId)){
            return null;
        }
        VideoCoverPO po=byLiveId.get(byLiveId.size()-1);
        return new VideoCoverDTO(po);
    }

    public void update(VideoCoverDTO videoCoverDTO) {
        VideoCoverPO po=videoCoverDTO.toDO();

        Long liveId=po.getLiveId();
        if(!Objects.isNull(liveId)) {
            VideoCoverDTO byLiveShowID = getByLiveShowID(liveId);
            if (Objects.nonNull(byLiveShowID)) {
                po.setId(byLiveShowID.getId());
            }
        }
        if(Objects.isNull(po.getId())){
            add(videoCoverDTO);
        }else{
            mapper.update(videoCoverDTO.toDO(), Utils.getCurrentMillis());
        }
    }

    public void delete(Long liveId){
        mapper.delete(liveId);

    }

    public List<VideoCoverDTO> list(DBLiveVideoCoverQuery dbLiveVideoCoverQuery) {
        List<VideoCoverPO> videoCoverPOS = mapper.list(dbLiveVideoCoverQuery);
        return videoCoverPOS.stream().map(VideoCoverDTO::new).collect(Collectors.toList());
    }

    /**
     * 重构使用
     */
    public void batchUpdateVideoCoverLiveId(List<VideoCoverPO> pos) {
        for (VideoCoverPO po:pos){
            mapper.updateVideoCoverLiveId(po,Utils.getCurrentMillis());
        }

    }

    public VideoCoverDTO get(Long id) {
        VideoCoverPO videoCoverPO = mapper.get(id);
        return videoCoverPO==null?null:new VideoCoverDTO(videoCoverPO);
    }
}

package cn.idongjia.live.restructure.impl.cover;

import cn.idongjia.live.api.live.VideoCoverService;
import cn.idongjia.live.db.mybatis.po.VideoCoverPO;
import cn.idongjia.live.pojo.live.LiveVideoCover;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.repo.VideoCoverRepo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 岳晓东 on 2017/12/22.
 */
@Service("videoCoverService")
public class VideoCoverServiceImpl implements VideoCoverService {

    @Resource
    private VideoCoverRepo videoCoverRepo;

    @Override
    public Long add(LiveVideoCover cover) {
        if (cover.getUrl() == null) {
            return null;
        }
        VideoCoverDTO videoCoverDTO=new VideoCoverDTO(new VideoCoverPO());
        videoCoverDTO.setUrl(cover.getUrl());
        videoCoverDTO.setPic(cover.getPic());
        videoCoverDTO.setDuration(cover.getDuration());
        videoCoverDTO.setCreateTime(Utils.getCurrentMillis());
        videoCoverDTO.setUpdateTime(Utils.getCurrentMillis());
        videoCoverDTO.setId(cover.getId());
        return videoCoverRepo.add(videoCoverDTO);
    }

    @Override
    public void update(LiveVideoCover cover) {
        VideoCoverDTO videoCoverDTO=new VideoCoverDTO(new VideoCoverPO());
        videoCoverDTO.setUrl(cover.getUrl());
        videoCoverDTO.setPic(cover.getPic());
        videoCoverDTO.setDuration(cover.getDuration());
        videoCoverDTO.setCreateTime(Utils.getCurrentMillis());
        videoCoverDTO.setUpdateTime(Utils.getCurrentMillis());
        videoCoverDTO.setId(cover.getId());
        videoCoverRepo.update(videoCoverDTO);
    }

    @Override
    public LiveVideoCover get(Long id) {
        VideoCoverDTO videoCoverDTO = videoCoverRepo.get(id);
        return videoCoverDTO.assembleLiveVideoCover();
    }
}

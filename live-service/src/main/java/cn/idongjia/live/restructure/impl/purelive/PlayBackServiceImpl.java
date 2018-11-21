package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PlayBackService;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.playback.PlayBackAdmin;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDO;
import cn.idongjia.live.query.purelive.PlayBackSearch;
import cn.idongjia.live.restructure.biz.PlayBackBO;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Component("playBackServiceImpl")
public class PlayBackServiceImpl implements PlayBackService {

    @Resource
    private PlayBackBO playBackBO;
    @Resource
    private MqProducerManager mqProducerManager;

    @Override
    public Long addPlayBack(PlayBackDO playBackDO) {
//        Long id = playBackRepo.addPlayBack(playBackDO);
//        mqProducerManager.pushLiveModify(playBackDO.getLid());
        return null;
    }

    @Override
    public List<PlayBackDO> getPlayBackByLid(Long lid) {
        return playBackBO.listByLiveId(lid);
    }

    @Override
    public boolean deletePlayBack(PlayBackDO playBackDO) {
        return playBackBO.deletePlayBack(playBackDO);
    }

    @Override
    public boolean updatePlayBack(PlayBackDO playBackDO) {
//        return playBack;
        return true;
    }

    @Override
    public BaseList<PlayBackAdmin> listPlayBackWithCount(PlayBackSearch playBackSearch) {
        return playBackBO.listForAdmin(playBackSearch);
    }

    @Override
    public boolean deletePlayBack(Long id, PlayBackDO playBackDO) {
        playBackDO.setId(id);
        return playBackBO.deletePlayBack(playBackDO);
    }

    @Override
    public List<List<PlayBackDO>> getBatchPlayBackByLiveId(PlayBackSearch search) {
        if (search == null || search.getLiveIds() == null || search.getLiveIds().size() == 0) {
            return Collections.EMPTY_LIST;
        }

        return playBackBO.getBatchPlayBackByLiveId(search);
    }
}



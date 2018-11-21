package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.PlayBackMapper;
import cn.idongjia.live.db.mybatis.po.PlayBackPO;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLiveDO;
import cn.idongjia.live.pojo.purelive.playback.PlayBackAdmin;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDO;
import cn.idongjia.live.query.purelive.PlayBackSearch;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class PlayBackRepo {

    private static final int STATUS_PLAYBACK_NORMAL = 0;
    private static final int STATUS_PLAYBACK_DEL = -1;
    private static final Log LOGGER = LogFactory.getLog(PlayBackRepo.class);

    @Resource
    private PlayBackMapper playBackMapper;

    /**
     * 增加回放信息
     *
     * @param playBackDTO 回放信息
     * @return 回放ID
     */
    public Long addPlayBack(PlayBackDTO playBackDTO) {
        playBackDTO.setCreateTime(new Timestamp(Utils.getCurrentMillis()));
        playBackDTO.setStatus(PlayBackRepo.STATUS_PLAYBACK_NORMAL);
        LOGGER.info("增加回放内容为:{} ", playBackDTO);
        PlayBackPO playBackPO = playBackDTO.toDO();
        if (playBackMapper.insert(playBackPO) > 0) {
            PlayBackRepo.LOGGER.info("增加回放成功ID: {}", playBackPO);
        } else {
            PlayBackRepo.LOGGER.info("增加回放失败");
        }
        return playBackPO.getId();
    }

    /**
     * 根据直播ID获取回放列表
     *
     * @param liveId 直播ID
     * @return 回放列表
     */
    public List<PlayBackPO> listByLiveId(Long liveId) {
        DBPlayBackQuery dbPlayBackQuery = DBPlayBackQuery.builder().status(PlayBackRepo.STATUS_PLAYBACK_NORMAL).liveIds(Arrays.asList(liveId)).build();
        return playBackMapper.list(dbPlayBackQuery);
    }



    /**
     * 更新回放信息
     *
     * @param playBackDTO 回放信息
     * @return 是否成功
     */
    public boolean updatePlayBack(PlayBackDTO playBackDTO) {
        PlayBackRepo.LOGGER.info("更新回放内容为: {}", playBackDTO);
        PlayBackPO playBackPO = playBackDTO.toDO();
        return playBackMapper.update(playBackPO, Utils.getCurrentMillis()) > 0;
    }

    /**
     * 根据检索条件获取回放列表
     *
     * @param dbPlayBackQuery 检索条件
     * @return 回放列表
     */
    public BaseList<PlayBackDTO> page(DBPlayBackQuery dbPlayBackQuery) {
        BaseList<PlayBackDTO> baseList = new BaseList<>();
        int count = playBackMapper.count(dbPlayBackQuery);
        baseList.setCount(count);
        if (count > 0) {
            List<PlayBackPO> playBackPOS = playBackMapper.list(dbPlayBackQuery);
            List<PlayBackDTO> playBackDTOS = playBackPOS.stream().map(PlayBackDTO::new).collect(Collectors.toList());
            baseList.setItems(playBackDTOS);
        }
        return baseList;
    }



    /**
     * 根据ID删除回放
     *
     * @param id         回放ID
     * @return 是否成功
     */
    public boolean delete(Long id) {
        PlayBackPO po=PlayBackPO.builder().id(id).status(PlayBackRepo.STATUS_PLAYBACK_DEL).build();
        return playBackMapper.update(po,Utils.getCurrentMillis()) > 0;
    }




    public List<PlayBackDTO> list(DBPlayBackQuery dbPlayBackCoverQuery) {
        List<PlayBackPO> playBackPOS = playBackMapper.list(dbPlayBackCoverQuery);
        return playBackPOS.stream().map(PlayBackDTO::new).collect(Collectors.toList());
    }

    public int count(DBPlayBackQuery dbPlayBackQuery) {
        return playBackMapper.count(dbPlayBackQuery);
    }
}

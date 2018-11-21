package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.LivePureMapper;
import cn.idongjia.live.db.mybatis.po.LivePurePO;
import cn.idongjia.live.db.mybatis.query.DBLivePureQuery;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 纯直播repo类
 *
 * @author zhang
 */
@Component
public class LivePureRepo {

    private static final Log LOGGER = LogFactory.getLog(LivePureRepo.class);

    @Resource
    private LivePureMapper livePureMapper;


    /**
     * 根据纯直播ID获取纯直播信息
     *
     * @param pureLiveId 纯直播ID
     * @return 纯直播信息
     */
    public LivePureDTO getByLiveId(Long pureLiveId) {
        LivePurePO pureLivePO = livePureMapper.get(pureLiveId);
        return pureLivePO==null?null:new LivePureDTO(pureLivePO);
    }

    public List<LivePureDTO> list(DBLivePureQuery dbLivePureQuery) {
        List<LivePurePO> livePurePOS = livePureMapper.list(dbLivePureQuery);
        return livePurePOS.stream().map(LivePureDTO::new).collect(Collectors.toList());
    }

    public void update(LivePureDTO livePureDTO) {
        livePureMapper.update(livePureDTO.toDO());
    }


    public Integer countOpeningPureLiveByAnchor(Long anchorId){
        return livePureMapper.countOpeningPureLives(anchorId);
    }


    /**
     * 根据主播ID获取其回放lid
     *
     * @param anchorId 主播ID
     * @return 回放数量
     */
    public List<Long> listOverPureLiveByAnchor(Long anchorId) {
        return livePureMapper.searchOverPureLives(anchorId);
    }

}

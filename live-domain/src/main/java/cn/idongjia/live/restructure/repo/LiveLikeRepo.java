package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.AnchorBlackWhiteMapper;
import cn.idongjia.live.db.mybatis.mapper.LiveLikeMapper;
import cn.idongjia.live.db.mybatis.po.AnchorBlackWhitePO;
import cn.idongjia.live.db.mybatis.po.LiveLikePO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBlackWhiteQuery;
import cn.idongjia.live.restructure.dto.AnchorBlackWhiteDTO;
import cn.idongjia.live.restructure.dto.LiveLikeDTO;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午2:11
 */
@Component
public class LiveLikeRepo {

    @Resource
    private LiveLikeMapper liveLikeMapper;


    public void add(LiveLikeDTO dto){
        LiveLikePO liveLikePO = dto.toDO();

        LiveLikePO liveLikePO1 = liveLikeMapper.get(liveLikePO.getLiveId(), liveLikePO.getUserId());
        if(Objects.isNull(liveLikePO1)){
            liveLikeMapper.insert(liveLikePO);
        }else{
            update(dto);
        }
    }

    public boolean delete(LiveLikeDTO dto){
        LiveLikePO liveLikePO = dto.toDO();
        return liveLikeMapper.update(liveLikePO)>0;
    }

    public boolean update(LiveLikeDTO dto){
        LiveLikePO liveLikePO = dto.toDO();
        return liveLikeMapper.update(liveLikePO)>0;
    }

    public LiveLikeDTO get(Long liveId,Long userId){
        LiveLikePO liveLikePO = liveLikeMapper.get(liveId, userId);
        if(Objects.isNull(liveLikePO)){
            return null;
        }
        return new LiveLikeDTO(liveLikePO);
    }


}

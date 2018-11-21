package cn.idongjia.live.restructure.cache;

import cn.idongjia.live.db.mybatis.po.LiveAuctionSessionPO;
import cn.idongjia.live.restructure.redis.BaseRedis;
import cn.idongjia.live.support.redis.RedisKey;
import cn.idongjia.live.support.redis.RedisTools;
import cn.idongjia.util.Utils;
import org.mapdb.Atomic;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/27
 * Time: 下午3:50
 */
@Component
public class LiveAuctionSessionCache implements RedisKey {



    @Resource
    private RedisTools auctionRedisCache;

    private static final String BASE_KEY="outcry.VideoCoverIdDao.";

    public List<LiveAuctionSessionPO> batchGet(List<LiveAuctionSessionPO> pos){
        if(CollectionUtils.isEmpty(pos)){
            return new ArrayList<>();
        }
        List<String> keys=pos.stream().map(po->{
            return BASE_KEY+po.getSessionId();
        }).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(keys)){
            return new ArrayList<>();
        }

        Map<String, String> stringStringMap = auctionRedisCache.pipeGet(keys);
        if(Utils.isEmpty(stringStringMap)){
            return new ArrayList<>();
        }

        List<LiveAuctionSessionPO> pos1=pos.stream().map(po->{

            LiveAuctionSessionPO liveAuctionSessionPO=new LiveAuctionSessionPO();
            liveAuctionSessionPO.setLiveId(po.getLiveId());
            liveAuctionSessionPO.setSessionId(po.getSessionId());
            String videoCoverStr=stringStringMap.get(BASE_KEY+po.getSessionId());
            if(Objects.nonNull(videoCoverStr)){
                liveAuctionSessionPO.setVideoCoverId(Long.parseLong(videoCoverStr));
            }

            return liveAuctionSessionPO;

        }).collect(Collectors.toList());

        return pos1;


    }
}

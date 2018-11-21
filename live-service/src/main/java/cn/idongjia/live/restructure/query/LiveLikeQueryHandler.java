package cn.idongjia.live.restructure.query;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.api.live.pojo.LiveIndexSearch;
import cn.idongjia.live.api.live.pojo.LiveResp;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveRoomQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveVideoCoverQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.pojo.live.LiveShow4Article;
import cn.idongjia.live.pojo.live.LiveTypeConfig;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.query.live.LiveShowSearch;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.*;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.repo.LiveLikeRepo;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class LiveLikeQueryHandler {
    private static final Log logger = LogFactory.getLog(LiveLikeQueryHandler.class);


    @Resource
    private LiveLikeRepo liveLikeRepo;

    public LiveLikeDTO get(Long liveId,Long userId){
        return liveLikeRepo.get(liveId,userId);
    }



}

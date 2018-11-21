package cn.idongjia.live.db.mybatis.mapper;


import cn.idongjia.live.db.mybatis.po.AnchorBlackWhitePO;
import cn.idongjia.live.db.mybatis.po.LiveLikePO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBlackWhiteQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveLikeQuery;
import cn.idongjia.live.restructure.pojo.co.live.LiveLikeCO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * 订阅mapper
 * @author Frank
 */
public interface LiveLikeMapper {
    /**
     * 添加
     * @param liveLikePO
     */
    void insert(LiveLikePO liveLikePO);


    /**
     * 取消点赞
     * @param liveLikePO
     * @return
     */
    int update(LiveLikePO liveLikePO);

    List<LiveLikePO> list(DBLiveLikeQuery query);

    LiveLikePO get(@Param("liveId") Long liveId,@Param("userId") Long userId);

}

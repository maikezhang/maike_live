package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveBannerPO;
import cn.idongjia.live.db.mybatis.query.DBLiveBannerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by dongjia_lj on 17/3/9.
 */
public interface LiveBannerMapper {


    int insert(LiveBannerPO liveBannerPO);

    int update(LiveBannerPO liveBannerPO);

    List<LiveBannerPO> list(DBLiveBannerQuery dbLiveBannerQuery);

    Integer count(DBLiveBannerQuery dbLiveBannerQuery);
}

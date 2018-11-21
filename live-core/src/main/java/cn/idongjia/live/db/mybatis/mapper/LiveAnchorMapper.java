package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveAnchorPO;
import cn.idongjia.live.v2.pojo.query.LiveAnchorQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：直播主播表 db接口
 *
 * @author YueXiaodong, yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
public interface LiveAnchorMapper {

    int insert(LiveAnchorPO liveAnchorPO);

    int update(LiveAnchorPO liveAnchorPO);

    int deleteByPrimaryKey(@Param("id") Long id);

    LiveAnchorPO getByUserId(@Param("userId") Long userId);

    List<LiveAnchorPO> select(LiveAnchorQuery query);

    Integer count(LiveAnchorQuery query);

}

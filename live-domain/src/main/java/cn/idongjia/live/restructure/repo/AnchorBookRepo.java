package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.AnchorBookMapper;
import cn.idongjia.live.db.mybatis.po.AnchorBookPO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import cn.idongjia.live.restructure.dto.AnchorBookDTO;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午9:41
 */
@Repository
public class AnchorBookRepo {
    @Resource
    private AnchorBookMapper anchorsBookMapper;

    public List<AnchorBookDTO> list(DBAnchorBookQuery dbLiveBookQuery) {
        List<AnchorBookPO> anchorsBookPOS = anchorsBookMapper.list(dbLiveBookQuery);
        return anchorsBookPOS.stream().map(AnchorBookDTO::new).collect(Collectors.toList());
    }

    public Integer count(DBAnchorBookQuery dbAnchorBookQuery) {
        return anchorsBookMapper.count(dbAnchorBookQuery);
    }

    /**
     * 添加主播订阅关系
     * @author zhangyingjie
     * @param anchorsBookPO 主播订阅关系
     * @return 主键id
     */
    public Long addAnchorBook(AnchorBookPO anchorsBookPO){
        anchorsBookMapper.insert(anchorsBookPO);
        return anchorsBookPO.getId();


    }

    /**
     * 获取单个主播订阅关系
     * @param uid 用户id
     * @param anchorId 主播id
     * @return 主播订阅数据
     */
    public AnchorBookPO getByUidAnchorId(Long uid,Long anchorId){
        return anchorsBookMapper.getByUidAnchorId(uid,anchorId);
    }


    /**
     * 更新主播订阅关系
     * @param po 主播订阅数据
     * @return true or false
     */
    public boolean updateAnchorBook(AnchorBookPO po){
        return  anchorsBookMapper.update(po, Utils.getCurrentMillis())>0;
    }

}

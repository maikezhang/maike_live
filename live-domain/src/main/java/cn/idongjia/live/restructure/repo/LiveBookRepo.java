package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.AnchorBookMapper;
import cn.idongjia.live.db.mybatis.mapper.LiveBookMapper;
import cn.idongjia.live.db.mybatis.po.LiveBookCountPO;
import cn.idongjia.live.db.mybatis.po.LiveBookPO;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import cn.idongjia.live.restructure.dto.LiveBookDTO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.DateTimeUtil.millis2Timestamp;

@Component
public class LiveBookRepo {

    private static final Log LOGGER = LogFactory.getLog(LiveBookRepo.class);
    @Resource
    private LiveBookMapper liveBookMapper;
    @Resource
    private AnchorBookMapper anchorsBookMapper;


    @Resource
    private UserManager userManager;
    @Resource
    private ConfigManager configManager;


    public List<LiveBookDTO> list(DBLiveBookQuery dbLiveBookQuery) {
        List<LiveBookPO> liveBookPOS = liveBookMapper.list(dbLiveBookQuery);
        return liveBookPOS.stream().map(LiveBookDTO::new).collect(Collectors.toList());
    }


    /**
     * @param dbLiveBookQuery
     * @return 订阅数量
     */
    public Integer count(DBLiveBookQuery dbLiveBookQuery) {
        return liveBookMapper.count(dbLiveBookQuery);
    }


    /**
     * 增加用户订阅纯直播关系
     *
     * @param liveBookDTO
     * @return 订阅ID
     */
    public Long addPureLiveBook(LiveBookDTO liveBookDTO) {
        liveBookDTO.setCreateTime(millis2Timestamp(Utils.getCurrentMillis()));
        LiveBookPO liveBookPO = liveBookDTO.toDO();
        int insertCount = liveBookMapper.insert(liveBookPO);
        if (insertCount < 1) {
            throw LiveException.failure("订阅失败");
        }
        return liveBookPO.getId();
    }

    /**
     * 批量添加直播订阅
     * @param liveBookPOS
     */
    public void batchAddLiveBook(List<LiveBookPO> liveBookPOS){
        liveBookMapper.batchAddLiveBook(liveBookPOS);
    }

    /**
     * 批量删除直播订阅
     * @param liveBookPOS
     */
    public void batchDeleteLiveBook(List<LiveBookPO> liveBookPOS){

        Long currentTime=Utils.getCurrentMillis();
        liveBookPOS.forEach(liveBookPO -> {
            liveBookMapper.update(liveBookPO,currentTime);

        });
//        liveBookMapper.batchDeletePureLiveBook(liveBookPOS,Utils.getCurrentMillis());
    }

    /**
     * 删除纯直播订阅
     *
     * @param id 纯直播订阅关系数据
     * @return 是否成功
     */
    public boolean delete(Long id) {
        DBLiveBookQuery query=DBLiveBookQuery.builder().id(id).build();
        List<LiveBookPO> liveBookPOS = liveBookMapper.list(query);
        if (CollectionUtils.isEmpty(liveBookPOS)|| liveBookPOS.get(0).getStatus() == LiveConst.STATUS_BOOK_DEL) {
            return true;
        }
        liveBookPOS.get(0).setStatus(LiveConst.STATUS_BOOK_DEL);
        return liveBookMapper.update(liveBookPOS.get(0), Utils.getCurrentMillis()) == 1;
    }

    /**
     * 更新纯直播订阅
     *
     * @param liveBookDTO 纯直播订阅关系
     * @return 是否成功
     */
    public boolean update(LiveBookDTO liveBookDTO) {
        liveBookDTO.setModifiedTime(millis2Timestamp(Utils.getCurrentMillis()));
        return liveBookMapper.update(liveBookDTO.toDO(), Utils.getCurrentMillis()) > 0;
    }


    public List<LiveBookCountPO> countMap( final  List<Long> liveIds) {
        return liveBookMapper.countGroup(liveIds);
    }
}

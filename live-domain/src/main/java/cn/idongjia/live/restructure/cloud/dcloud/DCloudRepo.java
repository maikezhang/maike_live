package cn.idongjia.live.restructure.cloud.dcloud;

import cn.idongjia.live.db.mybatis.mapper.DLiveCloudMapper;
import cn.idongjia.live.db.mybatis.po.DLiveCloudPO;
import cn.idongjia.live.restructure.dto.DLiveCloudDTO;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("dCloudRepo")
public class DCloudRepo {

    @Resource
    private DLiveCloudMapper dLiveCloudMapper;

    public Long addDLiveCloud(DLiveCloudDTO dLiveCloudDTO){
        DLiveCloudPO po=dLiveCloudDTO.toDO();
        dLiveCloudMapper.insert(po);
        return po.getId();
    }
//
//    boolean updateDLiveCloud(Long uid, LivePullUrl livePullUrl){
//        DLiveCloudDO dLiveCloudDO = new DLiveCloudDO();
//        dLiveCloudDO.setUid(uid);
//        dLiveCloudDO.setRtmpUrl(livePullUrl.getRtmpUrl());
//        dLiveCloudDO.setHlsUrl(livePullUrl.getHlsUrl());
//        dLiveCloudDO.setFlvUrl(livePullUrl.getFlvUrl());
//        dLiveCloudDO.setModifiedTm(millis2Timestamp(getCurrentMillis()));
//        return dLiveCloudMapper.update(dLiveCloudDO) > 0;
//    }

    public boolean updateDLiveCloud(DLiveCloudDTO dLiveCloudDTO){

        return dLiveCloudMapper.update(dLiveCloudDTO.toDO(), Utils.getCurrentMillis()) > 0;
    }

    public DLiveCloudDTO getPullUrlsByUid(Long uid){
        DLiveCloudPO dLiveCloudFromDB = getDLiveCloudFromDB(uid).toDO();

        return new DLiveCloudDTO(dLiveCloudFromDB);
    }

//    LivePullUrl getPullUrlsById(String id){
//        Long idLong = Long.parseLong(id);
//        DLiveCloudDO dLiveCloudDO = dLiveCloudMapper.getById(idLong);
//        LivePullUrl livePullUrl = new LivePullUrl();
//        livePullUrl.setRtmpUrl(dLiveCloudDO.getRtmpUrl());
//        livePullUrl.setHlsUrl(dLiveCloudDO.getHlsUrl());
//        livePullUrl.setFlvUrl(dLiveCloudDO.getFlvUrl());
//        return livePullUrl;
//    }

    public DLiveCloudDTO getPullUrlsById(String id){
        Long idLong = Long.parseLong(id);
        DLiveCloudDTO dLiveCloudDTO =new DLiveCloudDTO(dLiveCloudMapper.getById(idLong));

        return dLiveCloudDTO;
    }

    public boolean updateDLiveCloudById( DLiveCloudDTO dLiveCloudDTO){
        return dLiveCloudMapper.update(dLiveCloudDTO.toDO(),Utils.getCurrentMillis()) > 0;
    }

    private DLiveCloudDTO getDLiveCloudFromDB(Long uid){
        return new DLiveCloudDTO(dLiveCloudMapper.getByUid(uid));
    }
}

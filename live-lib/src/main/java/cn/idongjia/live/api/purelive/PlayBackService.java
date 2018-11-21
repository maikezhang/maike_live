package cn.idongjia.live.api.purelive;

import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.playback.PlayBackAdmin;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDO;
import cn.idongjia.live.query.purelive.PlayBackSearch;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDO;
import cn.idongjia.live.query.purelive.PlayBackSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("playback")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface PlayBackService {

    /**
     * 增加回放信息
     * @param playBackDO 回放信息
     * @return 回放信息ID
     */
    @POST
    Long addPlayBack(PlayBackDO playBackDO);

    /**
     * 根据直播ID获取对应的回放信息
     * @param lid 直播ID
     * @return 回放信息
     */
    @Path("{lid:\\d+}")
    @GET
    List<PlayBackDO> getPlayBackByLid(Long lid);

    /**
     * 根据直播id批量获取回放
     * @param search liveIds需要获取回放的直播id列表
     * @return
     */
    @POST
    @Path("batch/get")
    List<List<PlayBackDO>> getBatchPlayBackByLiveId(PlayBackSearch search);

    /**
     * 删除对应的回放信息
     * @param playBackDO 回放信息
     * @return 是否成功
     */
    @DELETE
    boolean deletePlayBack(PlayBackDO playBackDO);

    /**
     * 更新对应的回放信息
     * @param playBackDO 回放信息
     * @return 是否成功
     */
    @PUT
    boolean updatePlayBack(PlayBackDO playBackDO);

    /**
     * 根据检索条件获取列表
     * @param playBackSearch 检索条件
     * @return 回放列表
     */
    @GET
    BaseList<PlayBackAdmin> listPlayBackWithCount(PlayBackSearch playBackSearch);

    /**
     * 根据回放ID更新
     * @param id 回放ID
     * @return 是否成功
     */
    @Path("{id:\\d+}")
    @PUT
    boolean deletePlayBack(@PathParam("id") Long id, PlayBackDO playBackDO);
}

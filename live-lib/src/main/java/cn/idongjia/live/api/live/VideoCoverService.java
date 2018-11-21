package cn.idongjia.live.api.live;

import cn.idongjia.live.pojo.live.LiveVideoCover;
import cn.idongjia.live.pojo.live.LiveVideoCover;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;

/**
 * 短视频接口地址
 * Created by 岳晓东 on 2017/12/22.
 */
@Path("videoCover")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface VideoCoverService {

    /**
     * 添加
     * @param id
     * @return
     */
    @Path("get/{id:\\d+}")
    @POST
    LiveVideoCover get(@PathParam("id") Long id);

    /**
     * 添加
     *
     * @return
     */
    @Path("add")
    @POST
    Long add(LiveVideoCover cover);

    /**
     * 修改
     *
     * @param cover
     */
    @Path("update")
    @POST
    void update(LiveVideoCover cover);

}

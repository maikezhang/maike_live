package cn.idongjia.live.api.live;

import cn.idongjia.live.restructure.pojo.cmd.LiveLikeAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.LiveLikeDelCmd;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;

/**
 * Created by zhangmaike on 2018/9/3.
 */
@Path("like")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface LiveLikeServiceI {

    @POST
    @Path("add")
    void addLike(@BeanParam LiveLikeAddCmd cmd);



    @POST
    @Path("delete")
    boolean deleteLike(@BeanParam LiveLikeDelCmd cmd);
}

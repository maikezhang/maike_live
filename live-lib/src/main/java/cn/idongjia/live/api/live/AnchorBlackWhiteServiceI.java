package cn.idongjia.live.api.live;

import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteUpdateCmd;
import cn.idongjia.live.restructure.pojo.co.live.AnchorBlackWhiteCO;
import cn.idongjia.live.restructure.pojo.query.AnchorBlackWhiteQry;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by zhangmaike on 2018/9/2.
 */
@Path("AnchorBW")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface AnchorBlackWhiteServiceI {

    @POST
    @Path("add")
    void add(@BeanParam AnchorBlackWhiteAddCmd cmd);

    @POST
    @Path("update")
    Long update(@BeanParam AnchorBlackWhiteUpdateCmd cmd);

    @POST
    @Path("delete")
    boolean delete(@BeanParam AnchorBlackWhiteDelCmd cmd);

    @GET
    @Path("page")
    MultiResponse<AnchorBlackWhiteCO> page(@BeanParam AnchorBlackWhiteQry qry);









}

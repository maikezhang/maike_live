package cn.idongjia.live.api.purelive;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * 资源
 * @author dongjia_lj
 */
@Path("resource")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface ResourceService {

    /**
     * 设置（取消）主推
     * @param lid   直播id
     * @param rid   资源id
     * @param rtype     资源类型
     * @param status    主推状态 1：推 2：取消
     * @return
     */
    @Path("main/{lid}/{rid}/{rtype}/{status}")
    @POST
    public boolean mainResource(@PathParam("lid") long lid,@PathParam("rid") long rid,@PathParam
            ("rtype") int rtype,@PathParam("status") int status);


    /**
     * 获取主推
     * @param lid
     * @param rtype
     * @return
     */
    @Path("{lid}/{rtype}")
    @GET
    public Long mainResource(@PathParam("lid") long lid,@PathParam("rtype") int rtype);
}

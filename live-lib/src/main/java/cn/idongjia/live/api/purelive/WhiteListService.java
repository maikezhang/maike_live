package cn.idongjia.live.api.purelive;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("whitelist")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface WhiteListService {

    /**
     * 更新白名单
     * @param uids 主播ID列表
     * @return 是否成功
     */
    @POST
    boolean addAnchors(List<Long> uids);

    /**
     * 列出白名单用户
     * @return 用户列表
     */
    @GET
    List<Long> listWhitList();

    /**
     * 删除白名单中uid
     * @param uid 被删除的uid
     * @return 是否成功
     */
    @Path("{uid:\\d+}")
    @DELETE
    boolean deleteAnchor(@PathParam("uid") Long uid);

    /**
     * 增加一个白名单uid
     * @param uid 被增加的uid
     * @return 是否成功
     */
    @Path("{uid:\\d+}")
    @POST
    boolean addAnchor(@PathParam("uid") Long uid);

}

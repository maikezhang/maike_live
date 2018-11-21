package cn.idongjia.live.api.purelive;

import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.pojo.user.Anchor;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("hot")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface HotAnchorsService {

    /**
     * 增加热门主播
     * @param uids 主播ID列表
     * @return 是否成功
     */
    @POST
    boolean addHotAnchors(List<Long> uids);

    /**
     * 列出热门主播列表
     * @return 主播列表
     */
    @GET
    List<Long> listHotAnchors();

    /**
     * 根据uid随机推荐主播
     * @param uid 用户ID
     * @return 主播列表
     */
    @Path("random")
    @GET
    List<Anchor> listHotAnchorsRandomly(Long uid);

    /**
     * 删除推荐主播uid
     * @param uid 被删除的uid
     * @return 是否成功
     */
    @Path("{uid:\\d+}")
    @DELETE
    boolean deleteHotAnchor(@PathParam("uid") Long uid);

    /**
     * 增加一个主播
     * @param uid 主播ID
     * @return 是否成功
     */
    @Path("{uid:\\d+}")
    @POST
    boolean addHotAnchor(@PathParam("uid") Long uid);

}

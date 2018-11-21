package cn.idongjia.live.api;

import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.api.live.pojo.response.SingleResponse;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabUpdateCmd;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabCO;
import cn.idongjia.live.restructure.pojo.query.PageTabQry;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * 页面tab服务
 *
 * @author lc on 2018/7/6.
 * @class cn.idongjia.live.api.PageTabService
 */
@Path("pageTab")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface PageTabServiceI {

    /**
     * 添加分页tab
     *
     * @param pageTabAddCmd
     * @return 添加id
     */
    @Path("add")
    @PUT
    public SingleResponse<Long> add(PageTabAddCmd pageTabAddCmd);

    /**
     * 修改分页tab
     *
     * @param pageTabUpdateCmd
     * @return 添加id
     */
    @Path("update")
    @PUT
    public SingleResponse<Integer> update(PageTabUpdateCmd pageTabUpdateCmd);


    /**
     * 删除分页
     *
     * @param pageTabDelCmd
     * @return 添加id
     */
    @Path("delete")
    @DELETE
    public SingleResponse delete(PageTabDelCmd pageTabDelCmd);

    /**
     * 获取tab详情
     * @param id
     * @return
     */
    @Path("get")
    @GET
    public SingleResponse<PageTabCO> get(long id);

    /**
     * 获取分页数据
     *
     * @param pageTabPageCO
     * @return
     */
    @Path("page")
    @GET
    public MultiResponse<PageTabCO> page(@BeanParam PageTabQry pageTabPageCO);

    @Path("getTabForApi")
    @GET
    public List<PageTabCO> getTabForApi(@BeanParam PageTabQry qry);
}

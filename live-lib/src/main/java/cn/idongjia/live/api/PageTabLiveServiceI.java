package cn.idongjia.live.api;

import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.api.live.pojo.response.SingleResponse;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabLiveAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabLiveDeleteCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabLiveUpdateWeightCmd;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabLiveCO;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveApiQry;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveQry;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author lc on 2018/7/8.
 * @class cn.idongjia.live.api.PageTabLiveServiceI
 */
@Path("pageTabLive")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface PageTabLiveServiceI {
    /**
     * 添加tab关联直播
     *
     * @param addCmd
     * @return
     */
    @Path("add")
    @PUT
    public SingleResponse<Integer> add(@BeanParam PageTabLiveAddCmd addCmd);


    /**
     * 添加tab关联直播
     *
     * @param addCmd
     * @return
     */
    @Path("add")
    @PUT
    public SingleResponse<Integer> addByAsidLiveId( @BeanParam PageTabLiveAddCmd addCmd);

    /**
     * 更新tab关联直播权重
     *
     * @param updateWeightCmd
     * @return
     */
    @Path("updateWeight")
    @PUT
    public SingleResponse<Integer> updateWeight(@BeanParam PageTabLiveUpdateWeightCmd updateWeightCmd);


    /**
     * 删除tab关联直播
     *
     * @param deleteCmd
     * @return
     */
    @Path("delete")
    @DELETE
    public SingleResponse<Integer> delete(@BeanParam PageTabLiveDeleteCmd deleteCmd);


    /**
     * tab关联直播查询
     *
     * @param pageTabLiveQry
     * @return
     */
    @Path("page")
    @GET
    public MultiResponse<PageTabLiveCO> page(@BeanParam PageTabLiveQry pageTabLiveQry);

    /**
     * api查询数据
     *
     * @param pageTabLiveApiQry
     * @return
     */
    @Path("pageApi")
    @GET
    public MultiResponse<LiveCO> pageApi(@BeanParam PageTabLiveApiQry pageTabLiveApiQry);



}

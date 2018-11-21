package cn.idongjia.live.api.live;

import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.query.live.LiveModuleSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * @author dongjia_lj
 */
@Path("module")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface ModuleService {
    /**
     * 新增拍卖模块资源
     * @param liveModule 模块资源
     * @return 资源ID
     */
    @POST
    @Path("add")
    Long createModule(LiveModule liveModule);

    /**
     * 根据ID和资源内容更新资源
     * @param id 资源ID
     * @param liveModule 资源内容
     * @return 是否成功
     */
    @PUT
    @Path("{mid:\\d+}")
    boolean modifyModule(@PathParam("mid") Long id,LiveModule liveModule);

    /**
     * 根据ID删除资源
     * @param id 资源ID
     * @return 是否成功
     */
    @DELETE
    @Path("{mid:\\d+}")
    boolean deleteModule(@PathParam("mid") Long mid);

    /**
     * 根据ID获取对应资源
     * @param id 资源ID
     * @return 资源消息
     */
    @GET
    @Path("{mid:\\d+}")
    LiveModule getModuleById(@PathParam("mid") Long id);

    /**
     * 根据模块样式获取对应的进行中资源列表
     * @param style 样式
     * @return 资源列表
     */
    @GET
    @Path("module/{style:\\d+}")
    List<LiveModule> listModuleGroupByStyle(@PathParam("style") Integer style);

    /**
     * 根据模块样式上架模块
     * @param style 样式
     * @return 是否成功
     */
    @GET
    @Path("module/{style:\\d+}/on")
    boolean onShelfModuleGroup(@PathParam("style") Integer style);

    /**
     * 根据位置获取对应资源列表
     * @param position 位置
     * @return 资源列表
     */
    @GET
    @Path("position/{pos:\\d+}")
    List<LiveModule> listLiveModuleByPosition(@PathParam("pos") Integer position
            , @BeanParam LiveModuleSearch search);

    /**
     * 获取上线模块资源列表
     * @return 模块资源列表
     */
    @GET
    @Path("module")
    List<LiveModule> listModuleGroup();

    /**
     * 根据位置和开始时间判断是否有重复的开始时间
     * @param position 位置
     * @param startTime 开始时间
     * @return 是否
     */
    @Path("pos/{pos:\\d+}/start/{start:\\d+}/duplicate")
    @GET
    boolean isDuplicateStartTime(@PathParam("pos") Integer position,@PathParam("start") Long startTime);

    /**
     * 根据位置获取对应资源列表
     * @param position 位置
     * @return 资源列表
     */
    @GET
    @Path("count/position/{pos:\\d+}")
    int countLiveModuleByPosition(@PathParam("pos") Integer position
            , @BeanParam LiveModuleSearch search);
}

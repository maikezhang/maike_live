package cn.idongjia.live.v2.service;

import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.v2.pojo.LiveAnchorBan;
import cn.idongjia.live.v2.pojo.LiveAnchorBanRecord;
import cn.idongjia.live.v2.pojo.query.LiveAnchorBanRecordQuery;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 主播先关接口
 *
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
@Path("anchor")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface LiveAnchorService {


    /**
     * 匠人禁播
     *
     * @param anchorBan
     */
    @Path("ban")
    @POST
    void banAnchor(LiveAnchorBan anchorBan);

    /**
     * 匠人禁播记录列表
     *
     * @param query
     * @return
     */
    @Path("listBannedRecord")
    @POST
    BaseList<LiveAnchorBanRecord> listBannedRecord(LiveAnchorBanRecordQuery query);

}

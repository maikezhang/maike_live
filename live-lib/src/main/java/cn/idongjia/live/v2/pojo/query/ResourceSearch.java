package cn.idongjia.live.v2.pojo.query;

import cn.idongjia.common.query.BaseSearch;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.ws.rs.QueryParam;

/**
 * Created by YueXiaodong on 2018/01/23.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceSearch extends BaseSearch {

    @QueryParam("id")
    private Long id;
    @QueryParam("resourceId")
    private Long resourceId;
    @QueryParam("resourceType")
    private Integer resourceType;

    @QueryParam("liveId")
    private Long lid;
    @QueryParam("userId")
    private Long craftsUid; //匠人id
    @QueryParam("title")
    private String title; //标题

}

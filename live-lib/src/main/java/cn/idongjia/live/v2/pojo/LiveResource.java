package cn.idongjia.live.v2.pojo;

import cn.idongjia.common.base.Base;
import cn.idongjia.live.exception.LiveException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Objects;

/**
 * 直播资源
 * @author zhang created on 2018/1/17 下午12:57
 * @version 1.0
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveResource extends Base {

    /**
     * 资源id
     */
    private Long id;
    /**
     * 资源id
     */
    private Long resourceId;
    /**
     * 资源类型
     */

    private Integer resourceType;
    /**
     * 标题
     */

    private String title;


    public LiveResource(){}

    public LiveResource(Long resourceId, Integer resourceType) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    public void setResourceId(Long resourceId){
        if (Objects.isNull(resourceId)){
            throw LiveException.failure("资源id不能为空");
        }
        this.resourceId = resourceId;
    }

    public void setResourceType(Integer resourceType){
        if (Objects.isNull(resourceType)){
            throw LiveException.failure("资源类型不能为空");
        }
        this.resourceType = resourceType;
    }
}

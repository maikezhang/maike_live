package cn.idongjia.live.restructure.pojo.co;

import cn.idongjia.live.restructure.pojo.ClientObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor

public class UserStageLiveCO extends ClientObject {

    /**
     * 老用户强运营数据
     */
    private List<Long> oldStageLiveIds;

    /**
     * 新用户强运营数据
     */

    private List<Long> newStageLiveIds;
}

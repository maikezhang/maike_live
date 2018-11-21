package cn.idongjia.live.db.mybatis.query;

import cn.idongjia.common.base.Base;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseQuery extends Base {
    private String  orderBy;
    private Integer limit;
    private Integer page;
    private Long    beginTime;
    private Long    endTime;
    private Integer offset;

    public Integer getOffset() {
        if(null == offset && page != null && limit != null) {
            offset = (page - 1) * limit;
        }
        return offset;
    }

}


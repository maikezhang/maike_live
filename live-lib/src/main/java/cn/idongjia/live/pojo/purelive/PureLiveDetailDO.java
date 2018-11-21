package cn.idongjia.live.pojo.purelive;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 *
 * @author zhang
 * @date 2017/2/19
 * 纯直播详情表对应DO
 */
@Getter
@Setter
@ToString
public class PureLiveDetailDO extends Base {

    private static final long serialVersionUID = -7355886717867426273L;

    /**
     * 纯直播详情ID
     */
    private Long id;
    /**
     * 资源ID
     */
    private Long resourceId;
    /**
     * 资源类型
     */
    private Integer resourceType;
    /**
     * 详情权重
     */
    private Integer weight;
    /**
     * 详情状态
     */
    private Integer status;
    /**
     * 对应纯直播ID
     */
    private Long lid;
    /**
     * 详情创建时间
     */
    private Timestamp createTm;
    /**
     * 最后更新时间
     */
    private Timestamp modifiedTm;

    @Override
    public int hashCode(){
        int result = this.resourceId.hashCode();
        return 31 * result + this.resourceType.hashCode();
    }

    @Override
    public boolean equals(Object detailDO){
        if (detailDO instanceof PureLiveDetailDO){
            PureLiveDetailDO pureLiveDetailDO = (PureLiveDetailDO) detailDO;
            return pureLiveDetailDO.getResourceId().equals(this.getResourceId())
                    && pureLiveDetailDO.getResourceType().equals(this.getResourceType());
        }
        else{
            return super.equals(detailDO);
        }
    }
}

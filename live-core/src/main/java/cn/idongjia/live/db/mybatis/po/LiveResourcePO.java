package cn.idongjia.live.db.mybatis.po;

import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 直播资源
 *
 * @author lc
 * @create at 2018/6/11.
 */
@Getter
@Setter
public class LiveResourcePO extends BasePO {
    /**
     * 资源id
     */
    private Long id;

    /**
     * 直播id
     */
    private Long liveId;

    /**
     * 资源id
     */
    private Long resourceId;
    /**
     * 资源类型
     */
    private Integer resourceType;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long modifiedTime;

    @Override
    public int hashCode(){
        int result = this.resourceId.hashCode();
        return 31 * result + this.resourceType.hashCode();
    }

    @Override
    public boolean equals(Object resourcePO){
        if (resourcePO instanceof LiveResourcePO){
            LiveResourcePO liveResourcePO = (LiveResourcePO) resourcePO;
            return liveResourcePO.getResourceId().equals(this.getResourceId())
                    && liveResourcePO.getResourceType().equals(this.getResourceType());
        }
        else{
            return super.equals(resourcePO);
        }
    }
}

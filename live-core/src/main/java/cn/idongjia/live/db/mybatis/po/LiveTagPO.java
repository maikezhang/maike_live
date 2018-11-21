package cn.idongjia.live.db.mybatis.po;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播tag数据表类
 */
@Getter
@Setter
public class LiveTagPO extends BasePO {

    /**
     * 唯一ID
     */
    private Long id;

    /**
     * tag名字
     */
    private String name;
    /**
     * tag类型
     */
    private Integer type;
    /**
     * tag图片
     */
    private String pic;
    /**
     * tag描述
     */
    private String desc;
    /**
     * tag状态-1、删除0、正常
     */
    private Integer status;
    /**
     * tag权重
     */
    private Integer weight;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后修改时间
     */
    private Timestamp modifiedTime;
    public LiveTagPO(){

    }

    @Builder
    public LiveTagPO(Long id, String name, Integer type, String pic, String desc, Integer status, Integer weight, Timestamp createTime, Timestamp modifiedTime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.pic = pic;
        this.desc = desc;
        this.status = status;
        this.weight = weight;
        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
    }

}

package cn.idongjia.live.pojo.purelive.tag;

import cn.idongjia.common.base.Base;

import java.sql.Timestamp;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播tag数据表类
 */
public class PureLiveTagDO extends Base{
    private static final long serialVersionUID = 6416363190831221345L;

    //唯一ID
    private Long id;
    //tag名字
    private String name;
    //tag类型
    private Integer type;
    //tag图片
    private String pic;
    //tag描述
    private String desc;
    //tag状态-1、删除0、正常
    private Integer status;
    //tag权重
    private Integer weight;
    //创建时间
    private Timestamp createTm;
    //最后修改时间
    private Timestamp modifiedTm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Timestamp getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Timestamp createTm) {
        this.createTm = createTm;
    }

    public Timestamp getModifiedTm() {
        return modifiedTm;
    }

    public void setModifiedTm(Timestamp modifiedTm) {
        this.modifiedTm = modifiedTm;
    }

    public PureLiveTagDO(){}
}

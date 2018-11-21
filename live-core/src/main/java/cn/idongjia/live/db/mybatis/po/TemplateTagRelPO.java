package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class TemplateTagRelPO extends BasePO {

    /**
     * 唯一ID
     */
    private Long id;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 超级模版url
     */
    private String url;
    /**
     * 关联状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后修改时间
     */
    private Timestamp modifiedTime;


}

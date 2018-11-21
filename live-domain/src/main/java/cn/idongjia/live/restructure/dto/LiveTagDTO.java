package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveTagPO;
import cn.idongjia.live.pojo.purelive.tag.ColumnTag;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagDO;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagRelDO;
import cn.idongjia.live.pojo.purelive.tag.TemplateTagRelDO;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/13.
 */
public class LiveTagDTO extends BaseDTO<LiveTagPO> {
    public LiveTagDTO(LiveTagPO entity) {
        super(entity);
    }

    public static LiveTagDTO assembleFromPureLiveTagDO(PureLiveTagDO pureLiveTagDO) {
        LiveTagPO liveTagPO = LiveTagPO.builder()
                .createTime(pureLiveTagDO.getCreateTm())
                .desc(pureLiveTagDO.getDesc())
                .id(pureLiveTagDO.getId())
                .modifiedTime(pureLiveTagDO.getModifiedTm())
                .name(pureLiveTagDO.getName())
                .pic(pureLiveTagDO.getPic())
                .status(pureLiveTagDO.getStatus())
                .type(pureLiveTagDO.getType())
                .weight(pureLiveTagDO.getWeight())
                .build();
        return new LiveTagDTO(liveTagPO);
    }

    public Long getId() {
        return entity.getId();
    }

    public String getName() {
        return entity.getName();
    }

    public Integer getType() {
        return entity.getType();
    }

    public String getPic() {
        return entity.getPic();
    }

    public String getDesc() {
        return entity.getDesc();
    }

    public Integer getStatus() {
        return entity.getStatus();

    }

    public Integer getWeight() {
        return entity.getWeight();
    }

    public Timestamp getCreateTime() {
        return entity.getCreateTime();

    }

    public Timestamp getModifiedTime() {

        return entity.getModifiedTime();
    }


    public PureLiveTagDO assemblePureLiveTagDO() {
        PureLiveTagDO pureLiveTagDO = new PureLiveTagDO();
        pureLiveTagDO.setCreateTm(entity.getCreateTime());
        pureLiveTagDO.setDesc(entity.getDesc());
        pureLiveTagDO.setId(entity.getId());
        pureLiveTagDO.setModifiedTm(entity.getModifiedTime());
        pureLiveTagDO.setName(entity.getName());
        pureLiveTagDO.setPic(entity.getPic());
        pureLiveTagDO.setStatus(entity.getStatus());
        pureLiveTagDO.setType(entity.getType());
        pureLiveTagDO.setWeight(entity.getWeight());
        return pureLiveTagDO;
    }


    public TemplateTagRelDO assemblePureLiveTagDTO(TemplateTagRelDTO templateTagRelDTO) {
        TemplateTagRelDO templateTagRelDO = new TemplateTagRelDO();
        templateTagRelDO.setCreateTm(entity.getCreateTime());
        templateTagRelDO.setId(entity.getId());
        templateTagRelDO.setModifiedTm(entity.getModifiedTime());
        templateTagRelDO.setStatus(entity.getStatus());
        templateTagRelDO.setTagId(entity.getId());
        if(templateTagRelDTO!=null){
            templateTagRelDO.setUrl(templateTagRelDTO.getUrl());

        }
        return templateTagRelDO;
    }


    /**
     * 根据tagDO组装专栏信息
     */
    public ColumnTag assembleColumnTag(TemplateTagRelDTO templateTagRelDTO) {
        ColumnTag columnTag = new ColumnTag();
        // 组装基本信息
        columnTag.setId(entity.getId());
        columnTag.setName(entity.getName());
        columnTag.setType(entity.getType());
        columnTag.setPic(entity.getPic());
        columnTag.setDesc(entity.getDesc());
        columnTag.setStatus(entity.getStatus());
        columnTag.setWeight(entity.getWeight());
        columnTag.setCreateTm(entity.getCreateTime());
        columnTag.setModifiedTm(entity.getModifiedTime());
        // 组装超级模版地址
        columnTag.setUrl(templateTagRelDTO==null?null:templateTagRelDTO.getUrl());
        return columnTag;
    }

    public void setWeight(Integer weight) {
        entity.setWeight(weight);
    }

    public void setStatus(int status) {
        entity.setStatus(status);
    }

    public void setCreateTime(Timestamp timestamp) {
        entity.setCreateTime(timestamp);
    }

    public void setId(Long tagId) {
        entity.setId(tagId);
    }

    public void setModifiedTime(Timestamp timestamp) {
        entity.setModifiedTime(timestamp);
    }
}

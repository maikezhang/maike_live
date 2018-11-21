package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.TemplateTagRelPO;
import cn.idongjia.live.db.mybatis.query.DBTemplateRelQuery;
import cn.idongjia.live.pojo.purelive.tag.TemplateTagRelDO;
import cn.idongjia.live.query.purelive.tag.TemplateTagRelSearch;
import cn.idongjia.live.pojo.purelive.tag.TemplateTagRelDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TemplateTagRelMapper {

    int insert(TemplateTagRelPO templateTagRelPO);
    int update(TemplateTagRelPO templateTagRelPO, @Param("newUpdateTime") long newUpdateTime);
    List<TemplateTagRelPO> list(DBTemplateRelQuery dbTemplateRelQuery);
}

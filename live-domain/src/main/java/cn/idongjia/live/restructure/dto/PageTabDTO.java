package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.PageTabPO;

/**
 * 页面tab DTO
 *
 * @author lc
 * @create at 2018/7/6.
 */
public class PageTabDTO extends BaseDTO<PageTabPO> {
    public PageTabDTO(PageTabPO entity) {
        super(entity);
    }


    public void setId(Long id) {
        entity.setId(id);
    }
}

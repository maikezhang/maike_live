package cn.idongjia.live.restructure.domain.entity.tab;

import cn.idongjia.live.restructure.domain.entity.Entity;
import cn.idongjia.live.restructure.repo.PageTabLiveRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/8.
 */
@Getter
@Setter
public class PageTabLiveE extends Entity {

    private final PageTabLiveRepo pageTabLiveRepo = SpringUtils.takeBean("pageTabLiveRepo", PageTabLiveRepo.class);

    /**
     * id
     */
    private Long id;



    /**
     * 直播id
     */
    private Long liveId;
    /**
     * 权重
     */
    private int  weight;

    /**
     * 显示状态
     */
    private BaseEnum.YesOrNo showStatus;

    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;

    public Integer edit() {
        return pageTabLiveRepo.edit(this);
    }


}

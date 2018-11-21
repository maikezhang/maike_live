package cn.idongjia.live.restructure.domain.entity.tab;

import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.cache.liveHomePage.HPTabCache;
import cn.idongjia.live.restructure.domain.entity.Entity;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.SearchManager;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabLiveAddCmd;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.restructure.repo.PageTabLiveRepo;
import cn.idongjia.live.restructure.repo.PageTabRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.util.Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/7.
 */

public class PageTabE extends Entity {
    /**
     * id
     */
    @Getter
    @Setter
    private Long id;

    /**
     * tab名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * tab权重
     */
    @Getter
    @Setter
    private Integer weight;


    /**
     * tab类型
     */
    @Getter
    @Setter
    private LiveEnum.TabType type;

    /**
     * 数据状态 1 正常 -1删除
     */
    @Getter
    @Setter
    private LiveEnum.TabStatus status;

    /**
     * 创建时间
     */
    @Getter
    @Setter
    private Long createTime;

    /**
     * 更新时间
     */
    @Getter
    @Setter
    private Long updateTime;

    /**
     * 描述
     */
    @Getter
    @Setter
    private String desc;

    /***
     * 关联id
     */
    @Getter
    @Setter
    private List<CraftmanCategoryV> craftmanCategories;

    /**
     * 上架状态
     */
    @Getter
    @Setter
    private BaseEnum.YesOrNo   online;
    @Getter
    @Setter
    private List<PageTabLiveE> pageTabLiveVS;

    private static Log LOGGER = LogFactory.getLog(PageTabE.class);


    private PageTabRepo pageTabRepo = SpringUtils.takeBean("pageTabRepo", PageTabRepo.class);

    private PageTabLiveRepo pageTabLiveRepo = SpringUtils.takeBean("pageTabLiveRepo", PageTabLiveRepo.class);

    private LiveShowRepo liveShowRepo = SpringUtils.takeBean("liveShowRepo", LiveShowRepo.class);


    public void save() {
        pageTabRepo.add(this);
    }

    public Integer update() {
        Integer edit = pageTabRepo.edit(this);
        return edit;
    }

    public int delete() {
        int delete = pageTabRepo.delete(id);
        return delete;
    }

    public int batchAddLive(PageTabLiveAddCmd addCmd) {
        List<Long> liveIds = new ArrayList<>();
        addCmd.getLiveIds().forEach(liveId -> {
            liveIds.add(liveId);
        });
        DBLiveShowQuery   query        = DBLiveShowQuery.builder().ids(liveIds).status(Arrays.asList(0,1,2)).build();
        List<LiveShowDTO> liveShowDTOS = new ArrayList<>();
        liveShowDTOS = liveShowRepo.listLiveShows(query);
        if (CollectionUtils.isEmpty(liveShowDTOS)) {
            liveShowDTOS = new ArrayList<>();
        }

        List<Long> ids        = liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());
        List<Long> newLiveIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            ids.forEach(id -> {
                newLiveIds.add(id);
            });
        }

        if (CollectionUtils.isEmpty(newLiveIds)) {
            throw LiveException.failure("直播id" + liveIds + "不存在，请修改后提交");

        } else {
            liveIds.removeAll(newLiveIds);
            if (!CollectionUtils.isEmpty(liveIds)) {
                throw LiveException.failure("直播id" + liveIds + "不存在，请修改后提交");
            }
        }


        if (!Utils.isEmpty(liveShowDTOS)) {
            List<PageTabLiveE> pageTabLiveES = liveShowDTOS.stream().map(liveShowDTO -> {
                PageTabLiveE pageTabLiveE = new PageTabLiveE();
                pageTabLiveE.setLiveId(liveShowDTO.getId());
                boolean isShow = liveShowDTO.getState() != LiveEnum.LiveState.FINISHED.getCode() && liveShowDTO.getOnline() ==
                        LiveEnum.LiveOnline.ONLINE.getCode();
                pageTabLiveE.setShowStatus(isShow ? BaseEnum.YesOrNo.YES : BaseEnum.YesOrNo.NO);
                return pageTabLiveE;

            }).collect(Collectors.toList());
            return pageTabLiveRepo.batchAdd(pageTabLiveES, id);
        }
        return 0;
    }


}

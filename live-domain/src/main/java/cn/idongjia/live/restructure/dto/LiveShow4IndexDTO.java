package cn.idongjia.live.restructure.dto;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.db.mybatis.po.LiveShow4IndexPO;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.domain.entity.user.Category;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.domain.entity.zoo.ZooCount;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.pojo.co.CategoryCO;
import cn.idongjia.live.restructure.pojo.co.LiveWithCategoryCO;
import cn.idongjia.live.restructure.pojo.co.UserStageCO;
import cn.idongjia.util.Utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/8.
 */
public class LiveShow4IndexDTO extends BaseDTO<LiveShow4IndexPO> {

    public LiveShow4IndexDTO(LiveShow4IndexPO entity) {
        super(entity);
    }

    public LiveIndexResp assembleLive4Index(LiveZoo liveZoo, User user) {
        LiveIndexResp liveIndexResp = new LiveIndexResp();
        liveIndexResp.setAsid(entity.getAsid());
        liveIndexResp.setAvatar(user != null ? user.getAvatar() : null);
        liveIndexResp.setUtype(user != null && user.getAdminflag() == 2 ? 1 : 0);
        liveIndexResp.setCreateTime(entity.getCreateTm() != null ? entity.getCreateTm().getTime() : null);
        liveIndexResp.setEndTime(entity.getEndTm() != null ? entity.getEndTm().getTime() : null);
        liveIndexResp.setId(entity.getId());
        liveIndexResp.setName(user != null ? user.getUsername() : null);
        liveIndexResp.setPic(entity.getPic());
        liveIndexResp.setPreEndTime(entity.getPreEndTm() != null ? entity.getPreEndTm().getTime() : null);
        liveIndexResp.setPreStartTime(entity.getPreStartTm() != null ? entity.getPreStartTm().getTime() : null);
        liveIndexResp.setCreateTime(entity.getCreateTm() != null ? entity.getCreateTm().getTime() : null);
        liveIndexResp.setEndTime(entity.getEndTm() != null ? entity.getEndTm().getTime() : null);
        liveIndexResp.setStartTime(entity.getStartTm() != null ? entity.getStartTm().getTime() : null);
        liveIndexResp.setState(entity.getState());
        liveIndexResp.setStatus(entity.getStatus());
        liveIndexResp.setType(entity.getType());
        liveIndexResp.setUid(entity.getUserId());
        liveIndexResp.setTitle(entity.getTitle());
        if (LiveEnum.LiveState.UNSTART.getCode() == entity.getState()) {
            liveIndexResp.setUv(0);
        } else {
            liveIndexResp.setUv(liveZoo.getZooCount().getReal());
        }
        liveIndexResp.setUpdateTime(entity.getModifiedTm() != null ? entity.getModifiedTm().getTime() : null);
        liveIndexResp.setVideoCoverId(entity.getVideoCoverId());
        liveIndexResp.setVideoUrl(entity.getVideoCoverUrl());
        liveIndexResp.setGeneralWeight(entity.getGeneralWeight());
        liveIndexResp.setOnline(entity.getOnline());
        // 是否有回放
        liveIndexResp.setHasPlayback(entity.getHasPlayback());
        //屏幕方向
        liveIndexResp.setScreenDirection(entity.getScreenDirection());
        liveIndexResp.setZid(entity.getZid());
        if (liveZoo != null) {
            liveIndexResp.setZrc(liveZoo.getZrc());
        }

        return liveIndexResp;
    }


    public LiveWithCategoryCO assembleLiveWithCategory(LiveZoo liveZoo, LiveAnchor user, LiveBookCountDTO
            liveBookCountDTO, List<UserStageLiveE> userStages) {
        LiveWithCategoryCO liveWithCategory = new LiveWithCategoryCO();
        liveWithCategory.setAsid(entity.getAsid());
        liveWithCategory.setAvatar(user != null ? user.getAvatar() : null);
        liveWithCategory.setUtype(0);
        liveWithCategory.setCreateTime(entity.getCreateTm() != null ? entity.getCreateTm().getTime() : null);
        liveWithCategory.setEndTime(entity.getEndTm() != null ? entity.getEndTm().getTime() : null);
        liveWithCategory.setId(entity.getId());
        liveWithCategory.setName(user != null ? user.getUsername() : null);
        liveWithCategory.setPic(entity.getPic());
        liveWithCategory.setPreEndTime(entity.getPreEndTm() != null ? entity.getPreEndTm().getTime() : null);
        liveWithCategory.setPreStartTime(entity.getPreStartTm() != null ? entity.getPreStartTm().getTime() : null);
        liveWithCategory.setCreateTime(entity.getCreateTm() != null ? entity.getCreateTm().getTime() : null);
        liveWithCategory.setEndTime(entity.getEndTm() != null ? entity.getEndTm().getTime() : null);
        liveWithCategory.setStartTime(entity.getStartTm() != null ? entity.getStartTm().getTime() : null);
        liveWithCategory.setState(entity.getState());
        liveWithCategory.setStatus(entity.getStatus());
        liveWithCategory.setType(entity.getType());
        liveWithCategory.setUid(entity.getUserId());
        liveWithCategory.setTitle(entity.getTitle());
        if (LiveEnum.LiveState.UNSTART.getCode() == entity.getState()||LiveEnum.LiveState.FINISHED.getCode()==entity.getState()) {
            liveWithCategory.setUv(0);
        } else {
            liveWithCategory.setUv(liveZoo.getZooCount().getReal());
        }
        liveWithCategory.setUpdateTime(entity.getModifiedTm() != null ? entity.getModifiedTm().getTime() : null);
        liveWithCategory.setVideoCoverId(entity.getVideoCoverId());
        liveWithCategory.setVideoUrl(entity.getVideoCoverUrl());
        liveWithCategory.setGeneralWeight(entity.getGeneralWeight());
        liveWithCategory.setOnline(entity.getOnline());
        // 是否有回放
        liveWithCategory.setHasPlayback(entity.getHasPlayback());
        //屏幕方向
        liveWithCategory.setScreenDirection(entity.getScreenDirection());
        liveWithCategory.setZid(entity.getZid());
        if (null != liveZoo) {
            liveWithCategory.setZrc(liveZoo.getZrc());
        }
        if (user != null) {
            List<Category> categories = user.getCategories();
            if (!Utils.isEmpty(categories)) {
                List<CategoryCO> categoryCOS = categories.stream().map(category -> {
                    CategoryCO categoryCO = new CategoryCO();
                    categoryCO.setId(category.getId());
                    categoryCO.setName(category.getName());
                    return categoryCO;
                }).collect(Collectors.toList());
                liveWithCategory.setCategories(categoryCOS);
            }
        }

        if (liveBookCountDTO != null) {
            liveWithCategory.setBookCount(liveBookCountDTO.getCount());
        }
        if (!Utils.isEmpty(userStages)) {
            List<UserStageCO> stages = userStages.stream().map(userStageLiveE -> {
                int stage = userStageLiveE.getStage().getCode();
                Integer weight = userStageLiveE.getWeight();
                UserStageCO userStageCO = new UserStageCO();
                userStageCO.setStage(stage);
                userStageCO.setWeight(weight);
                return userStageCO;
            }).collect(Collectors.toList());
            liveWithCategory.setUserStages(stages);
        } else {
            liveWithCategory.setUserStages(Collections.EMPTY_LIST);
        }
        return liveWithCategory;
    }

    public Long getZooId() {
        return entity.getZid();
    }

    public Long getUserId() {
        return entity.getUserId();
    }

    public Long getId() {
        return entity.getId();
    }
    public Long getSessionId(){
        return entity.getAsid();
    }
}

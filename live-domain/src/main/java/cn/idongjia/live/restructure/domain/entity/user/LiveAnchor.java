package cn.idongjia.live.restructure.domain.entity.user;

import cn.idongjia.live.exception.LiveException;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class LiveAnchor {

    /**
     * 主播ID
     */
    private Long id;
    /**
     * 主播头像
     */
    private String avatar;
    /**
     * 主播名字
     */
    private String username;
    /**
     * 主播头衔
     */
    private String title;
    /**
     * 主播粉丝数
     */

    private Integer fansAmount;
    /**
     * 匠人类目
     */
    private List<Category> categories;
    /**
     * 注册时间，13位时间错
     */
    private Long registerTime;

    public LiveAnchor(Long id) {
        this.setId(id);
    }

    /**
     * 主播设置id 必填
     *
     * @param userId 主播id
     */
    private void setId(Long userId) {
        if (Objects.isNull(userId)) {
            throw LiveException.failure("主播id必填");
        }
        if (userId < 1) {
            throw LiveException.failure("主播id不合理");
        }
        this.id = userId;
    }

    public List<Category> getCategories() {
        if (categories == null) {
            return Collections.emptyList();
        }
        return categories;
    }

}

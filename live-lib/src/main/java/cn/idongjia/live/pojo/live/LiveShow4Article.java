package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;

public class LiveShow4Article extends Base {
    private static final long serialVersionUID = 8416255297525569217L;

    private String zUserName;
    private Long zUid;
    private String zAvatar;
    private Integer alState;

    public String getzUserName() {
        return zUserName;
    }

    public void setzUserName(String zUserName) {
        this.zUserName = zUserName;
    }

    public Long getzUid() {
        return zUid;
    }

    public void setzUid(Long zUid) {
        this.zUid = zUid;
    }

    public String getzAvatar() {
        return zAvatar;
    }

    public void setzAvatar(String zAvatar) {
        this.zAvatar = zAvatar;
    }

    public Integer getAlState() {
        return alState;
    }

    public void setAlState(Integer alState) {
        this.alState = alState;
    }
}

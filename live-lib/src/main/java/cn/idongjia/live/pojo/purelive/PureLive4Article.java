package cn.idongjia.live.pojo.purelive;

import cn.idongjia.common.base.Base;

public class PureLive4Article extends Base {
    private static final long serialVersionUID = -6841976642387661952L;

    //纯直播ID
    private Long plid;
    //首图
    private String cover;
    //标题
    private String title;
    //直播进程
    private Integer alState;
    //主播名字
    private String zUserName;
    //主播头像
    private String zAvatar;
    //主播头衔
    private String zctf;
    //主播ID
    private Long zUid;
    //创建时间
    private Long createTm;

    public PureLive4Article(){}

    public Long getPlid() {
        return plid;
    }

    public void setPlid(Long plid) {
        this.plid = plid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAlState() {
        return alState;
    }

    public void setAlState(Integer alState) {
        this.alState = alState;
    }

    public String getzUserName() {
        return zUserName;
    }

    public void setzUserName(String zUserName) {
        this.zUserName = zUserName;
    }

    public String getzAvatar() {
        return zAvatar;
    }

    public void setzAvatar(String zAvatar) {
        this.zAvatar = zAvatar;
    }

    public String getZctf() {
        return zctf;
    }

    public void setZctf(String zctf) {
        this.zctf = zctf;
    }

    public Long getzUid() {
        return zUid;
    }

    public void setzUid(Long zUid) {
        this.zUid = zUid;
    }

    public Long getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Long createTm) {
        this.createTm = createTm;
    }
}

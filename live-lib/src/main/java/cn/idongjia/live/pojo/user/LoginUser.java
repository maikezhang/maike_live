package cn.idongjia.live.pojo.user;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 用户pojo
 * Created by wuqiang on 16/4/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("all")
public class LoginUser extends Base {

    //登录后需要将主播信息
    public static final String ANCHOR_USER_KEY = "ANCHOR_USER_KEY";

    private static final long serialVersionUID = 1283685837446878127L;

    private Long uid;
    private Long aid;
    private String username;
    private String mobile;
    private String password;
    private String salt;

    private String avatar; //头像
    private int type;      //判断是什么用户,0 普通用户,1主持人

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LoginUser(){}
}

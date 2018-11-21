package cn.idongjia.live.pojo.purelive;

import cn.idongjia.common.base.Base;

public class PureLiveShare extends Base {
    private static final long serialVersionUID = 3954567029681484090L;

    //分享标题
    private String shareTitle;
    //分享描述
    private String shareDesc;
    //分享图片
    private String sharePic;

    public PureLiveShare(){}

    public PureLiveShare(String shareTitle, String sharePic, String shareDesc){
        this.shareTitle = shareTitle;
        this.sharePic = sharePic;
        this.shareDesc = shareDesc;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    public String getSharePic() {
        return sharePic;
    }

    public void setSharePic(String sharePic) {
        this.sharePic = sharePic;
    }
}

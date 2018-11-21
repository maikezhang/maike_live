package cn.idongjia.live.pojo.purelive.playback;

public class PlayBackAdmin extends PlayBackDO{

    //直播标题
    private String title;
    //回放时长时间长度字符串
    private String durationStr;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }
}

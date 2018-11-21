package cn.idongjia.live.db.mybatis.po;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/8.
 */
@Getter
@Setter
public class PlayBackPO extends BasePO {
    private  Long liveId;
    private  Long duration;
    private  Long id;
    private  String url;
    /**
     * 回放状态-1删除0正常
     */
    private  Integer status;
    /**
     * 创建时间
     */
    private  Timestamp createTime;
    /**
     * 修改时间

     */
    private  Timestamp modifiedTime;

    public  PlayBackPO(){

    }
    @Builder
    public PlayBackPO(Long liveId, Long duration, Long id, String url, Integer status, Timestamp createTime, Timestamp modifiedTime) {
        this.liveId = liveId;
        this.duration = duration;
        this.id = id;
        this.url = url;
        this.status = status;
        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
    }


}

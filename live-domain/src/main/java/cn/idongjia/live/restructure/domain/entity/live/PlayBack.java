package cn.idongjia.live.restructure.domain.entity.live;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/15
 * Time: 上午11:09
 */
@Setter
@Getter
@Builder
public class PlayBack  {
    //唯一ID
    private Long id;
    //直播ID
    private Long lid;
    //回放地址
    private String url;
    //回放时长单位毫秒
    private Long duration;
    //腾讯文件ID
    private Integer fileId;
    //回放状态-1删除0正常
    private Integer status;
    //创建时间
    private Timestamp createTm;
    //修改时间
    private Timestamp modifiedTm;
}

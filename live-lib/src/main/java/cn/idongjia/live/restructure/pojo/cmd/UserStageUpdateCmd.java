package cn.idongjia.live.restructure.pojo.cmd;

import cn.idongjia.live.restructure.pojo.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/11
 * Time: 上午11:28
 */
@Getter
@Setter
@NoArgsConstructor

public class UserStageUpdateCmd extends Command {


    /**
     * id
     */
    private Long id;
    /**
     * 直播id
     */
    private Long liveId;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 用户阶段
     */
    private int stage;

    /**
     * 专场id
     */
    private Long sessionId;

    /**
     * 首图
     */
    private String pic;

    /**
     * 匠人名称
     */
    private String craftman;

    /**
     * 直播进程
     */
    private Integer state;
}

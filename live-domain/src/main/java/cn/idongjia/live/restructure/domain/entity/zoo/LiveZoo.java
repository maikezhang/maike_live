package cn.idongjia.live.restructure.domain.entity.zoo;

import cn.idongjia.zoo.pojo.Zoo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/6/7.
 */
@Getter
@Setter
public class LiveZoo {


    private Long zid;
    private Long uid;       //聊天室管理员
    private Integer zrc;    //聊天室随机数
    private String name;
    private Long createTime;
    private ZooCount zooCount;

    public LiveZoo(Zoo zoo, ZooCount zooCount) {
        zid = zoo.getZid();
        uid = zoo.getUid();
        zrc = zoo.getZrc();
        name = zoo.getName();
        createTime = zoo.getCreatetm();
        this.zooCount = zooCount;
    }
}

package cn.idongjia.live.api.live.pojo;

import cn.idongjia.common.query.BaseSearch;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 索引查询条件
 *
 * @author lc
 * @create at 2017/12/18.
 */
@Getter
@Setter
public class LiveIndexSearch extends BaseSearch {

    public  LiveIndexSearch(){

    }
    //查询id
    private List<Long> ids;


    //更新时间
    private Long updateTime;

}

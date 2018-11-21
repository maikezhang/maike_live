package cn.idongjia.live.pojo;

import cn.idongjia.common.base.Base;

import java.util.List;


public class BaseList<T> extends Base{

    //数量
    private Integer count;
    //内容列表
    private List<T> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}

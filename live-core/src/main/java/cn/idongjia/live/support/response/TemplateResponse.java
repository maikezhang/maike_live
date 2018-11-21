package cn.idongjia.live.support.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Description: 超级模板返回的数据标准结构体（对象）
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/14
 * Time: 下午5:59
 */
@Setter
@Getter
public class TemplateResponse {

    private List<TempateRes> res;

    private int code;

}

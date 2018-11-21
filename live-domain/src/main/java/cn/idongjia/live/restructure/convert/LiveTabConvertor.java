package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.db.mybatis.po.PageTabPO;
import cn.idongjia.live.pojo.homePage.LiveTabCO;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/20
 * Time: 上午11:34
 */
@Component
public class LiveTabConvertor implements ConvertorI<LiveTabCO,PageTabE,PageTabPO> {


    @Override
    public LiveTabCO entityToClient(PageTabE pageTabE){
        if(Objects.isNull(pageTabE)){
            return null;
        }
        LiveTabCO co=new LiveTabCO();
        co.setId(pageTabE.getId());
       co.setTitle(pageTabE.getName());
        return co;
    }

}

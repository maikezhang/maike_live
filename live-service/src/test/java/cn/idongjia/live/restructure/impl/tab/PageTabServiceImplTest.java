package cn.idongjia.live.restructure.impl.tab;

import cn.idongjia.live.api.PageTabServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.tab.PageTabUpdateCmd;
import cn.idongjia.live.restructure.pojo.co.CategoryCO;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabCO;
import cn.idongjia.live.restructure.pojo.query.PageTabQry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/7/11.
 */
public class PageTabServiceImplTest extends SpringJUnitNoRollbackTest {

    @Resource
    private PageTabServiceI pageTabServiceI;
    @Before
    public void setUp() throws Exception {
    }


    @Test
    @Rollback(false)
    public void add(){
        PageTabAddCmd cmd=new PageTabAddCmd();
        PageTabCO pageTabCO=new PageTabCO();
        pageTabCO.setStatus(1);
        pageTabCO.setWeight(0);
        pageTabCO.setType(2);
        pageTabCO.setOnline(1);
        pageTabCO.setName("ceshi1111");
        pageTabCO.setDesc("sdfwefwefewfwef");
        List<CategoryCO> categoryCOS=new ArrayList<>();
        CategoryCO categoryCO=new CategoryCO();
        categoryCO.setName("茶器");
        categoryCO.setId(1L);
        categoryCOS.add(categoryCO);
//        pageTabCO.setCategoryCOS(categoryCOS);
        cmd.setPageTabCO(pageTabCO);
        pageTabServiceI.add(cmd);
    }

    @Test
    @Rollback(false)
    public void update(){
        PageTabUpdateCmd cmd=new PageTabUpdateCmd();
        PageTabCO pageTabCO=new PageTabCO();
        pageTabCO.setId(2L);
        pageTabCO.setStatus(1);
        pageTabCO.setWeight(100);
        pageTabCO.setType(1);
        pageTabCO.setOnline(1);
        pageTabCO.setName("ceshi1111");
        pageTabCO.setDesc("sdfwefwefewfwef");
        List<CategoryCO> categoryCOS=new ArrayList<>();
        CategoryCO categoryCO=new CategoryCO();
        categoryCO.setName("茶器");
        categoryCO.setId(2L);
        categoryCOS.add(categoryCO);
        pageTabCO.setCategoryCOS(categoryCOS);
        cmd.setPageTabCO(pageTabCO);
        pageTabServiceI.update(cmd);
    }

    @Test
    @Rollback(false)
    public void delete(){
        PageTabDelCmd cmd=new PageTabDelCmd();
        cmd.setId(3);
        pageTabServiceI.delete(cmd);
    }

    @Test
    public void get(){
        System.out.println(pageTabServiceI.get(11).getData());

    }

    @Test
    public void page(){
        PageTabQry qry=new PageTabQry();
        qry.setStatus(1);
        MultiResponse<PageTabCO> page = pageTabServiceI.page(qry);
        System.out.println(page.getTotal()+"\n"+page.getData());
    }
}
package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.ResourceService;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/6/23.
 */
public class ResourceServiceImplTest extends SpringJUnitNoRollbackTest {

    @Resource
    private ResourceService resourceService;



    @Test
    @Rollback(false)
    public void mainResource() throws Exception {
        resourceService.mainResource(3650L,320810L,1,2);

    }

    @Test
    public void mainResource1() throws Exception {
    }

}
package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.AnchorBlackWhiteServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteDelCmd;
import cn.idongjia.live.restructure.pojo.co.live.AnchorBlackWhiteCO;
import cn.idongjia.live.restructure.pojo.query.AnchorBlackWhiteQry;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/9/3.
 */
public class AnchorBlackWhiteServiceImplTest extends SpringJUnitNoRollbackTest{

    @Resource
    private AnchorBlackWhiteServiceI anchorBlackWhiteServiceI;

    private Log LOGGER= LogFactory.getLog(AnchorBlackWhiteServiceImplTest.class);

    @Test
    @Rollback(false)
    public void add() throws Exception {

        AnchorBlackWhiteAddCmd cmd=new AnchorBlackWhiteAddCmd();
        List<Long> anchorIds= Arrays.asList(190639L,704692L);
        cmd.setType(AnchorBlackWhiteEnum.BlackWhite.BLACK.getCode());
        cmd.setAnchorIds(anchorIds);
        anchorBlackWhiteServiceI.add(cmd);



    }

    @Test
    public void update() throws Exception {
    }

    @Test
    @Rollback(false)
    public void delete() throws Exception {
        AnchorBlackWhiteDelCmd cmd=new AnchorBlackWhiteDelCmd();
        cmd.setId(190639L);
        cmd.setType(AnchorBlackWhiteEnum.BlackWhite.BLACK.getCode());
        anchorBlackWhiteServiceI.delete(cmd);

    }

    @Test
    public void page() throws Exception {


        AnchorBlackWhiteQry qry=new AnchorBlackWhiteQry();

        qry.setType(AnchorBlackWhiteEnum.BlackWhite.WHITE.getCode());
        qry.setLimit(10);
        qry.setPage(1);

        MultiResponse<AnchorBlackWhiteCO> page = anchorBlackWhiteServiceI.page(qry);
        List<AnchorBlackWhiteCO> data = (List<AnchorBlackWhiteCO>) page.getData();
        LOGGER.info(data);
    }

}
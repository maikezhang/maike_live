package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.LiveMPServiceI;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.restructure.cache.liveMP.LiveMPPushCache;
import cn.idongjia.live.restructure.event.handler.LiveStartedStepOneHandler;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPDetailCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPFormIdCO;
import cn.idongjia.live.restructure.pojo.query.LiveMPQry;
import cn.idongjia.live.support.redis.RedisTools;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.junit.Test;
import org.relaxng.datatype.DatatypeException;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/9/5.
 */
public class LiveMPServiceImplTest extends SpringJUnitNoRollbackTest {


    @Resource
    private LiveMPServiceI liveMPServiceI;

    @Resource
    private LiveMPPushCache liveMPPushCache;

    @Resource
    private RedisTools redisCache;

    @Resource
    private LiveStartedStepOneHandler liveStartedStepOneHandler;

    private Log LOGGER = LogFactory.getLog(LiveMPServiceImplTest.class);

    @Test
    public void list() throws Exception {
    }

    @Test
    @Rollback(false)
    public void updateWeight() throws Exception {


        liveMPServiceI.updateWeight(3682L, 20);


    }

    @Test
    public void mpPageApi() throws Exception {

        LiveMPQry qry = new LiveMPQry();
        qry.setPage(1);
        liveMPServiceI.mpPageApi(qry);

    }

    @Test
    public void getDetail() throws Exception {

        LiveMPDetailCO detail = liveMPServiceI.getDetail(3682L, 190639L);


        LOGGER.info(detail);

    }

    @Test
    public void collectFormId() throws Exception {

        LiveMPFormIdCO liveMPFormIdCO = new LiveMPFormIdCO();


        liveMPServiceI.collectFormId(liveMPFormIdCO);
    }

    @Test
    @Rollback(false)
    public void putRedisTest() {

        Long key = 123L;
        String value = "789-zhangyingjie123";


        String LIVEKEY = "live_MP_liveId_";
//        liveMPPushCache.putLiveUserRedis(key, value, null);
//
//        liveMPPushCache.putUserFormIdRedis(10086L, "hahahahhahahah");

        List<Long> liveId= Arrays.asList(2381L,3606L);

        List<String> keys=liveId.stream().map(id->{
            return LIVEKEY+id;
        }).collect(Collectors.toList());


        redisCache.pipeDel(keys);


    }

    @Test
    @Rollback(false)
    public void sendTest(){
        liveStartedStepOneHandler.sendWxMpNotify(3694L);
    }

}
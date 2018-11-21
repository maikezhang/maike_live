package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PlayBackService;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDO;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/6/22.
 */
public class PlayBackServiceImplTest extends SpringJUnitNoRollbackTest {

    @Resource
    private PlayBackService playBackService;

    @Test
    public void addPlayBack() throws Exception {
    }

    @Test
    public void getPlayBackByLid() throws Exception {
    }

    @Test
    @Rollback(false)
    public void deletePlayBack() throws Exception {


        PlayBackDO playBackDO=new PlayBackDO();
        playBackDO.setId(11l);
        playBackDO.setStatus(-1);

playBackService.deletePlayBack(11L,playBackDO);


    }

    @Test
    public void updatePlayBack() throws Exception {
    }

    @Test
    public void listPlayBackWithCount() throws Exception {
    }

    @Test
    public void deletePlayBack1() throws Exception {
    }

    @Test
    public void getBatchPlayBackByLiveId() throws Exception {
    }

}
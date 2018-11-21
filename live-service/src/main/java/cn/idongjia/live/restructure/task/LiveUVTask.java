package cn.idongjia.live.restructure.task;

import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.schedule.AbstractScheduleTask;

import javax.annotation.Resource;

/**
 * 刷新uv更新时间
 * 
 * @author longchuan
 *
 */

public class LiveUVTask extends AbstractScheduleTask {

    private static final Log LOG = LogFactory.getLog(LiveUVTask.class);
    @Resource
    private LiveShowRepo liveShowRepo;

    @Override
    public void run() {
        Long count = liveShowRepo.updateLiveUVTime();
        LOG.info("本次更新直播中的直播条数" + count);
    }
}

package cn.idongjia.live.restructure.event.exception;

import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import com.lmax.disruptor.ExceptionHandler;

public class EventExceptinHandler implements ExceptionHandler {

    private static final Log logger = LogFactory.getLog(EventExceptinHandler.class);

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        logger.error("process data error sequence ==[{}] event==[{}] ,ex ==[{}]", sequence, event.toString(), ex);
        logger.error("处理事件异常{}",ex);
    }


    @Override
    public void handleOnStartException(Throwable ex) {
        logger.error("start disruptor error ==[{}]!", ex);
        logger.error("处理事件异常{}",ex);
    }

    
    @Override
    public void handleOnShutdownException(Throwable ex) {
        logger.error("处理事件异常{}",ex);
        logger.error("shutdown disruptor error ==[{}]!", ex);
    }

}

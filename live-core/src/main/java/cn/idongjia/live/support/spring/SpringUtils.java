package cn.idongjia.live.support.spring;

import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import com.google.common.base.Strings;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * Spring帮助类
 *
 * @version 1.0
 */
@Component
public final class SpringUtils implements ApplicationContextAware {

    private SpringUtils() {
    }

    private static final Log LOGGER = LogFactory.getLog(SpringUtils.class);

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.context = applicationContext;
    }

    /**
     * 根据bean名字和类型获取实例
     *
     * @param beanName 名字
     * @param clazz    类型
     * @param <T>      类型T
     * @return 类型实例
     */
    public static <T> Optional<T> getBean(String beanName, Class<T> clazz) {
        if (Strings.isNullOrEmpty(beanName)) {
            LOGGER.warn("类名字为空");
            return Optional.empty();
        }
        if (Objects.isNull(clazz)) {
            LOGGER.warn("类型为空");
            return Optional.empty();
        }
        if (Objects.isNull(context)) {
            LOGGER.warn("spring 加载失败");
            return Optional.empty();
        }
        return Optional.ofNullable(context.getBean(beanName, clazz));
    }
    public static <T> T takeBean(String beanName, Class<T> clazz) {
        if (Strings.isNullOrEmpty(beanName)) {
            LOGGER.warn("类名字为空");
            return null;
        }
        if (Objects.isNull(clazz)) {
            LOGGER.warn("类型为空");
            return null;
        }
        if (Objects.isNull(context)) {
            LOGGER.warn("spring 加载失败");
            return null;
        }
        return context.getBean(beanName, clazz);
    }

}

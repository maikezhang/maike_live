package cn.idongjia.live.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

public final class SpringBeanLoader implements ApplicationContextAware {

    private SpringBeanLoader(){}

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    /**
     * 从当前上下文中获取指定名称的Bean实例
     * @param beanName bean 名称
     * @param clazz bean 类型
     * @param <T> 泛型
     * @return T 实例
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        Objects.requireNonNull(ctx);
        return ctx.getBean(beanName, clazz);
    }
}

package me.wenkang.rpc.core.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author wenkang
 * @since 2018/9/14
 */
@Slf4j
public class ProviderInitializer implements ApplicationContextAware, ServiceInitializer{

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.context = applicationContext;
    }


    @Override
    public Object getImpl(Class<?> cls) {
        return context.getBean(cls);
    }
}

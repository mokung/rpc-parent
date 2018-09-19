package me.wenkang.rpc.core.consumer;

import lombok.extern.slf4j.Slf4j;
import me.wenkang.rpc.core.bean.ClientConf;
import me.wenkang.rpc.core.bean.ClientConfBean;
import me.wenkang.rpc.core.consumer.proxy.ConsumerProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wenkang
 * @since 2018/9/14
 */
@Slf4j
public class ConsumerInitializer implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor{



    private ApplicationContext ctx;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {


    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Map<String, ClientConfBean> clientConfMap = new HashMap<>();
        Map<String, ClientConf> confMap = ctx.getBeansOfType(ClientConf.class);
        confMap.forEach((k,v)->{
            v.getInterfaces().forEach(i->{
                try {
                    ClientConfBean clientConfBean = new ClientConfBean();
                    clientConfBean.setHost(v.getHost());
                    clientConfBean.setPort(v.getPort());
                    Class clazz = Class.forName(i);
                    clientConfBean.setIface(clazz);
                    clientConfMap.put(i,clientConfBean);
                } catch (ClassNotFoundException e) {
                    log.error("ClassNotFoundException", e);
                }
            });

        });

        clientConfMap.forEach((k,v)->{
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(v.getIface());
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            definition.getPropertyValues().addPropertyValue("iface", definition.getBeanClassName());
            definition.getPropertyValues().addPropertyValue("clientConfBean", v);
            definition.setBeanClass(ConsumerProxyFactory.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefinitionRegistry.registerBeanDefinition(k, definition);

        });

        log.info("bean init");
    }
}

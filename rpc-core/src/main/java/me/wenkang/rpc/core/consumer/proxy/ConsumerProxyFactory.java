package me.wenkang.rpc.core.consumer.proxy;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.wenkang.rpc.core.bean.ClientConfBean;
import me.wenkang.rpc.core.consumer.handler.ConsumerInvocationHandler;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author wenkang
 * @since 2018/9/14
 */
@Slf4j
public class ConsumerProxyFactory<T> implements FactoryBean<T> {



    @Setter
    private  ClientConfBean clientConfBean;

    @Setter
    private  Class<T> iface ;


    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
       return  (T) Proxy.newProxyInstance(iface.getClassLoader(),
               new Class[] {iface}, new ConsumerInvocationHandler(clientConfBean));
    }


    @Override
    public Class<T> getObjectType() {
        return iface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}

package me.wenkang.rpc.core;

import me.wenkang.rpc.core.consumer.ConsumerInitializer;
import me.wenkang.rpc.core.provider.ProviderInitializer;
import me.wenkang.rpc.core.provider.ServerStartApplicationListener;
import me.wenkang.rpc.core.provider.ServiceInitializer;
import org.springframework.context.annotation.Bean;

/**
 * @author wenkang
 * @since 2018/9/14
 */
public class RpcConfig  {

    @Bean
    public ServerStartApplicationListener startApplicationListener() {
        return new ServerStartApplicationListener();
    }

    @Bean
    public ConsumerInitializer consumerInitializer() {
        return new ConsumerInitializer();
    }

    @Bean
    public ServiceInitializer providerInitializer() {
        return new ProviderInitializer();
    }

}

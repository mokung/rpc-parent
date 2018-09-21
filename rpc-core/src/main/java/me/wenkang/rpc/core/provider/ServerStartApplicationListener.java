package me.wenkang.rpc.core.provider;

import lombok.extern.slf4j.Slf4j;
import me.wenkang.rpc.core.bean.InterfaceServiceInfo;
import me.wenkang.rpc.core.bean.ServerConf;
import me.wenkang.rpc.core.provider.context.ProviderCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * @author wenkang
 * @since 2018/9/18
 */
@Slf4j
public class ServerStartApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        ServerConf serverConf = applicationContext.getBean(ServerConf.class);

        ServiceInitializer serviceInitializer = applicationContext.getBean(ServiceInitializer.class);

        List<String> interfaces = serverConf.getInterfaces();
        for (String service : interfaces) {
            Class serviceBean = null;
            try {
                serviceBean  = Class.forName(service);
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException",e);
                continue;
            }
            ProviderCache.get().put(service, new InterfaceServiceInfo(serviceBean));
        }
            try {
                Server.startServer(serviceInitializer, serverConf.getPort());
            } catch (Exception e) {
                log.error("fail to start server", e);
                System.exit(-1);
            }

    }
}

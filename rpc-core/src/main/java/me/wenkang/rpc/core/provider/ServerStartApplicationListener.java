package me.wenkang.rpc.core.provider;

import lombok.extern.slf4j.Slf4j;
import me.wenkang.rpc.core.bean.ServerConf;
import me.wenkang.rpc.core.bean.InterfaceServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, InterfaceServiceInfo> handlerMap = new HashMap<>();
        for (String service : interfaces) {
            Class serviceBean = null;
            try {
                serviceBean  = Class.forName(service);
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException",e);
                continue;
            }
            handlerMap.put(service, new InterfaceServiceInfo(serviceBean));
        }
            try {
                Server.startServer(serviceInitializer, handlerMap, serverConf.getPort());
            } catch (Exception e) {
                log.error("fail to start server", e);
                System.exit(-1);
            }

    }
}

package me.wenkang.rpc.core.provider.context;

import me.wenkang.rpc.core.bean.InterfaceServiceInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenkang
 * @since 2018/9/20
 */
public class ProviderCache {

    private ProviderCache(){
    }

    private static final ProviderCache CACHE = new ProviderCache();

    public static ProviderCache get(){
        return CACHE;
    }
    
    private final Map<String, InterfaceServiceInfo> services = new ConcurrentHashMap<>();

    public void put(String iface, InterfaceServiceInfo serviceInfo){
        services.put(iface, serviceInfo);
    }

    public InterfaceServiceInfo getService(String iface){
       return services.get(iface);
    }

}

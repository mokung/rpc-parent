package me.wenkang.rpc.core.provider;

/**
 * @author wenkang
 * @since 2018/9/18
 */
public interface ServiceInitializer {

    Object getImpl(Class<?> cls);
}

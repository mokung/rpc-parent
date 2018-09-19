package me.wenkang.rpc.core.bean;

import lombok.Data;

/**
 * @author wenkang
 * @since 2018/9/14
 */
@Data
public class ClientConfBean {

    private String host;

    private int port;

    private Class<?> iface;

}

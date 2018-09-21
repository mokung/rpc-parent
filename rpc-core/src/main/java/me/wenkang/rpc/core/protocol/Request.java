package me.wenkang.rpc.core.protocol;

import lombok.Data;

/**
 * @author wenkang
 * @since 2018/9/4
 */

@Data
public class Request{

    private String requestId;
    private String iface;
    private String methodName;
    private Object[] parameters;
}

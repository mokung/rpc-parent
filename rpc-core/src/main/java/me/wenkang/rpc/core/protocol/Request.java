package me.wenkang.rpc.core.protocol;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wenkang
 * @since 2018/9/4
 */

@Data
public class Request{

    private String requestId;
    private String iface;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}

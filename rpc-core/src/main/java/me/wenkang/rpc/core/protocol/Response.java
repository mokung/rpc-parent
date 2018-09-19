package me.wenkang.rpc.core.protocol;

import lombok.Data;

/**
 * @author wenkang
 * @since 2018/9/4
 */

@Data
public class Response {

    private String requestId;

    private Throwable error;

    private Object result;

}

package me.wenkang.rpc.core.bean;

import lombok.Data;

import java.util.List;

/**
 * @author wenkang
 * @since 2018/9/14
 */
@Data
public class ServerConf {

    private int port;

    private List<String> interfaces;
}

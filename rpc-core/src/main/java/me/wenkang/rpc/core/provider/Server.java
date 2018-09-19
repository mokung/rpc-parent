package me.wenkang.rpc.core.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import me.wenkang.rpc.core.protocol.Request;
import me.wenkang.rpc.core.bean.InterfaceServiceInfo;
import me.wenkang.rpc.core.provider.handler.ProviderHandler;
import me.wenkang.rpc.core.serialization.Deserializer;
import me.wenkang.rpc.core.serialization.Serializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author wenkang
 * @since 2018/9/18
 */
@Slf4j
class Server {

    private static boolean init = false;


    static void startServer(ServiceInitializer serviceInitializer, Map<String, InterfaceServiceInfo> handlerMap, int port) throws Exception {

        if (init) {
            return;
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelHandler channelHandler = new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline().addLast(new Deserializer(Request.class)).addLast(new Serializer())
                        .addLast(new ProviderHandler(handlerMap, serviceInitializer));
            }
        };

        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(channelHandler).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        String host = getLocalHost();

        bootstrap.bind(host, port).sync();
        //  future.channel().closeFuture().sync();
        init = true;

        log.info("rpc start");

    }

    private static String getLocalHost() throws UnknownHostException {
            return InetAddress.getLocalHost().getHostName();
    }

}

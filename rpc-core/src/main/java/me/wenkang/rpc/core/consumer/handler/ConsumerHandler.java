package me.wenkang.rpc.core.consumer.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.wenkang.rpc.core.protocol.Request;
import me.wenkang.rpc.core.protocol.Response;
import me.wenkang.rpc.core.serialization.Deserializer;
import me.wenkang.rpc.core.serialization.Serializer;

import java.util.concurrent.CountDownLatch;

/**
 * @author wenkang
 * @since 2018/9/14
 */
public class ConsumerHandler extends SimpleChannelInboundHandler<Response> {


    private int port;

    private String host;

    private Response response;

    private CountDownLatch latch = new CountDownLatch(1);

    ConsumerHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
        this.response = response;
        latch.countDown();
    }


    Response call(Request request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelInitializer<SocketChannel> channelHandler = new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline()
                            .addLast(new Serializer())
                            .addLast(new Deserializer(Response.class))
                            .addLast(ConsumerHandler.this);
                }
            };
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(channelHandler)
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync();
            latch.await();
            if (response != null) {
                future.channel().closeFuture().sync();
            }
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }
}

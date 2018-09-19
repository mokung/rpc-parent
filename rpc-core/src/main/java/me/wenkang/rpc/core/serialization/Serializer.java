package me.wenkang.rpc.core.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.wenkang.rpc.core.protocol.Response;

/**
 * @author wenkang
 * @since 2018/9/14
 */
public class Serializer extends MessageToByteEncoder {




    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] data = SerializationHelper.serialize(o);
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}

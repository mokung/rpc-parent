package me.wenkang.rpc.core.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author wenkang
 * @since 2018/9/14
 */
public class Deserializer extends ByteToMessageDecoder {


    private Class<?> deClass;

    public Deserializer(Class<?> deClass) {
        this.deClass = deClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int headerLength = 4;
        if (byteBuf.readableBytes() < headerLength) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0) {
            channelHandlerContext.close();
        }
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        Object obj = SerializationHelper.deserialize(data, deClass);
        list.add(obj);
    }
}

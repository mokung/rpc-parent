package me.wenkang.rpc.core.provider.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.wenkang.rpc.core.bean.InterfaceServiceInfo;
import me.wenkang.rpc.core.protocol.Request;
import me.wenkang.rpc.core.protocol.Response;
import me.wenkang.rpc.core.provider.ServiceInitializer;

import java.util.Map;

/**
 * @author wenkang
 * @since 2018/9/7
 */

@Slf4j
public class ProviderHandler extends SimpleChannelInboundHandler<Request> {

    private final Map<String, InterfaceServiceInfo> handlerMap;

    private ServiceInitializer serviceInitializer;



    public ProviderHandler(Map<String, InterfaceServiceInfo> handlerMap, ServiceInitializer serviceInitializer) {
        this.handlerMap = handlerMap;
        this.serviceInitializer = serviceInitializer;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
        Response response = new Response();

        response.setRequestId(request.getRequestId());
        try {
            log.info("RequestId {}",request.getRequestId());
            Object result = call(request);
            response.setResult(result);
            log.info("===invoke ({}.{}) success", request.getIface(),request.getMethodName());
        } catch (Exception e) {
            log.error("===Fail to invoke ("+request.getIface() +"." + request.getMethodName()+ ")", e);
            response.setError(e);
        }
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object call(Request request) throws Exception {
        InterfaceServiceInfo serviceBean = handlerMap.get(request.getIface());

        if (serviceBean == null){
            throw new Exception("interface undeclared");
        }

        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();

        return serviceBean.getMethod(methodName).invoke(serviceInitializer.getImpl(serviceBean.getIface()), parameters);

    }



}

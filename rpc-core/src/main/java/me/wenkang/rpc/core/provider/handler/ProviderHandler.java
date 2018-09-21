package me.wenkang.rpc.core.provider.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.wenkang.rpc.core.bean.InterfaceServiceInfo;
import me.wenkang.rpc.core.protocol.Request;
import me.wenkang.rpc.core.protocol.Response;
import me.wenkang.rpc.core.provider.ServiceInitializer;
import me.wenkang.rpc.core.provider.context.ProviderCache;

/**
 * @author wenkang
 * @since 2018/9/7
 */

@Slf4j
public class ProviderHandler extends SimpleChannelInboundHandler<Request> {


    private ServiceInitializer serviceInitializer;


    public ProviderHandler(ServiceInitializer serviceInitializer) {
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
        InterfaceServiceInfo serviceBean =  ProviderCache.get().getService(request.getIface());

        if (serviceBean == null ){
            throw new Exception("interface undeclared");
        }

        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();

        return serviceBean.getMethod(methodName).invoke(serviceInitializer.getImpl(serviceBean.getIface()), parameters);

    }



}

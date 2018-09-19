package me.wenkang.rpc.core.consumer.handler;

import lombok.extern.slf4j.Slf4j;
import me.wenkang.rpc.core.bean.ClientConfBean;
import me.wenkang.rpc.core.protocol.Request;
import me.wenkang.rpc.core.protocol.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author wenkang
 * @since 2018/9/14
 */

@Slf4j
public class ConsumerInvocationHandler implements InvocationHandler {


    private ClientConfBean clientConfBean;


   public ConsumerInvocationHandler(ClientConfBean clientConfBean){
        this.clientConfBean = clientConfBean;
   }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        Request request = buildRequest(method, args);
        log.info("RequestId {} service {} method {} ",request.getRequestId(), request.getIface(), request.getMethodName());
        ConsumerHandler consumerHandler = new ConsumerHandler(clientConfBean.getHost(), clientConfBean.getPort());
        Response response = consumerHandler.call(request);
        if (response.getError() != null) {
            log.error("service invoker error, cause:{}", response.getError());
            throw new Exception("service invoker error,cause:", response.getError());
        } else {
            return response.getResult();
        }
    }

    private Request buildRequest(Method method, Object[] args) {
        Request request = new Request();
        request.setIface(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        request.setRequestId(UUID.randomUUID().toString());

        return request;
    }
}

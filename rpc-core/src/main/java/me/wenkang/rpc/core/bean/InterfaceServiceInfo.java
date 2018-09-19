package me.wenkang.rpc.core.bean;

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wenkang
 * @since 2018/9/14
 */
public class InterfaceServiceInfo {

    @Getter
    private final Class<?> iface;

    private final Map<String, Method> methods = new HashMap<>();

    public InterfaceServiceInfo(Class<?> iface) {
        if (iface == null || !iface.isInterface()){
            throw new VerifyError("接口为空");
        }
        this.iface = iface;
        initMethods();
    }

    private void initMethods() {
        for (Method method : iface.getMethods()) {
            methods.put(method.getName(), method);
        }
    }

    public Method getMethod(String methodName) {
        Method method = methods.get(methodName);
        if (method == null) {
            throw new VerifyError("Method(" + methodName + ") doesn't Exist in Interface(" + iface.getName() + ")!");
        }
        return method;
    }


}

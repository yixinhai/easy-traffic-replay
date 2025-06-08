package com.xh.easy.trafficreplay.service.core.handler;

import com.xh.easy.trafficreplay.service.core.executor.ReplayExecutor;
import com.xh.easy.trafficreplay.service.core.executor.Visitor;

import java.lang.reflect.Method;

/**
 * 目标方法信息
 *
 * @author yixinhai
 */
public class MethodInfo extends MethodHandler {

    private final Visitor visitor;
    private final Object target;
    private final Method method;

    public MethodInfo(Object target, Method method) {
        this.visitor = ReplayExecutor.getInstance();
        this.target = target;
        this.method = method;
    }

    @Override
    protected void accept() {
        visitor.visit(this);
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "MethodInfo{" + "target=" + target + ", method=" + method + '}';
    }

    @Override
    public Object invoke() throws Exception {
        return super.invoke();
    }
}

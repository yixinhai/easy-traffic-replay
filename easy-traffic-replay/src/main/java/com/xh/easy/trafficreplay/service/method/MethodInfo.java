package com.xh.easy.trafficreplay.service.method;

import com.xh.easy.trafficreplay.service.ReplayExecutor;
import com.xh.easy.trafficreplay.service.Visitor;

import java.util.Arrays;

/**
 * 目标方法信息
 *
 * @author yixinhai
 */
public class MethodInfo implements Method {

    private final Visitor visitor;
    private final Object target;
    private final java.lang.reflect.Method method;
    private Object[] args;

    public MethodInfo(Object target, java.lang.reflect.Method method) {
        this.visitor = ReplayExecutor.getInstance();
        this.target = target;
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public java.lang.reflect.Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "MethodInfo{" + "target=" + target + ", method=" + method + ", args=" + Arrays.toString(args) + '}';
    }

    public Object invoke() throws Exception {
        return method.invoke(target, args);
    }

    @Override
    public void accept() {
        visitor.visit(this);
    }
}

package com.xh.easy.trafficreplay.service.core.allocator;

import com.xh.easy.trafficreplay.service.core.executor.ReplayExecutor;
import com.xh.easy.trafficreplay.service.core.executor.Visitor;
import com.xh.easy.trafficreplay.service.core.handler.MethodHandler;

import java.lang.reflect.Method;

/**
 * 方法信息增强
 *
 * @author yixinhai
 */
public abstract class Allocator extends MethodHandler {

    protected MethodHandler methodInfo;
    protected Visitor visitor;

    public Allocator(MethodHandler methodHandler) {
        this.methodInfo = methodHandler;
        this.visitor = ReplayExecutor.getInstance();
    }

    public abstract void allocate() throws Exception;

    @Override
    public Object getTarget() {
        return methodInfo.getTarget();
    }

    @Override
    public Method getMethod() {
        return methodInfo.getMethod();
    }

    @Override
    protected void accept() {
        visitor.visit(this);
    }
}

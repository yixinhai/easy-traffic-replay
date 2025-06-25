package com.xh.easy.trafficreplay.service.core.handler;

import com.xh.easy.trafficreplay.service.util.ClassWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author yixinhai
 */
public abstract class MethodHandler implements MethodInvoker {

    protected abstract void accept();

    public abstract Object getTarget();

    public abstract Method getMethod();

    @Override
    public Object invoke() throws Exception {
        return this.getMethod().invoke(this.getTarget());
    }

    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        return ClassWrapper.getDeclaredAnnotation(getMethod(), clazz);
    }
}

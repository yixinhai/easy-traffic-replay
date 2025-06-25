package com.xh.easy.trafficreplay.service.model;

import com.xh.easy.trafficreplay.service.core.handler.MethodHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 参数定义
 *
 * @author yixinhai
 */
public class ParameterInfo {

    /**
     * 方法处理器
     */
    private final MethodHandler methodHandler;

    /**
     * 参数
     */
    private final Parameter parameter;

    public ParameterInfo(MethodHandler methodHandler, Parameter parameter) {
        this.methodHandler = methodHandler;
        this.parameter = parameter;
    }

    /**
     * 参数实际名称
     */
    public String getParameterRelativeName() {
		return parameter.getName();
	}

    /**
     * 获取参数相对名称（args0, args1...）
     */
    public String getParameterAbsoluteName() {
        return parameter.getName();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Class<?> getParameterType() {
        return parameter.getType();
    }

    public Method getMethod() {
        return methodHandler.getMethod();
    }

    public MethodHandler getMethodHandler() {
        return methodHandler;
    }

    public String getMethodName() {
    	return getMethod().getName();
    }

    public boolean isPrimitive() {
        return parameter.getType().isPrimitive();
    }

    public Class<?> getTargetType() {
    	return methodHandler.getTarget().getClass();
    }
}

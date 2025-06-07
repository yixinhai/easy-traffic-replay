package com.xh.easy.trafficreplay.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数分配注解
 * 用于标识需要进行参数分配的类
 *
 * @author yixinhai
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterAllocation {
}
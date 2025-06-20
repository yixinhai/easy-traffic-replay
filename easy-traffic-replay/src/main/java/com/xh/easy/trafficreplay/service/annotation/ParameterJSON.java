package com.xh.easy.trafficreplay.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加到方法上，将JSON字符串序列化为参数对象
 *
 * @author yixinhai
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterJSON {

    /**
     * 参数默认值
     */
    String jsonValue() default "";
}

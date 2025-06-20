package com.xh.easy.trafficreplay.service.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * class工具类
 *
 * @author yixinhai
 */
public class ClassWrapper {

    public static Object newInstance(Class<?> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }

    public static Object newInstance(Class<?> clazz, Class<?>[] parameterTypes, Object[] args) throws Exception {
        return clazz.getDeclaredConstructor(parameterTypes).newInstance(args);
    }

    public static Method getAccessibelDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes)
        throws NoSuchMethodException {

        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);

        // 设置方法可访问
        method.setAccessible(true);

        return method;
    }

    public static <T extends Annotation> T getDeclaredAnnotation(Method method, Class<T> annotationClass) {
        return method.getDeclaredAnnotation(annotationClass);
    }
}

package com.xh.easy.trafficreplay.service.core.allocator;

import com.xh.easy.trafficreplay.service.annotation.ParameterAllocation;
import com.xh.easy.trafficreplay.service.annotation.ParameterValue;
import com.xh.easy.trafficreplay.service.core.handler.MethodInfo;
import com.xh.easy.trafficreplay.service.util.PrimitiveUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * 参数分配器
 *
 * @author yixinhai
 */
public class ParameterAllocator extends Allocator {

    private Object[] args;

    public ParameterAllocator(MethodInfo methodInfo) {
        super(methodInfo);
    }

    public static ParameterAllocator of(MethodInfo methodInfo) {
        return new ParameterAllocator(methodInfo);
    }

    /**
     * 分配参数
     */
    @Override
    public void allocate() throws IllegalAccessException {
        Parameter[] parameters = methodInfo.getMethod().getParameters();
        if (parameters == null || parameters.length == 0) {
            this.allocateComplete();
            return;
        }

        this.args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> parameterClass = parameter.getType();

            // 参数是logStr特殊处理
            if (String.class == parameter.getType() && "logStr".equals(parameter.getName())) {
                args[i] = "after.sale.traffic.replay-" + System.currentTimeMillis();
                this.allocateComplete();
                continue;
            }

            // 参数是基本类型，直接给默认值
            if (parameterClass.isPrimitive()) {
                // 如果参数本身是基本类型，直接设置默认值
                args[i] = this.getPrimitiveDefaultValue(parameterClass);
                this.allocateComplete();
                continue;
            }

            // 其他类型判断是否有注解
            ParameterAllocation parameterAllocation = parameterClass.getDeclaredAnnotation(ParameterAllocation.class);
            // 没有注解直接返回
            if (Objects.isNull(parameterAllocation)) {
                continue;
            }

            // 有注解先创建空对象回调一次
            Object o = this.getEntityDefaultValue(parameterClass);
            args[i] = o;
            this.allocateComplete();

            // 再判断对象属性是否有注解
            Field[] declaredFields = parameterClass.getDeclaredFields();
            for (Field field : declaredFields) {
                ParameterValue parameterValue = field.getDeclaredAnnotation(ParameterValue.class);

                if (Objects.isNull(parameterValue)) {
                    continue;
                }

                String value = parameterValue.value();
                Class<?> fieldType = field.getType();
                field.setAccessible(true);

                if (PrimitiveUtil.isPrimitive(fieldType)) {
                    field.set(o, this.getPrimitiveValue(fieldType, value));
                } else {
                    field.set(o, this.getEntityValue(fieldType, value));
                }
                this.allocateComplete();
            }
        }
    }

    /**
     * 获取实体类型的默认值
     *
     * @return 实体的空对象
     */
    private Object getEntityDefaultValue(Class<?> clazz) {
        return this.getEntityValue(clazz, null);
    }

    /**
     * 获取实体类型的默认值
     *
     * @return 实体的空对象
     */
    private Object getEntityValue(Class<?> clazz, String value) {
        try {
            if (clazz.equals(String.class)) {
                return value == null || value.isEmpty() ? null : value;
            }

            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取基本类型的默认值
     *
     * @return 基本类型的默认值
     */
    private Object getPrimitiveDefaultValue(Class<?> clazz) {
        return this.getPrimitiveValue(clazz, null);
    }

    /**
     * 获取基本类型的默认值
     *
     * @return 基本类型的默认值
     */
    private Object getPrimitiveValue(Class<?> clazz, String value) {
        return value == null || value.isEmpty() ? PrimitiveUtil.getDefaultValue(clazz)
            : PrimitiveUtil.setValue(clazz, value);
    }

    private void allocateComplete() {
        this.accept();
    }

    @Override
    public Object invoke() throws Exception {

        Object target = methodInfo.getTarget();
        Method method = methodInfo.getMethod();

        return method.invoke(target, args);
    }
}

package com.xh.easy.trafficreplay.service.core.allocator.parameter.method;

import com.xh.easy.trafficreplay.service.annotation.ParameterAllocation;
import com.xh.easy.trafficreplay.service.annotation.ParameterValue;
import com.xh.easy.trafficreplay.service.model.ParameterInfo;
import com.xh.easy.trafficreplay.service.util.BeanUtil;
import com.xh.easy.trafficreplay.service.util.ClassWrapper;
import com.xh.easy.trafficreplay.service.util.PrimitiveUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

/**
 * 带注解对象参数分配策略
 *
 * @author yixinhai
 */
@Slf4j
public class ObjectTransformer extends MethodParamTransformer {

    @Override
    public boolean supports(ParameterInfo parameterInfo) {
        Class<?> parameterClass = parameterInfo.getParameterType();
        return parameterClass.getDeclaredAnnotation(ParameterAllocation.class) != null;
    }

    @Override
    public List<Object> transform(ParameterInfo parameterInfo) throws Exception {

        List<Object> args = new ArrayList<>();
        Class<?> parameterClass = parameterInfo.getParameterType();

        // 创建对象实例
        Object instance = getEntityValue(parameterClass);
        args.add(instance);

        // 处理对象字段
        args.addAll(transformFields(instance, parameterInfo));

        return args;
    }

    /**
     * 获取对象字段值
     */
    private List<Object> transformFields(Object o, ParameterInfo parameterInfo) {

        List<Object> args = new ArrayList<>();
        Class<?> parameterClass = parameterInfo.getParameterType();

        // 处理对象字段
        for (Field field : parameterClass.getDeclaredFields()) {
            try {
                ParameterValue parameterValue = field.getDeclaredAnnotation(ParameterValue.class);

                if (Objects.isNull(parameterValue)) {
                    continue;
                }

                o = BeanUtil.deepCopy(o, parameterClass);
                Class<?> fieldType = field.getType();
                String value = parameterValue.value();
                field.setAccessible(true);

                if (PrimitiveUtil.isPrimitive(fieldType)) {
                    field.set(o, getPrimitiveValue(fieldType, value));
                } else {
                    field.set(o, getEntityValue(fieldType, value));
                }

                args.add(o);
            } catch (Exception e) {
                log.error("{} Failed to set field value. Class:{} Method:{} Argument:{} Field:{}", LOG_STR,
                    parameterInfo.getTargetType().getName(), parameterInfo.getMethodName(),
                    parameterInfo.getParameter(), field.getName(), e);
            }
        }

        return args;
    }

    /**
     * 获取基本类型值
     */
    private Object getPrimitiveValue(Class<?> clazz, String value) {
        return value == null || value.isEmpty() ? PrimitiveUtil.getDefaultValue(clazz)
            : PrimitiveUtil.setValue(clazz, value);
    }

    /**
     * 获取实体类型值
     */
    private Object getEntityValue(Class<?> clazz) {
        return this.getEntityValue(clazz, null);
    }

    /**
     * 获取实体类型值
     */
    private Object getEntityValue(Class<?> clazz, String value) {
        try {
            if (clazz.equals(String.class)) {
                return value == null || value.isEmpty() ? null : value;
            }
            return ClassWrapper.newInstance(clazz);
        } catch (Exception e) {
            log.error("{} Failed to create an instance of class: {}", LOG_STR, clazz.getName(), e);
            throw new RuntimeException(e);
        }
    }
}
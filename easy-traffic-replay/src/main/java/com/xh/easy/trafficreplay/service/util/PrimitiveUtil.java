package com.xh.easy.trafficreplay.service.util;

import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.function.Function;

/**
 * 基本类型处理器
 *
 * @author yixinhai
 */
public class PrimitiveUtil {

    /**
     * 判断是否是基本类型
     *
     * @param type Class对象
     * @return 是否为基本类型
     */
    public static boolean isPrimitive(Class<?> type) {
        if (type == null) {
            return false;
        }

        if (type.isPrimitive()) {
            return true;
        }

        return PrimitiveType.of(type) != null;
    }

    /**
     * 获取基本类型的默认值
     *
     * @param type 基本类型Class对象
     * @return 基本类型的默认值
     */
    public static Object getDefaultValue(Class<?> type) {
        return Objects.equals(type, Boolean.TYPE) ? false : 0;
    }

    /**
     * 设置基本类型的值
     *
     * @param type 基本类型Class对象
     * @param value 值
     * @return 基本类型的值
     */
    public static Object setValue(Class<?> type, String value) {
        PrimitiveType primitiveType = PrimitiveType.of(type);

        if (primitiveType == null) {
            String typeName = type == null ? "unknown" : type.getName();
            throw new IllegalArgumentException("Unknown data type: " + typeName);
        }

        return primitiveType.converter.apply(value);
    }


    private enum PrimitiveType {
        INTEGER(Integer.class, Integer::parseInt),
        LONG(Long.class, Long::parseLong),
        FLOAT(Float.class, Float::parseFloat),
        DOUBLE(Double.class, Double::parseDouble),
        BOOLEAN(Boolean.class, Boolean::parseBoolean),
        CHAR(Character.class, s -> s.charAt(0)),
        BYTE(Byte.class, Byte::parseByte),
        SORT(Short.class, Short::parseShort);

        private final Class<?> type;
        private final Function<String, ?> converter;

        PrimitiveType(Class<?> type, Function<String, ?> converter) {
            this.type = type;
            this.converter = converter;
        }

        /**
         * 获取基本类型的枚举
         *
         * @param type Class对象
         * @return 基本类型的枚举
         */
        public static PrimitiveType of(Class<?> type) {
            if (type == null) {
                return null;
            }

            for (PrimitiveType primitiveType : values()) {
                if (Objects.equals(primitiveType.type, type)) {
                    return primitiveType;
                }
            }

            return null;
        }
    }
}

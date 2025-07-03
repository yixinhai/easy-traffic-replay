package com.xh.easy.trafficreplay.service.util;

import java.util.Objects;
import java.util.function.Function;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

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
     * 判断是否是基本类型
     *
     * @param type 基本类型的名称
     * @return 是否为基本类型
     */
    public static boolean isPrimitive(String type) {
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
     * 获取基本类型的默认值
     *
     * @param type 基本类型的名称
     * @return 基本类型的默认值
     */
    public static Object getDefaultValue(String type) {
        PrimitiveType primitiveType = PrimitiveType.of(type);
        return primitiveType == null ? null : getDefaultValue(primitiveType.primitiveType);
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
            throw new IllegalArgumentException(LOG_STR + "Unknown data type: " + type.getName());
        }

        return primitiveType.converter.apply(value);
    }

    /**
     * 获取基本类型的包装类型
     *
     * @param type
     *     基本类型Class名称
     * @return 基本类型的包装类型
     */
    public static Class<?> parsePrimitive(String type) {
        PrimitiveType primitiveType = PrimitiveType.of(type);
        return primitiveType == null ? null : primitiveType.primitiveType;
    }

    private enum PrimitiveType {
        INTEGER(int.class, Integer.class, "int", Integer::parseInt),
        LONG(long.class, Long.class, "long", Long::parseLong),
        FLOAT(float.class, Float.class, "float", Float::parseFloat),
        DOUBLE(double.class, Double.class, "double", Double::parseDouble),
        BOOLEAN(boolean.class, Boolean.class, "boolean", Boolean::parseBoolean),
        CHAR(char.class, Character.class, "char", s -> s.charAt(0)),
        BYTE(byte.class, Byte.class, "byte", Byte::parseByte),
        SHORT(short.class, Short.class, "short", Short::parseShort);

        private final Class<?> primitiveType;
        private final Class<?> wrapperType;
        private final String name;
        private final Function<String, ?> converter;

        PrimitiveType(Class<?> primitiveType, Class<?> wrapperType, String name, Function<String, ?> converter) {
            this.primitiveType = primitiveType;
            this.wrapperType = wrapperType;
            this.name = name;
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
                if (primitiveType.primitiveType.equals(type) || primitiveType.wrapperType.equals(type)) {
                    return primitiveType;
                }
            }

            return null;
        }

        /**
         * 获取基本类型的枚举
         *
         * @param name
         *     基本类型的名称
         * @return 基本类型的枚举
         */
        public static PrimitiveType of(String name) {
            if (name == null) {
                return null;
            }

            for (PrimitiveType primitiveType : values()) {
                if (primitiveType.name.equals(name)) {
                    return primitiveType;
                }
            }

            return null;
        }
    }
}

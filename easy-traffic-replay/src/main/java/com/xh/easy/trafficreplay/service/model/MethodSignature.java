package com.xh.easy.trafficreplay.service.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

/**
 * 方法签名
 *
 * @author yixinhai
 */
@Slf4j
public class MethodSignature {

    /**
     * 类名
     */
    private final String className;

    /**
     * 方法名
     */
    private final String methodName;

    /**
     * 参数类型
     */
    private final String[] paramTypes;

    private MethodSignature(String className, String methodName, String[] paramTypes) {
        this.className = className;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParamTypes() {
        return paramTypes;
    }

    /**
     * 解析方法签名
     * 格式：com.example.Class#methodName(paramType1, paramType2)
     *
     * @param signature
     *     方法签名
     * @return 方法签名对象
     */
    public static MethodSignature parse(String signature) {

        if (signature == null || signature.isEmpty()) {
            return null;
        }

        return parseSignature(signature);
    }

    /**
     * 解析方法签名
     *
     * @param signature
     *     方法签名
     * @return 方法签名对象
     */
    private static MethodSignature parseSignature(String signature) {
        try {
            // 分割类名和方法签名
            String[] parts = signature.split("#");
            if (parts.length != 2) {
                return null;
            }

            String className = parts[0].trim();
            String methodPart = parts[1].trim();

            // 提取方法名和参数列表
            int paramStart = methodPart.indexOf('(');
            int paramEnd = methodPart.lastIndexOf(')');

            String methodName = null;
            String paramString = null;

            if (paramStart == -1 || paramEnd == -1 || paramStart >= paramEnd) {
                methodName = methodPart;
            } else {
                methodName = methodPart.substring(0, paramStart)
                    .trim();
                paramString = methodPart.substring(paramStart + 1, paramEnd)
                    .trim();
            }

            // 解析参数类型
            String[] paramTypes = paramString == null || paramString.isEmpty() ? new String[0] : Arrays.stream(
                paramString.split(","))
                .map(String::trim)
                .toArray(String[]::new);

            return new MethodSignature(className, methodName, paramTypes);
        } catch (Exception e) {
            log.error("{} Failed to parse method signature: {}", LOG_STR, signature, e);
            return null;
        }
    }
}

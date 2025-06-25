package com.xh.easy.trafficreplay.plugin.idea.util;

import com.xh.easy.trafficreplay.plugin.idea.service.MethodDataService;

import java.util.Map;
import java.util.Set;

/**
 * 插件数据访问器
 * 提供获取窗口输入的JSON和选择的目标方法的API
 * 
 * @author yixinhai
 */
public class PluginDataAccessor {

    private static final MethodDataService methodDataService = MethodDataService.getInstance();

    /**
     * 获取最近选择的方法签名
     * 
     * @return 方法签名，格式为：com.example.Class#methodName(paramType1, paramType2)
     */
    public static String getLastSelectedMethod() {
        return methodDataService.getLastSelectedMethod();
    }

    /**
     * 获取最近选择方法对应的JSON内容
     * 
     * @return JSON字符串内容
     */
    public static String getLastSelectedJson() {
        return methodDataService.getLastSelectedJson();
    }

    /**
     * 获取指定方法的JSON内容
     * 
     * @param methodSignature
     *     方法签名
     * @return JSON字符串内容，如果不存在则返回null
     */
    public static String getJsonByMethod(String methodSignature) {
        return methodDataService.getJsonContent(methodSignature);
    }

    /**
     * 获取所有保存的方法签名
     * 
     * @return 所有方法签名的集合
     */
    public static Set<String> getAllMethods() {
        return methodDataService.getAllMethods();
    }

    /**
     * 获取所有保存的数据
     * 
     * @return 方法签名和JSON内容的映射关系
     */
    public static Map<String, String> getAllMethodData() {
        return methodDataService.getAllData();
    }

    /**
     * 检查是否有保存的数据
     * 
     * @return 如果有数据返回true，否则返回false
     */
    public static boolean hasData() {
        return methodDataService.getMethodCount() > 0;
    }

    /**
     * 检查指定方法是否有数据
     * 
     * @param methodSignature
     *     方法签名
     * @return 如果存在返回true，否则返回false
     */
    public static boolean hasMethodData(String methodSignature) {
        return methodDataService.hasMethodData(methodSignature);
    }

    /**
     * 获取保存的方法数量
     * 
     * @return 方法数量
     */
    public static int getMethodCount() {
        return methodDataService.getMethodCount();
    }

    /**
     * 打印所有保存的数据（用于调试）
     */
    public static void printAllData() {
        Map<String, String> allData = getAllMethodData();
        System.out.println("=== 插件保存的所有数据 ===");
        System.out.println("总计方法数量: " + allData.size());
        System.out.println("最近选择的方法: " + getLastSelectedMethod());
        System.out.println();

        for (Map.Entry<String, String> entry : allData.entrySet()) {
            System.out.println("方法: " + entry.getKey());
            System.out.println("JSON: " + entry.getValue());
            System.out.println("---");
        }
        System.out.println("=== 数据打印结束 ===");
    }

    /**
     * 获取最近选择方法的简要信息
     * 
     * @return 包含方法名和JSON长度的信息字符串
     */
    public static String getLastSelectedMethodInfo() {
        String method = getLastSelectedMethod();
        String json = getLastSelectedJson();

        if (method == null) {
            return "没有选择任何方法";
        }

        StringBuilder info = new StringBuilder();
        info.append("方法: ")
            .append(method);

        if (json != null) {
            info.append("\nJSON长度: ")
                .append(json.length())
                .append(" 字符");
            info.append("\nJSON预览: ")
                .append(json.length() > 100 ? json.substring(0, 100) + "..." : json);
        } else {
            info.append("\n没有JSON数据");
        }

        return info.toString();
    }
}
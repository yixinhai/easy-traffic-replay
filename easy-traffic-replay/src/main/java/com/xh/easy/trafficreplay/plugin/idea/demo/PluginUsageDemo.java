package com.xh.easy.trafficreplay.plugin.idea.demo;

import com.xh.easy.trafficreplay.plugin.idea.util.PluginDataAccessor;

import java.util.Map;
import java.util.Set;

/**
 * 插件使用演示类
 * 展示如何获取窗口输入的JSON和选择的目标方法
 * 
 * @author yixinhai
 */
public class PluginUsageDemo {

    /**
     * 演示基本用法
     */
    public static void basicUsage() {
        System.out.println("=== 基本用法演示 ===");

        // 1. 获取最近选择的方法
        String lastMethod = PluginDataAccessor.getLastSelectedMethod();
        System.out.println("最近选择的方法: " + lastMethod);

        // 2. 获取最近选择方法的JSON内容
        String lastJson = PluginDataAccessor.getLastSelectedJson();
        System.out.println("对应的JSON内容: " + lastJson);

        // 3. 检查是否有数据
        boolean hasData = PluginDataAccessor.hasData();
        System.out.println("是否有保存的数据: " + hasData);

        // 4. 获取保存的方法数量
        int methodCount = PluginDataAccessor.getMethodCount();
        System.out.println("保存的方法数量: " + methodCount);

        System.out.println();
    }

    /**
     * 演示如何处理特定方法的数据
     */
    public static void specificMethodUsage() {
        System.out.println("=== 特定方法处理演示 ===");

        // 获取所有保存的方法
        Set<String> allMethods = PluginDataAccessor.getAllMethods();

        if (allMethods.isEmpty()) {
            System.out.println("当前没有保存任何方法数据");
            return;
        }

        System.out.println("所有保存的方法:");
        for (String method : allMethods) {
            System.out.println("- " + method);

            // 获取该方法的JSON数据
            String json = PluginDataAccessor.getJsonByMethod(method);
            if (json != null) {
                System.out.println("  JSON长度: " + json.length() + " 字符");
                System.out.println("  JSON预览: " + (json.length() > 50 ? json.substring(0, 50) + "..." : json));
            }
        }

        System.out.println();
    }

    /**
     * 演示完整的数据处理流程
     */
    public static void completeWorkflow() {
        System.out.println("=== 完整数据处理流程演示 ===");

        // 1. 检查是否有数据
        if (!PluginDataAccessor.hasData()) {
            System.out.println("提示：请先在IDEA中右键Java方法选择'注入方法'并输入JSON数据");
            return;
        }

        // 2. 获取所有数据
        Map<String, String> allData = PluginDataAccessor.getAllMethodData();

        System.out.println("处理所有保存的方法数据:");
        for (Map.Entry<String, String> entry : allData.entrySet()) {
            String methodSignature = entry.getKey();
            String jsonContent = entry.getValue();

            System.out.println("\n处理方法: " + methodSignature);

            // 解析方法信息
            processMethodSignature(methodSignature);

            // 处理JSON数据
            processJsonData(jsonContent);
        }

        // 3. 显示最近操作的方法信息
        System.out.println("\n最近操作信息:");
        System.out.println(PluginDataAccessor.getLastSelectedMethodInfo());

        System.out.println();
    }

    /**
     * 处理方法签名
     */
    private static void processMethodSignature(String methodSignature) {
        // 解析方法签名：com.example.Class#methodName(paramType1, paramType2)
        if (methodSignature.contains("#")) {
            String[] parts = methodSignature.split("#");
            String className = parts[0];
            String methodPart = parts[1];

            String methodName;
            String parameters = "";

            if (methodPart.contains("(")) {
                int parenIndex = methodPart.indexOf("(");
                methodName = methodPart.substring(0, parenIndex);
                parameters = methodPart.substring(parenIndex);
            } else {
                methodName = methodPart;
            }

            System.out.println("  类名: " + className);
            System.out.println("  方法名: " + methodName);
            System.out.println("  参数: " + parameters);
        }
    }

    /**
     * 处理JSON数据
     */
    private static void processJsonData(String jsonContent) {
        if (jsonContent == null || jsonContent.trim()
            .isEmpty()) {
            System.out.println("  JSON数据为空");
            return;
        }

        System.out.println("  JSON数据长度: " + jsonContent.length() + " 字符");

        // 简单的JSON格式检查
        String trimmed = jsonContent.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            System.out.println("  JSON格式: 对象");
        } else if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            System.out.println("  JSON格式: 数组");
        } else {
            System.out.println("  JSON格式: 可能不是标准JSON格式");
        }

        // 这里可以添加更多的JSON处理逻辑
        // 例如：使用fastjson或其他JSON库解析数据
    }

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        System.out.println("插件数据访问演示");
        System.out.println("==================");

        basicUsage();
        specificMethodUsage();
        completeWorkflow();

        // 打印调试信息
        PluginDataAccessor.printAllData();
    }
}
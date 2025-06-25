package com.xh.easy.trafficreplay.plugin.idea.test;

import com.xh.easy.trafficreplay.plugin.idea.service.MethodDataService;
import com.xh.easy.trafficreplay.plugin.idea.util.PluginDataAccessor;

/**
 * 简单测试类，演示插件功能
 * 
 * @author yixinhai
 */
public class SimpleTest {

    public static void main(String[] args) {
        System.out.println("====== Easy Traffic Replay 插件功能测试 ======");

        // 1. 模拟用户选择方法并输入JSON数据
        simulateUserInput();

        // 2. 演示数据访问API
        demonstrateDataAccess();

        // 3. 演示完整工作流程
        demonstrateWorkflow();

        System.out.println("====== 测试完成 ======");
    }

    /**
     * 模拟用户输入
     */
    private static void simulateUserInput() {
        System.out.println("\n1. 模拟用户操作");
        System.out.println("-------------------");

        // 模拟用户选择第一个方法并输入JSON
        String method1 = "com.example.service.UserService#getUserById(java.lang.Long)";
        String json1 = "{\n  \"id\": 12345,\n  \"name\": \"张三\",\n  \"email\": \"zhangsan@example.com\"\n}";

        MethodDataService.getInstance()
            .saveMethodData(method1, json1);
        System.out.println("✓ 保存了方法: " + method1);

        // 模拟用户选择第二个方法并输入JSON
        String method2 = "com.example.service.OrderService#createOrder(com.example.model.Order)";
        String json2 =
            "{\n  \"orderId\": \"ORDER-001\",\n  \"amount\": 99.99,\n  \"items\": [\n    {\"name\": \"商品A\", \"quantity\": 2},\n    {\"name\": \"商品B\", \"quantity\": 1}\n  ]\n}";

        MethodDataService.getInstance()
            .saveMethodData(method2, json2);
        System.out.println("✓ 保存了方法: " + method2);
    }

    /**
     * 演示数据访问API
     */
    private static void demonstrateDataAccess() {
        System.out.println("\n2. 数据访问API演示");
        System.out.println("-------------------");

        // 获取最近选择的方法
        String lastMethod = PluginDataAccessor.getLastSelectedMethod();
        System.out.println("最近选择的方法: " + lastMethod);

        // 获取对应的JSON
        String lastJson = PluginDataAccessor.getLastSelectedJson();
        System.out.println("对应的JSON内容: " + lastJson);

        // 获取所有方法
        System.out.println("\n所有保存的方法数量: " + PluginDataAccessor.getMethodCount());

        // 检查是否有数据
        System.out.println("是否有数据: " + PluginDataAccessor.hasData());
    }

    /**
     * 演示完整工作流程
     */
    private static void demonstrateWorkflow() {
        System.out.println("\n3. 完整工作流程演示");
        System.out.println("-------------------");

        // 获取所有数据并处理
        var allData = PluginDataAccessor.getAllMethodData();

        System.out.println("处理所有保存的数据:");
        for (var entry : allData.entrySet()) {
            String methodSignature = entry.getKey();
            String jsonContent = entry.getValue();

            System.out.println("\n处理方法: " + methodSignature);

            // 解析方法信息
            parseMethodSignature(methodSignature);

            // 分析JSON数据
            analyzeJsonData(jsonContent);
        }

        // 显示汇总信息
        System.out.println("\n汇总信息:");
        System.out.println(PluginDataAccessor.getLastSelectedMethodInfo());
    }

    /**
     * 解析方法签名
     */
    private static void parseMethodSignature(String methodSignature) {
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

            System.out.println("  - 类名: " + className);
            System.out.println("  - 方法名: " + methodName);
            System.out.println("  - 参数: " + parameters);
        }
    }

    /**
     * 分析JSON数据
     */
    private static void analyzeJsonData(String jsonContent) {
        if (jsonContent == null || jsonContent.trim()
            .isEmpty()) {
            System.out.println("  - JSON数据为空");
            return;
        }

        System.out.println("  - JSON长度: " + jsonContent.length() + " 字符");

        String trimmed = jsonContent.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            System.out.println("  - JSON类型: 对象");
        } else if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            System.out.println("  - JSON类型: 数组");
        } else {
            System.out.println("  - JSON类型: 其他格式");
        }

        // 计算行数
        int lines = jsonContent.split("\n").length;
        System.out.println("  - JSON行数: " + lines);
    }
}
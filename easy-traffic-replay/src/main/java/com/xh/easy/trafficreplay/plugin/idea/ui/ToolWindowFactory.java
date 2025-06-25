package com.xh.easy.trafficreplay.plugin.idea.ui;

/**
 * 工具窗口工厂类
 * 
 * @author yixinhai
 */
public class ToolWindowFactory {

    public void createToolWindowContent(Object project, Object toolWindow) {
        // 创建默认的工具窗口内容
        JsonInputWindow jsonInputWindow = new JsonInputWindow(project, "未选择方法");

        // 模拟IDEA的工具窗口内容管理
        // 在实际的IDEA插件中，这里会使用IDEA的API
        System.out.println("创建工具窗口内容：Traffic Replay");
    }
}
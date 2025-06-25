# Easy Traffic Replay IDEA 插件使用指南

## 功能概述

Easy Traffic Replay IDEA插件为您提供了一个方便的方式来选择Java方法并输入JSON数据，用于流量回放测试。

## 核心功能

1. **右键注入方法**：在Java代码的目标方法上右键选择"注入方法"
2. **JSON输入窗口**：在IDEA右侧出现一个窗口，可以输入JSON字符串
3. **数据访问API**：通过代码获取窗口输入的JSON和选择的目标方法

## 使用步骤

### 1. 选择目标方法

在IDEA中打开Java文件，将光标放在目标方法内，然后：
- 右键点击 → 选择"注入方法"
- 或者使用快捷键（如果已配置）

### 2. 输入JSON数据

选择方法后，右侧会出现"Traffic Replay"工具窗口，包含：
- 选择的方法签名显示
- JSON输入文本区域
- 提交和清空按钮

在文本区域输入您的JSON数据，例如：
```json
{
  "name": "张三",
  "age": 25,
  "email": "zhangsan@example.com"
}
```

### 3. 获取数据

通过编程方式获取选择的方法和JSON数据：

```java
import com.xh.easy.trafficreplay.plugin.idea.util.PluginDataAccessor;

// 获取最近选择的方法
String method = PluginDataAccessor.getLastSelectedMethod();
System.out.println("选择的方法: " + method);

// 获取对应的JSON内容
String json = PluginDataAccessor.getLastSelectedJson();
System.out.println("JSON内容: " + json);

// 获取所有保存的数据
Map<String, String> allData = PluginDataAccessor.getAllMethodData();
for (Map.Entry<String, String> entry : allData.entrySet()) {
    System.out.println("方法: " + entry.getKey());
    System.out.println("JSON: " + entry.getValue());
}
```

## API 参考

### PluginDataAccessor 类

主要方法：

| 方法 | 描述 | 返回值 |
|------|------|--------|
| `getLastSelectedMethod()` | 获取最近选择的方法签名 | String |
| `getLastSelectedJson()` | 获取最近选择方法的JSON内容 | String |
| `getJsonByMethod(String)` | 获取指定方法的JSON内容 | String |
| `getAllMethods()` | 获取所有保存的方法签名 | Set<String> |
| `getAllMethodData()` | 获取所有保存的数据 | Map<String, String> |
| `hasData()` | 检查是否有保存的数据 | boolean |
| `getMethodCount()` | 获取保存的方法数量 | int |

### 方法签名格式

方法签名采用以下格式：
```
完整类名#方法名(参数类型1, 参数类型2, ...)
```

例如：
```
com.example.service.UserService#getUserById(java.lang.Long)
com.example.service.UserService#createUser(com.example.model.User)
```

## 使用示例

### 示例 1：基本使用

```java
// 检查是否有数据
if (PluginDataAccessor.hasData()) {
    String method = PluginDataAccessor.getLastSelectedMethod();
    String json = PluginDataAccessor.getLastSelectedJson();
    
    System.out.println("方法: " + method);
    System.out.println("JSON: " + json);
} else {
    System.out.println("请先在IDEA中选择方法并输入JSON数据");
}
```

### 示例 2：处理所有数据

```java
Map<String, String> allData = PluginDataAccessor.getAllMethodData();
for (Map.Entry<String, String> entry : allData.entrySet()) {
    String methodSignature = entry.getKey();
    String jsonContent = entry.getValue();
    
    // 解析方法信息
    if (methodSignature.contains("#")) {
        String[] parts = methodSignature.split("#");
        String className = parts[0];
        String methodName = parts[1].split("\\(")[0];
        
        System.out.println("处理类: " + className);
        System.out.println("处理方法: " + methodName);
        System.out.println("JSON数据: " + jsonContent);
    }
}
```

### 示例 3：结合流量回放使用

```java
import com.xh.easy.trafficreplay.plugin.idea.util.PluginDataAccessor;
import com.alibaba.fastjson.JSON;

public class TrafficReplayIntegration {
    
    public void executeReplay() {
        // 获取插件选择的方法和JSON数据
        String methodSignature = PluginDataAccessor.getLastSelectedMethod();
        String jsonContent = PluginDataAccessor.getLastSelectedJson();
        
        if (methodSignature == null || jsonContent == null) {
            System.out.println("请先选择方法并输入JSON数据");
            return;
        }
        
        // 解析JSON数据
        try {
            Object jsonObject = JSON.parse(jsonContent);
            
            // 这里可以结合现有的流量回放逻辑
            System.out.println("执行方法: " + methodSignature);
            System.out.println("使用参数: " + jsonObject);
            
            // 调用现有的ReplayExecutor或其他服务
            // replayExecutor.executeMethod(methodSignature, jsonObject);
            
        } catch (Exception e) {
            System.err.println("JSON解析失败: " + e.getMessage());
        }
    }
}
```

## 开发和调试

### 运行演示

您可以运行演示类来测试API：

```java
com.xh.easy.trafficreplay.plugin.idea.demo.PluginUsageDemo.main(String[] args)
```

### 调试信息

使用以下方法获取调试信息：

```java
// 打印所有保存的数据
PluginDataAccessor.printAllData();

// 获取最近选择方法的详细信息
String info = PluginDataAccessor.getLastSelectedMethodInfo();
System.out.println(info);
```

## 注意事项

1. **插件环境**：此插件需要在IntelliJ IDEA环境中运行
2. **Java文件**：只能在Java文件中的方法上使用右键菜单
3. **数据持久性**：数据在IDEA会话期间保存，重启后会丢失
4. **JSON格式**：请确保输入的是有效的JSON格式
5. **方法签名**：系统自动生成方法签名，包含完整的类名和参数类型

## 故障排除

### 右键菜单不显示
- 确保光标在Java方法内
- 确保文件是.java文件
- 检查插件是否正确安装

### 工具窗口不显示
- 检查IDEA右侧工具窗口栏
- 尝试从菜单 View → Tool Windows → Traffic Replay 打开

### 数据获取失败
- 确保已经通过右键菜单选择了方法
- 确保已经在工具窗口中输入并提交了JSON数据
- 检查控制台是否有错误信息

## 扩展开发

如果您需要扩展此插件的功能，可以：

1. **添加新的数据类型支持**：修改`MethodDataService`类
2. **改进UI界面**：修改`JsonInputWindow`类
3. **增加验证功能**：在JSON输入时添加格式验证
4. **持久化存储**：将数据保存到文件或数据库

## 联系支持

如有问题或建议，请联系：
- 邮箱：support@xh.com
- 项目地址：https://github.com/xh/easy-traffic-replay 
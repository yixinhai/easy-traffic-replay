# Easy Traffic Replay

Easy Traffic Replay 是一个轻量级的流量回放工具，用于模拟和重放系统流量，帮助开发人员进行系统测试和性能评估。

## 功能特性

- 支持方法级别的流量回放
- 灵活的参数分配机制
- 可配置的回放策略
- 详细的执行日志记录
- 支持 Apollo 配置中心集成

## 技术栈

- Java 17
- Spring Boot 2.7.5
- Maven
- Lombok
- Log4j2

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本

## 项目结构

#### 1. 整体项目结构
```
easy-traffic-replay/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/xh/easy/trafficreplay/
│   │   │       ├── agent/        # 代理相关代码
│   │   │       ├── service/      # 服务层代码
│   │   │       └── EasyTrafficReplayApplication.java
│   │   └── resources/
│   └── test/
```

#### 2. Service 包详细结构
```
service/
├── model/                 # 数据模型
│   └── MethodSignature.java  # 方法签名模型
├── core/                 # 核心功能实现
│   ├── executor/         # 执行器
│   │   ├── ReplayExecutor.java  # 回放执行器
│   │   └── Visitor.java        # 访问者接口
│   ├── handler/          # 处理器
│   │   ├── MethodHandler.java  # 方法处理器
│   │   ├── MethodInfo.java     # 方法信息
│   │   └── MethodInvoker.java  # 方法调用器
│   └── allocator/        # 分配器
│       ├── Allocator.java      # 分配器接口
│       └── ParameterAllocator.java  # 参数分配器
├── manager/              # 管理器
│   ├── ApolloConfigs.java     # Apollo配置
│   ├── MethodManager.java     # 方法管理器
│   └── ReplayConfig.java      # 回放配置
├── annotation/           # 注解
│   ├── ParameterAllocation.java  # 参数分配注解
│   └── ParameterValue.java       # 参数值注解
├── test/                 # 测试相关
│   ├── Test.java
│   └── TestAssemble.java
└── util/                 # 工具类
    └── PrimitiveUtil.java      # 基本类型工具
```

#### 3. Service 核心类图
![image](https://github.com/user-attachments/assets/271ff48d-4254-4229-ac55-c3de4c0ee75a)


### 1. 核心功能

1. **方法级流量回放**
   - 支持对指定方法进行流量回放
   - 可以配置多个方法同时回放
   - 支持从配置文件或 Apollo 配置中心读取回放配置

2. **参数分配机制**
   - 通过 `@ParameterAllocation` 注解标记需要参数分配的类
   - 使用 `@ParameterValue` 注解为字段指定默认值
   - 支持运行时动态分配参数

3. **配置管理**
   - 支持本地配置文件配置
   - 支持 Apollo 配置中心集成
   - 配置格式：`全限定类名#方法名`

### 2. 使用方法

#### 2.1 添加依赖

在项目的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.xh.easy</groupId>
    <artifactId>easy-traffic-replay</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### 2.2 配置目标方法

##### 方式一：通过配置文件

在`application.yaml`中配置：

```yaml
replay:
  methods:
    - com.example.service.TestService#method1()
    - com.example.service.TestService#method2(java.lang.String)
    - com.example.service.TestService#method3(java.lang.String, com.example.model.User)
```

##### 方式二：通过Apollo配置中心

1. 在`application.yaml`中启用Apollo配置：

```yaml
replay:
  apollo:
    enabled: true
    key: your_config_key
    namespace: application
```

2. 在Apollo配置中心添加配置：
   - Key: `your_config_key`
   - Value: 方法列表（JSON数组格式）
   ```json
   [
     "com.example.service.TestService#method1()",
     "com.example.service.TestService#method2(java.lang.String)"
   ]
   ```

#### 2.3. 参数配置

##### 基本类型参数

基本类型参数会自动分配默认值：
- 数值类型：0
- 布尔类型：false

##### 复杂对象参数

1. 在类上添加`@ParameterAllocation`注解：
```java
@ParameterAllocation
public class User {
    // ...
}
```

2. 在需要设置默认值的字段上添加`@ParameterValue`注解：
```java
@ParameterAllocation
public class User {
    @ParameterValue("defaultName")
    private String name;
    
    @ParameterValue("18")
    private Integer age;
}
```

#### 2.4 执行回放

```java
@Autowired
private ReplayExecutor replayExecutor;

public void executeReplay() {
    replayExecutor.executeAll();
}
```

### 3. 最佳实践

1. **参数配置**
   - 为关键参数设置合理的默认值
   - 使用 `@ParameterValue` 注解时提供有意义的默认值
   - 考虑参数之间的依赖关系

2. **方法选择**
   - 选择具有代表性的方法进行回放
   - 确保方法参数类型可序列化
   - 避免选择副作用较大的方法

3. **配置管理**
   - 优先使用 Apollo 配置中心进行动态配置
   - 合理组织配置项，便于管理和维护
   - 定期检查和更新配置

4. **监控和日志**
   - 关注执行日志，及时发现异常
   - 监控回放执行结果
   - 记录关键指标和异常情况

### 4. 注意事项

1. 确保回放方法的方法签名完整且准确
2. 注意参数类型的正确性
3. 避免在回放过程中修改系统状态
4. 合理控制回放频率，避免对系统造成压力
5. 建议在测试环境进行回放测试

这个工具主要用于系统测试和性能评估，通过模拟真实流量来验证系统的稳定性和性能。

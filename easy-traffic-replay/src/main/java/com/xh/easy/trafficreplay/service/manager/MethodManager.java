package com.xh.easy.trafficreplay.service.manager;

import com.xh.easy.trafficreplay.service.method.MethodInfo;
import com.xh.easy.trafficreplay.service.method.MethodSignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 流量回放方法管理类
 *
 * @author yixinhai
 */
@Slf4j
@Component
public class MethodManager {

    /**
     * 存储所有配置的方法
     * key: 方法标识（类名#方法名(参数类型列表)）
     * value: 方法信息（包含目标对象和方法）
     */
    private final Map<String, MethodInfo> methodMap = new HashMap<>();

    @Autowired
    private ReplayConfig replayConfig;

    @Autowired
    private ApplicationContext applicationContext;

    public MethodManager() {
        log.info("Initializing method manager...");
    }

    @PostConstruct
    private void init() {
        log.info("method manager init...");
        if (replayConfig.getMethods() == null || replayConfig.getMethods().isEmpty()) {
            return;
        }

        for (String methodConfig : replayConfig.getMethods()) {
            try {
                if (methodMap.get(methodConfig) != null) {
                    continue;
                }

                // 解析方法配置
                MethodSignature signature = MethodSignature.parse(methodConfig);
                if (signature == null) {
                    log.error("Invalid method configuration: {}", methodConfig);
                    continue;
                }

                // 获取类
                Class<?> targetClass = Class.forName(signature.getClassName());

                // 查找对应的Spring Bean
                Object target = findBeanByClass(targetClass);
                if (target == null) {
                    log.error("No Spring bean found for class: {}", signature.getClassName());
                    continue;
                }

                // 获取方法
                Method method = findMethod(targetClass, signature);
                if (method == null) {
                    log.error("Method not found: {} in class: {}", methodConfig, signature.getClassName());
                    continue;
                }

                // 存储方法信息
                methodMap.put(methodConfig, new MethodInfo(target, method));
            } catch (Exception e) {
                log.error("Failed to initialize method: {}", methodConfig, e);
            }
        }
    }

    /**
     * 根据类查找对应的Spring Bean
     */
    private Object findBeanByClass(Class<?> targetClass) {
        String[] beanNames = applicationContext.getBeanNamesForType(targetClass);
        if (beanNames.length > 0) {
            return applicationContext.getBean(beanNames[0]);
        }
        return null;
    }

    /**
     * 在类中查找指定名称和参数类型的方法
     */
    private Method findMethod(Class<?> targetClass, MethodSignature signature) {
        try {
            // 获取所有参数类型的Class对象
            Class<?>[] paramTypes = Arrays.stream(signature.getParamTypes())
                .map(paramType -> {
                    try {
                        return Class.forName(paramType);
                    } catch (ClassNotFoundException e) {
                        log.error("Parameter type not found: {}", paramType);
                        return null;
                    }
                })
                .toArray(Class<?>[]::new);

            // 查找匹配的方法
            Method method = targetClass.getDeclaredMethod(signature.getMethodName(), paramTypes);

            // 设置方法可访问
            method.setAccessible(true);

            return method;
        } catch (NoSuchMethodException e) {
            log.error("Method not found: {} with parameters: {}",
                signature.getMethodName(),
                Arrays.toString(signature.getParamTypes()));
            return null;
        }
    }

    public Map<String, MethodInfo> getMethodMap() {
        return this.methodMap;
    }

    /**
     * 监听Apollo配置变更
     */
    //    @ApolloConfigChangeListener("application")
//    public void onChange(ConfigChangeEvent event) {
//        if (event.isChanged("ass_traffic_replay")) {
//            this.init();
//        }
//    }
}

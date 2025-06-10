package com.xh.easy.trafficreplay.service.manager;

import com.alibaba.fastjson.JSON;
import com.xh.easy.trafficreplay.service.core.handler.MethodInfo;
import com.xh.easy.trafficreplay.service.model.MethodSignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

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
    private Map<String, MethodInfo> methodMap;

    @Autowired
    private ReplayConfig replayConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApolloConfigs apolloConfigs;


    @PostConstruct
    private void init() {
        log.info("{} Initialize method manager", LOG_STR);

        // 初始化回放方法信息
        initMethodMap();

        // 监听apollo配置变更，重新初始化方法信息
        apolloConfigs.changeListener(this::initMethodMap);
    }

    /**
     * 初始化回放方法信息
     */
    private void initMethodMap() {
        log.info("{} Initialize method information", LOG_STR);

        methodMap = new HashMap<>();

        Set<String> methods = replayConfig.getRelayMethods();
        if (methods == null || methods.isEmpty()) {
            return;
        }

        for (String methodConfig : methods) {
            try {
                if (methodMap.get(methodConfig) != null) {
                    continue;
                }

                // 解析方法配置
                MethodSignature signature = MethodSignature.parse(methodConfig);
                if (signature == null) {
                    log.error("{} Invalid method configuration: {}", LOG_STR, methodConfig);
                    continue;
                }

                // 获取类
                Class<?> targetClass = Class.forName(signature.getClassName());

                // 查找对应的Spring Bean
                Object target = findBeanByClass(targetClass);
                if (target == null) {
                    log.error("{} No Spring bean found for class: {}", LOG_STR, signature.getClassName());
                    continue;
                }

                // 获取方法
                Method method = findMethod(targetClass, signature);
                if (method == null) {
                    log.error("{} Method not found: {} in class: {}", LOG_STR, methodConfig, signature.getClassName());
                    continue;
                }

                // 存储方法信息
                methodMap.put(methodConfig, new MethodInfo(target, method));
            } catch (Exception e) {
                log.error("{} Failed to initialize method: {}", LOG_STR, methodConfig, e);
            }
        }

        log.info("{} Initialized {} methods methods={}", LOG_STR, methodMap.size(), JSON.toJSONString(methodMap));
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
                        log.error("{} Parameter type not found: {}", LOG_STR, paramType);
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
            log.error("{} Method not found: {} with parameters: {}", LOG_STR, signature.getMethodName(),
                Arrays.toString(signature.getParamTypes()));
            return null;
        }
    }

    public Map<String, MethodInfo> getMethodMap() {
        return this.methodMap;
    }
}

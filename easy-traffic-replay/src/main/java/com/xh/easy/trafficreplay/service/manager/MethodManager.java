package com.xh.easy.trafficreplay.service.manager;

import com.xh.easy.trafficreplay.service.core.handler.MethodInfo;
import com.xh.easy.trafficreplay.service.model.MethodSignature;
import com.xh.easy.trafficreplay.service.util.ClassWrapper;
import com.xh.easy.trafficreplay.service.util.PrimitiveUtil;
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
                    log.error("{} Class not found for class: {}", LOG_STR, signature.getClassName());
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

        log.info("{} Initialized {} methods", LOG_STR, methodMap.size());
    }

    /**
     * 根据类查找对应的实例
     * 优先从Spring容器中获取Bean，如果不存在则创建新实例
     *
     * @param targetClass 目标类
     * @return 目标类的实例，如果创建失败则返回null
     */
    private Object findBeanByClass(Class<?> targetClass) {
        if (targetClass == null) {
            log.warn("{} Target class is null", LOG_STR);
            return null;
        }

        // 1. 优先从Spring容器获取Bean
        Object instance = getBeanFromSpringContainer(targetClass);
        if (instance != null) {
            return instance;
        }

        // 2. 如果Spring容器中不存在，则创建新实例
        return createNewInstance(targetClass);
    }

    /**
     * 从Spring容器中获取Bean
     *
     * @param targetClass 目标类
     * @return Spring容器中的Bean实例，如果不存在则返回null
     */
    private Object getBeanFromSpringContainer(Class<?> targetClass) {
        try {
            String[] beanNames = applicationContext.getBeanNamesForType(targetClass);
            if (beanNames.length > 0) {
                Object bean = applicationContext.getBean(beanNames[0]);
                log.info("{} Found Spring bean for class: {}", LOG_STR, targetClass.getName());
                return bean;
            }
        } catch (Exception e) {
            log.warn("{} Failed to get bean from Spring container for class: {}", LOG_STR, targetClass.getName(), e);
        }
        return null;
    }

    /**
     * 创建类的新实例
     *
     * @param targetClass 目标类
     * @return 新创建的实例，如果创建失败则返回null
     */
    private Object createNewInstance(Class<?> targetClass) {
        try {
            Object instance = ClassWrapper.newInstance(targetClass);
            log.info("{} Created new instance for class: {}", LOG_STR, targetClass.getName());
            return instance;
        } catch (Exception e) {
            log.error("{} Failed to create instance for class: {}", LOG_STR, targetClass.getName(), e);
            return null;
        }
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
                        return ClassWrapper.forName(paramType);
                    } catch (ClassNotFoundException e) {
                        log.error("{} Parameter type not found: {}", LOG_STR, paramType);
                        return null;
                    }
                })
                .toArray(Class<?>[]::new);

            // 查找匹配的方法
            return ClassWrapper.getAccessibelDeclaredMethod(targetClass, signature.getMethodName(), paramTypes);
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

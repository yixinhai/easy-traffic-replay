package com.xh.easy.trafficreplay.service.util;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;

/**
 * Apollo配置相关工具类
 *
 * @author yixinhai
 */
public class ApolloUtil {

    /**
     * 根据键获取 apollo 中的配置信息
     *
     * @param key    配置的key
     * @return T  与 key 对应的配置信息
     */
    public static <T> T getConfigBean(Config config, String key, Class<T> clazz) {
        if (config == null || clazz == null) {
            return null;
        }

        return JSON.parseObject(config.getProperty(key, "{}"), clazz);
    }

    /**
     * 根据键获取 apollo 中的配置信息
     *
     * @param key    配置的key
     * @return T  与 key 对应的配置信息
     */
    public static <T> List<T> getConfigBeanList(Config config, String key, Class<T> clazz) {
        if (config == null || clazz == null) {
            return new ArrayList<>(0);
        }

        return JSON.parseArray(config.getProperty(key, "[]"), clazz);
    }

    /**
     * 添加配置监听器
     *
     * @param config    Apollo配置
     * @param key       配置的key
     * @param listener  监听器
     */
    public static void addChangeListener(Config config, String key, ConfigChangeListener listener) {
        if (config == null || listener == null) {
            return;
        }

        if (key == null || key.isEmpty()) {
            config.addChangeListener(listener);
        } else {
            config.addChangeListener(listener, Sets.newHashSet(key));
        }
    }

    /**
     * 添加配置监听器
     *
     * @param config    Apollo配置
     * @param key       配置的key
     * @param runnable  监听器
     */
    public static void addChangeListener(Config config, String key, Runnable runnable) {
        if (config == null || runnable == null) {
            return;
        }

        if (key == null || key.isEmpty()) {
            config.addChangeListener(event -> runnable.run());
        } else {
            config.addChangeListener(event -> runnable.run(), Sets.newHashSet(key));
        }
    }
}

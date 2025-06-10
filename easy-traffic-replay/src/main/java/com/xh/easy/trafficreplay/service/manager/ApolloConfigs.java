package com.xh.easy.trafficreplay.service.manager;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.xh.easy.trafficreplay.service.util.ApolloUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

/**
 * Apollo配置类
 *
 * @author yixinhai
 */
@Slf4j
@Setter
public class ApolloConfigs {

    private Config config;

    /**
     * Apollo 配置是否启用
     */
    private Boolean enabled;

    /**
     * Apollo 配置的命名空间
     */
    private String namespace;

    /**
     * Apollo 配置的 key
     */
    private String key;

    /**
     * apollo配置的流量回放方法
     */
    private List<String> trafficRelayMethods;

    @PostConstruct
    private void init() {
        log.info("{} Initialize apollo configs", LOG_STR);

        if (!checkApolloEnabled()) {
            return;
        }

        try {

            // 初始化 apollo 配置
            initApolloConfig();
            assert config != null;

            // 初始化流量回放方法
            initTrafficRelayMethods();
            // 监听流量回放配置变化
            changeListener(this::initTrafficRelayMethods);

        } catch (Exception e) {
            log.error("{} Failed to initialize apollo configs", LOG_STR, e);
            throw new RuntimeException(LOG_STR + "Failed to initialize apollo configs: Namespace not found", e);
        }
    }

    /**
     * 检查 apollo 配置是否启用
     *
     * @return 是否启用apollo配置
     */
    private boolean checkApolloEnabled() {

        if (enabled == null || !enabled) {
            return false;
        }

        if (key == null || key.isEmpty()) {
            return false;
        }

        if (namespace == null || namespace.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * 初始化 apollo 配置
     */
    private void initApolloConfig() {
        config = ConfigService.getConfig(namespace);
    }

    /**
     * 初始化流量回放方法
     */
    private void initTrafficRelayMethods() {
        trafficRelayMethods = ApolloUtil.getConfigBeanList(config, key, String.class);
    }

    /**
     * 添加 apollo 配置监听器
     *
     * @param runnable 监听的回调方法
     */
    protected void changeListener(Runnable runnable) {
        ApolloUtil.addChangeListener(config, key, runnable);
    }

    /**
     * 获取流量回放方法
     *
     * @return 流量回放方法
     */
    protected List<String> getTrafficRelayMethods() {
        return Optional.ofNullable(trafficRelayMethods).orElse(new ArrayList<>(0));
    }
}

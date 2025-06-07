package com.xh.easy.trafficreplay.service.manager;

import com.xh.easy.trafficreplay.service.apollo.ApolloConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * 回放方法配置类
 *
 * @author yixinhai
 */
@Configuration
@ConfigurationProperties(prefix = "replay")
public class ReplayConfig {

    @Autowired
    private ApolloConfigs apolloConfigs;
    
    /**
     * 需要回放的方法列表
     * 格式：全限定类名#方法名
     * 例如：com.zhuanzhuan.ass.config.center.assemble.test.TestAssemble#test1(java.lang.String, com.bj58.zhuanzhuan.kf.zzkfassdao.entity.AssOrderEntity)
     */
    private Set<String> methods;

    public Set<String> getMethods() {
        Set<String> methodSet = new HashSet<>(methods);
        methodSet.addAll(this.getTrafficRelayMethods());
        return methodSet;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    private List<String> getTrafficRelayMethods() {
        return Optional.ofNullable(apolloConfigs.getTrafficRelayMethods()).orElse(new ArrayList<>(0));
    }
}
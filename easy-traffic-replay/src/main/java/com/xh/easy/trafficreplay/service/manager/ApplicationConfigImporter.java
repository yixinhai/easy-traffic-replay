package com.xh.easy.trafficreplay.service.manager;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.HashSet;

/**
 * 配置类引入
 *
 * @author yixinhai
 */
@Configuration
public class ApplicationConfigImporter {

    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ReplayConfig replayConfig(Environment env) {
        ReplayConfig config = new ReplayConfig();

        config.setMethods(Binder.get(env)
            .bind("replay.methods", Bindable.setOf(String.class))
            .orElse(new HashSet<>(0)));

        return config;
    }

    @Bean
    public ApolloConfigs apolloConfigs(Environment env) {
        ApolloConfigs apollo = new ApolloConfigs();

        apollo.setEnabled(env.getProperty("replay.apollo.enabled", Boolean.class, false));
        apollo.setKey(env.getProperty("replay.apollo.key"));
        apollo.setNamespace(env.getProperty("replay.apollo.namespace"));

        return apollo;
    }
}

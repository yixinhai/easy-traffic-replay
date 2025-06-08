package com.xh.easy.trafficreplay.service.manager;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yixinhai
 */
@Getter
@Component
//@EnableApolloConfig("application")
public class ApolloConfigs {

//    @ApolloJsonValue("${ass_traffic_replay:[]}")
    public List<String> trafficRelayMethods;
}

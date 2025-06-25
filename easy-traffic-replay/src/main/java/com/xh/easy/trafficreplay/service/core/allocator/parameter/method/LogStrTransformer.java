package com.xh.easy.trafficreplay.service.core.allocator.parameter.method;

import com.xh.easy.trafficreplay.service.model.ParameterInfo;

import java.util.List;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

/**
 * logStr参数分配策略
 *
 * @author yixinhai
 */
public class LogStrTransformer extends MethodParamTransformer {

    @Override
    public boolean supports(ParameterInfo parameterInfo) {

        return String.class.equals(parameterInfo.getParameterType())
            && "logStr".equals(parameterInfo.getParameterRelativeName());
    }

    @Override
    public List<Object> transform(ParameterInfo parameterInfo) {
        return List.of(LOG_STR);
    }
}
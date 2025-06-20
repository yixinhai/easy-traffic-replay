package com.xh.easy.trafficreplay.service.core.allocator.parameter;

import com.xh.easy.trafficreplay.service.model.ParameterInfo;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

/**
 * logStr参数分配策略
 *
 * @author yixinhai
 */
public class LogStrTransformer extends ParamTransformer {

    private static final LogStrTransformer INSTANCE = new LogStrTransformer();

    private LogStrTransformer() {
    }

    public static LogStrTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean supports(ParameterInfo parameterInfo) {

        return String.class.equals(parameterInfo.getParameterType())
            && "logStr".equals(parameterInfo.getParameterRelativeName());
    }

    @Override
    public Object transform(ParameterInfo parameterInfo) {
        return LOG_STR;
    }
}
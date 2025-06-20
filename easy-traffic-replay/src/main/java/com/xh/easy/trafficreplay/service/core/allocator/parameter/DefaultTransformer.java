package com.xh.easy.trafficreplay.service.core.allocator.parameter;

import com.xh.easy.trafficreplay.service.model.ParameterInfo;

/**
 * 默认参数分配策略
 * 对于没有特殊处理要求的参数，返回null
 *
 * @author yixinhai
 */
public class DefaultTransformer extends ParamTransformer {

    private static final DefaultTransformer INSTANCE = new DefaultTransformer();

    private DefaultTransformer() {
    }

    public static DefaultTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean supports(ParameterInfo parameterInfo) {
        return true; // 支持所有参数，作为兜底策略
    }

    @Override
    public Object transform(ParameterInfo parameterInfo) {
        return null; // 默认返回null
    }

}
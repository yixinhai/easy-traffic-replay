package com.xh.easy.trafficreplay.service.core.allocator.parameter;

import com.xh.easy.trafficreplay.service.model.ParameterInfo;
import com.xh.easy.trafficreplay.service.util.PrimitiveUtil;

/**
 * 基本类型参数分配策略
 *
 * @author yixinhai
 */
public class PrimitiveTransformer extends ParamTransformer {

    private static final PrimitiveTransformer INSTANCE = new PrimitiveTransformer();

    private PrimitiveTransformer() {
    }

    public static PrimitiveTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean supports(ParameterInfo parameterInfo) {
        return parameterInfo.isPrimitive();
    }

    @Override
    public Object transform(ParameterInfo parameterInfo) {
        return PrimitiveUtil.getDefaultValue(parameterInfo.getParameterType());
    }
}
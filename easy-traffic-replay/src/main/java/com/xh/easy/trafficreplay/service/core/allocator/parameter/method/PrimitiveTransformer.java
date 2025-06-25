package com.xh.easy.trafficreplay.service.core.allocator.parameter.method;

import com.xh.easy.trafficreplay.service.model.ParameterInfo;
import com.xh.easy.trafficreplay.service.util.PrimitiveUtil;

import java.util.Collections;
import java.util.List;

/**
 * 基本类型参数分配策略
 *
 * @author yixinhai
 */
public class PrimitiveTransformer extends MethodParamTransformer {

    @Override
    public boolean supports(ParameterInfo parameterInfo) {
        return parameterInfo.isPrimitive();
    }

    @Override
    public List<Object> transform(ParameterInfo parameterInfo) {
        return Collections.singletonList(PrimitiveUtil.getDefaultValue(parameterInfo.getParameterType()));
    }
}
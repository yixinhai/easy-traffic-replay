package com.xh.easy.trafficreplay.service.core.allocator.parameter.method;

import com.xh.easy.trafficreplay.service.model.ParameterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yixinhai
 */
public class DefaultTransformer extends MethodParamTransformer {

    @Override
    List<Object> transform(ParameterInfo parameterInfo) throws Exception {
        return new ArrayList<>(0);
    }

    @Override
    boolean supports(ParameterInfo parameterInfo) {
        return true;
    }
}

package com.xh.easy.trafficreplay.service.core.allocator.parameter;

import com.xh.easy.trafficreplay.service.annotation.ParameterJSON;
import com.xh.easy.trafficreplay.service.model.ParameterInfo;
import com.xh.easy.trafficreplay.service.util.ClassWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

/**
 * JSON参数分配策略
 *
 * @author yixinhai
 */
@Slf4j
public class JsonTransformer extends ParamTransformer {

    private static final JsonTransformer INSTANCE = new JsonTransformer();

    private JsonTransformer() {
    }

    public static JsonTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean supports(ParameterInfo parameterInfo) {
        return ClassWrapper.getDeclaredAnnotation(parameterInfo.getMethod(), ParameterJSON.class) != null;
    }

    @Override
    public List<Object> transform(ParameterInfo parameterInfo) throws Exception {
        ParameterJSON parameterJSON =
            ClassWrapper.getDeclaredAnnotation(parameterInfo.getMethod(), ParameterJSON.class);
        String jsonValue = parameterJSON.jsonValue();

        // TODO: 实现JSON到参数的转换逻辑
        log.info("{} JSON参数分配策略暂未实现，jsonValue: {}", LOG_STR, jsonValue);
        throw new UnsupportedOperationException("JSON参数分配策略暂未实现");
    }
}
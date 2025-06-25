package com.xh.easy.trafficreplay.service.core.allocator.parameter.json;

import com.xh.easy.trafficreplay.service.annotation.ParameterJSON;
import com.xh.easy.trafficreplay.service.core.handler.MethodHandler;
import com.xh.easy.trafficreplay.service.model.ParameterInfo;
import com.xh.easy.trafficreplay.service.util.ClassWrapper;

import java.util.List;

/**
 * json注解参数转换
 *
 * @author yixinhai
 */
public class AnnotationJsonAdapter extends JsonTransformer implements JsonParamTransformer {

    @Override
    public List<Object> doTransform(ParameterInfo parameterInfo) throws Exception {
        return transform(parameterInfo.getMethod(), getJsonValue(parameterInfo.getMethodHandler()));
    }

    @Override
    public boolean supports(MethodHandler methodHandler) {
        ParameterJSON parameterJSON = methodHandler.getAnnotation(ParameterJSON.class);

        if (parameterJSON == null) {
            return false;
        }

        return parameterJSON.jsonValue() != null && parameterJSON.jsonValue().length() > 0;
    }


    private String getJsonValue(MethodHandler methodHandler) {
        return methodHandler.getAnnotation(ParameterJSON.class).jsonValue();
    }
}

package com.xh.easy.trafficreplay.service.core.allocator.parameter.json;

import com.xh.easy.trafficreplay.service.core.allocator.parameter.ParamTransformer;
import com.xh.easy.trafficreplay.service.core.handler.MethodHandler;

/**
 * json参数转换器
 *
 * @author yixinhai
 */
public interface JsonParamTransformer extends ParamTransformer {

    /**
     * 是否支持转换该参数
     *
     * @param methodHandler 方法信息
     * @return 是否支持 true:支持,false:不支持
     */
    boolean supports(MethodHandler methodHandler);
}

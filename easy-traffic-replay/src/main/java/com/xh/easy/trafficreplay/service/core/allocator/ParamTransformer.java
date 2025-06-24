package com.xh.easy.trafficreplay.service.core.allocator;

import com.xh.easy.trafficreplay.service.model.ParameterInfo;

import java.util.List;

/**
 * 转换器
 *
 * @author yixinhai
 */
public interface ParamTransformer {

    /**
     * 转换信息
     *
     * @param parameterInfo 参数信息
     * @return 转换结果
     * @throws Exception 异常
     */
    List<Object> doTransform(ParameterInfo parameterInfo) throws Exception;
}

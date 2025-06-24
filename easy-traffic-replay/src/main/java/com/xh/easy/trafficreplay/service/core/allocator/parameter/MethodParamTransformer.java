package com.xh.easy.trafficreplay.service.core.allocator.parameter;

import com.xh.easy.trafficreplay.service.core.allocator.ParamTransformer;
import com.xh.easy.trafficreplay.service.model.ParameterInfo;

import java.util.List;

/**
 * 参数分配策略接口
 *
 * @author yixinhai
 */
public abstract class MethodParamTransformer implements ParamTransformer {

    private MethodParamTransformer next;

    protected void setNext(MethodParamTransformer next) {
        this.next = next;
    }

    /**
     * 参数转换
     *
     * @param parameterInfo  方法信息
     * @return 转换后的参数
     * @throws Exception
     *     转换异常
     */
    @Override
    public List<Object> doTransform(ParameterInfo parameterInfo) throws Exception {
        return this.supports(parameterInfo) ? this.transform(parameterInfo) : next.doTransform(parameterInfo);
    }

    /**
     * 参数转换
     *
     * @param parameterInfo  方法信息
     * @return 转换后的参数
     * @throws Exception
     *     转换异常
     */
    abstract List<Object> transform(ParameterInfo parameterInfo) throws Exception;

    /**
     * 判断是否支持处理该参数
     *
     * @param parameterInfo  方法信息
     * @return 是否支持
     */
    abstract boolean supports(ParameterInfo parameterInfo);
}
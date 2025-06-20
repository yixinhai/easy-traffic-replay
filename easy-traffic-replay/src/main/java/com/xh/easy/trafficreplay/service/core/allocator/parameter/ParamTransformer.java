package com.xh.easy.trafficreplay.service.core.allocator.parameter;

import com.xh.easy.trafficreplay.service.model.ParameterInfo;

/**
 * 参数分配策略接口
 *
 * @author yixinhai
 */
public abstract class ParamTransformer {

    protected ParamTransformer next;

    protected void setNext(ParamTransformer next) {
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
    protected Object doTransform(ParameterInfo parameterInfo) throws Exception {
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
    abstract Object transform(ParameterInfo parameterInfo) throws Exception;

    /**
     * 判断是否支持处理该参数
     *
     * @param parameterInfo  方法信息
     * @return 是否支持
     */
    abstract boolean supports(ParameterInfo parameterInfo);
}
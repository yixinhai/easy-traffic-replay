package com.xh.easy.trafficreplay.service.core.allocator.parameter;

import com.xh.easy.trafficreplay.service.annotation.ParameterJSON;
import com.xh.easy.trafficreplay.service.core.allocator.Allocator;
import com.xh.easy.trafficreplay.service.core.allocator.parameter.json.JsonParamTransformer;
import com.xh.easy.trafficreplay.service.core.allocator.parameter.method.MethodParamTransformer;
import com.xh.easy.trafficreplay.service.core.allocator.parameter.method.TransformerBuilder;
import com.xh.easy.trafficreplay.service.core.handler.MethodInfo;
import com.xh.easy.trafficreplay.service.model.ParameterInfo;
import com.xh.easy.trafficreplay.service.util.ClassWrapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

/**
 * 参数分配器
 *
 * @author yixinhai
 */
@Slf4j
public class ParameterAllocator extends Allocator {

    private final MethodParamTransformer transformer;
    private final JsonParamTransformer jsonTransformer;
    private boolean hasAllocated = false;
    private Object[] args;

    public ParameterAllocator(MethodInfo methodInfo) {
        super(methodInfo);
        this.transformer = initializeTransformers();
        this.jsonTransformer = initializeJsonTransformers();
    }

    public static ParameterAllocator of(MethodInfo methodInfo) {
        return new ParameterAllocator(methodInfo);
    }

    /**
     * 初始化责任链列表
     */
    private MethodParamTransformer initializeTransformers() {
        return TransformerBuilder.buildMethodParamTransformer();
    }

    /**
     * 初始化json参数转换器
     */
    private JsonParamTransformer initializeJsonTransformers() {
        return TransformerBuilder.buildJsonParamTransformer();
    }

    /**
     * 分配参数
     */
    @Override
    public void allocate() {
        Parameter[] parameters = methodInfo.getMethod().getParameters();
        if (parameters == null || parameters.length == 0) {
            this.allocateComplete();
            return;
        }

        if (jsonTransformer.supports(methodInfo)) {
            jsonAllocate();
        } else {
            methodAllocate(parameters);
        }
    }

    /**
     * json参数分配
     */
    private void jsonAllocate() {
        args = doAllocate(jsonTransformer, null).toArray();
        allocateComplete();
    }

    /**
     * 方法参数分配
     */
    private void methodAllocate(Parameter[] parameters) {

        this.args = new Object[parameters.length];

        for (int i = 0; i < args.length; i++) {
            // 分配参数值
            for (Object o : doAllocate(transformer, parameters[i])) {
                args[i] = o;
                this.allocateComplete();
            }
        }

        if (!this.hasAllocated) {
            this.allocateComplete();
        }
    }

    private List<Object> doAllocate(ParamTransformer transformer, Parameter parameter) {
        try {
            return transformer.doTransform(new ParameterInfo(methodInfo, parameter));
        } catch (Exception e) {
            log.warn("{} Failed to allocate parameter for method: {}", LOG_STR, methodInfo, e);
            return new ArrayList<>(0);
        }
    }

    private void allocateComplete() {
        this.hasAllocated = true;
        this.accept();
    }

    @Override
    public Object invoke() throws Exception {

        Object target = methodInfo.getTarget();
        Method method = methodInfo.getMethod();

        return method.invoke(target, args);
    }
}

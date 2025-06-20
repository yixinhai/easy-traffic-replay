package com.xh.easy.trafficreplay.service.core.allocator.parameter;

import com.xh.easy.trafficreplay.service.core.allocator.Allocator;
import com.xh.easy.trafficreplay.service.core.handler.MethodInfo;
import com.xh.easy.trafficreplay.service.model.ParameterInfo;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;
import java.util.List;

/**
 * 参数分配器
 *
 * @author yixinhai
 */
@Slf4j
public class ParameterAllocator extends Allocator {

    private final ParamTransformer transformer;
    private boolean hasAllocated = false;
    private Object[] args;

    public ParameterAllocator(MethodInfo methodInfo) {
        super(methodInfo);
        this.transformer = initializeTransformers();
    }

    public static ParameterAllocator of(MethodInfo methodInfo) {
        return new ParameterAllocator(methodInfo);
    }

    /**
     * 初始化责任链列表
     */
    private ParamTransformer initializeTransformers() {

        ParamTransformer jsonTransformer = JsonTransformer.getInstance();
        ParamTransformer logStrTransformer = LogStrTransformer.getInstance();
        ParamTransformer primitiveTransformer = PrimitiveTransformer.getInstance();
        ParamTransformer annotatedObjectTransformer = AnnotatedObjectTransformer.getInstance();

        // 按优先级排序
        jsonTransformer.setNext(logStrTransformer);
        logStrTransformer.setNext(primitiveTransformer);
        primitiveTransformer.setNext(annotatedObjectTransformer);

        return jsonTransformer;
    }

    /**
     * 分配参数
     */
    @Override
    public void allocate() throws Exception {
        Parameter[] parameters = methodInfo.getMethod().getParameters();
        if (parameters == null || parameters.length == 0) {
            this.allocateComplete();
            return;
        }

        this.args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            // 分配参数值
            Iterator<Object> argumentIterator = transformer.doTransform(new ParameterInfo(methodInfo, parameter))
                .iterator();
            while (argumentIterator.hasNext()) {
                args[i] = argumentIterator.next();
                this.allocateComplete();
            }
        }

        if (!this.hasAllocated) {
            this.allocateComplete();
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

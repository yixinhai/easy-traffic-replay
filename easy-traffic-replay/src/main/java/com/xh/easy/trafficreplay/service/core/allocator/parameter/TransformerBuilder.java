package com.xh.easy.trafficreplay.service.core.allocator.parameter;

/**
 * 转换器责任链构建者
 *
 * @author yixinhai
 */
public class TransformerBuilder {

    private static final MethodParamTransformer logStrTransformer = new LogStrTransformer();
    private static final MethodParamTransformer primitiveTransformer = new PrimitiveTransformer();
    private static final MethodParamTransformer objectTransformer = new ObjectTransformer();
    private static final MethodParamTransformer defaultTransformer = new DefaultTransformer();


    public static MethodParamTransformer build() {
        logStrTransformer.setNext(primitiveTransformer);
        primitiveTransformer.setNext(objectTransformer);
        objectTransformer.setNext(defaultTransformer);

        return logStrTransformer;
    }
}

package com.xh.easy.trafficreplay.service.core.executor;

import com.xh.easy.trafficreplay.service.core.handler.MethodHandler;

/**
 * @author yixinhai
 */
public abstract class Visitor {

    public abstract void visit(MethodHandler methodInfo);
}

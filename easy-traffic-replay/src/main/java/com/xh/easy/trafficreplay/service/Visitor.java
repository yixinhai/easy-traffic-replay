package com.xh.easy.trafficreplay.service;

import com.xh.easy.trafficreplay.service.method.MethodInfo;

/**
 * @author yixinhai
 */
public abstract class Visitor {

    public abstract void visit(MethodInfo methodInfo);
}

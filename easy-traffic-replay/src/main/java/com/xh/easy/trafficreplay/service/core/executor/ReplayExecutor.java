package com.xh.easy.trafficreplay.service.core.executor;

import com.xh.easy.trafficreplay.service.core.allocator.Allocator;
import com.xh.easy.trafficreplay.service.core.handler.MethodHandler;
import com.xh.easy.trafficreplay.service.manager.MethodManager;
import com.xh.easy.trafficreplay.service.core.allocator.ParameterAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 回放执行器
 * 负责收集和执行配置文件中指定的方法
 */
@Slf4j
@Component
public class ReplayExecutor extends Visitor {

    private static final ReplayExecutor replayExecutor = new ReplayExecutor();

    @Autowired
    private MethodManager methodManager;

    public ReplayExecutor() {
    }

    public static ReplayExecutor getInstance() {
        return replayExecutor;
    }

    /**
     * 执行所有收集到的方法
     */
    public void executeAll() {
        methodManager.getMethodMap()
            .forEach((key, methodInfo) -> {
                try {
                    // 参数增强器
                    Allocator allocator = ParameterAllocator.of(methodInfo);

                    allocator.allocate();
                } catch (Exception e) {
                    log.error("Failed to allocate parameter for method: {}", methodInfo, e);
                }
            });
    }

    @Override
    public void visit(MethodHandler handler) {
        try {
            Object o = handler.invoke();
            log.info("Successfully executed method: {}, result: {}", handler, o);
        } catch (Exception e) {
            log.error("Failed to execute method: {}", handler, e);
            // TODO 告警
        }
    }
}
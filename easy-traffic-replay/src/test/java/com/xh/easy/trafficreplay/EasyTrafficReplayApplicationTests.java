package com.xh.easy.trafficreplay;

import com.xh.easy.trafficreplay.service.core.executor.ReplayExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EasyTrafficReplayApplicationTests {

    @Autowired
    private ReplayExecutor replayExecutor;

    @Test
    void contextLoads() {
        replayExecutor.executeAll();
    }

}

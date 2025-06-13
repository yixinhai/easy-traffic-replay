package com.xh.easy.trafficreplay.service.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yixinhai
 */
@Slf4j
public class TestPOJO {

    /**
     * com.xh.easy.trafficreplay.service.test.TestPOJO#test01()
     * com.xh.easy.trafficreplay.service.test.TestPOJO#test02(java.lang.String)
     * com.xh.easy.trafficreplay.service.test.TestPOJO#test03(java.lang.String, com.xh.easy.trafficreplay.service.test.Test)
     * com.xh.easy.trafficreplay.service.test.TestPOJO#test04(java.lang.Integer)
     */
    public void test01() {
        log.info("TestPOJO.test01");
    }

    public void test02(String logStr) {
        log.info("{} TestPOJO.test02", logStr);
    }

    public void test03(String logStr, Test test) {
        log.info("{} TestPOJO.test03 test={}", logStr, test);
    }

    public void test04(Integer num) {
        log.info("TestPOJO.test04 num={}", num);
    }
}

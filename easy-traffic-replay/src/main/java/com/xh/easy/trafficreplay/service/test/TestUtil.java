package com.xh.easy.trafficreplay.service.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yixinhai
 */
@Slf4j
public class TestUtil {

    /**
     * com.xh.easy.trafficreplay.service.test.TestUtil#test01()
     * com.xh.easy.trafficreplay.service.test.TestUtil#test02(java.lang.String)
     * com.xh.easy.trafficreplay.service.test.TestUtil#test03(java.lang.String, com.xh.easy.trafficreplay.service.test.Test)
     * com.xh.easy.trafficreplay.service.test.TestUtil#test04(java.lang.Integer)
     */
    public void test01() {
        log.info("TestUtil.test01");
    }

    public void test02(String logStr) {
        log.info("{} TestUtil.test02", logStr);
    }

    public void test03(String logStr, Test test) {
        log.info("{} TestUtil.test03 test={}", logStr, test);
    }

    public void test04(Integer num) {
        log.info("TestUtil.test04 num={}", num);
    }
}

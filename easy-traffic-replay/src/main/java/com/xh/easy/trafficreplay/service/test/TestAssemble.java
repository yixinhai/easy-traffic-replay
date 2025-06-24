package com.xh.easy.trafficreplay.service.test;

import com.xh.easy.trafficreplay.service.annotation.ParameterJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yixinhai
 */
@Slf4j
@Component
public class TestAssemble {

    /**
     * com.xh.easy.trafficreplay.service.test.TestAssemble#test01()
     * com.xh.easy.trafficreplay.service.test.TestAssemble#test03(java.lang.String, com.xh.easy.trafficreplay.service.test.Test)
     * com.xh.easy.trafficreplay.service.test.TestAssemble#test05(java.lang.String, java.util.List)
     * com.xh.easy.trafficreplay.service.test.TestAssemble#test06(java.lang.String, java.util.List, com.xh.easy.trafficreplay.service.test.Test)
     */
    public void test01() {
        log.info("test01");
    }

    public void test02(String logStr) {
        log.info("{} test02", logStr);
    }

    public void test03(String logStr, Test test) {
        log.info("{} test03 test={}", logStr, test);
    }

    public void test04(Integer num) {
        log.info("test04 num={}", num);
    }

    public void test05(String logStr, List<Long> ids) {
        log.info("{} test05 ids={}", logStr, ids);
    }

    @ParameterJSON(jsonValue = "{\"logStr\":\"logStr\",\"test\":{\"username\":\"yixinhai\",\"gender\":1}}")
    public void test06(String logStr, List<Long> ids, Test test) {
        log.info("{} test06 ids={} test={}", logStr, ids, test);
    }
}

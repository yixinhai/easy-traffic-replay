package com.xh.easy.trafficreplay.service.test;

import com.xh.easy.trafficreplay.service.annotation.ParameterAllocation;
import com.xh.easy.trafficreplay.service.annotation.ParameterValue;
import lombok.Data;

/**
 * @author yixinhai
 */
@Data
@ParameterAllocation
public class Test {

    @ParameterValue(value = "yixinhai")
    private String username;
    private int age;
}

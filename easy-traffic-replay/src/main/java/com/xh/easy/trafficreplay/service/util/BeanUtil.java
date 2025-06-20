package com.xh.easy.trafficreplay.service.util;

import com.alibaba.fastjson.JSON;

/**
 * @author yixinhai
 */
public class BeanUtil {

    public static <T> T deepCopy(Object source, Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(source), clazz);
    }
}

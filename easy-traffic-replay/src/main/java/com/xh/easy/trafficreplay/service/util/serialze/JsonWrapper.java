package com.xh.easy.trafficreplay.service.util.serialze;

import com.alibaba.fastjson.JSONObject;

/**
 * json包装类
 *
 * @author yixinhai
 */
public class JsonWrapper {

    private final String json;
    private final JSONObject jsonObject;

    private JsonWrapper(String json) {
        this.json = json;
        this.jsonObject = JsonUtil.parseObject(json);
    }

    /**
     * 创建JsonWrapper
     *
     * @param json json字符串
     * @return JsonWrapper
     */
    public static JsonWrapper of(String json) {

        assert json != null && !json.isEmpty();

        return new JsonWrapper(json);
    }

    /**
     * 获取json中属性值
     *
     * @param key 属性名称
     * @return 属性值
     */
    public Object get(String key) {
        return jsonObject.get(key);
    }

    /**
     * 判断json中是否包含指定属性
     *
     * @param key 待判断的属性名称
     * @return true:包含;false:不包含
     */
    public boolean contains(String key) {
        return jsonObject.containsKey(key);
    }
}

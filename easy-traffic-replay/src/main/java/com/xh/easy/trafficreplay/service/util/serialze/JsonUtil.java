package com.xh.easy.trafficreplay.service.util.serialze;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * json序列化工具类
 *
 * @author yixinhai
 */
public class JsonUtil {

    private static final SerializerFeature[] features = new SerializerFeature[]{
        SerializerFeature.IgnoreErrorGetter,
        SerializerFeature.IgnoreNonFieldGetter,
        SerializerFeature.WriteMapNullValue,
        SerializerFeature.SkipTransientField,
    };

    public static <T> T parseObject(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

    public static byte[] toJSONBytes(Object object) {
        return JSON.toJSONBytes(object, features);
    }

    public static Map<String, String> parseMap(String data) {

        Map<String, String> map = new HashMap<>();

        JSONObject jsonObject = JSON.parseObject(data);
        jsonObject.forEach((k, v) -> {
            map.put(k, v.toString());
        });

        return map;
    }

    public static boolean contains(String data, String key) {
        return JSON.parseObject(data).containsKey(key);
    }

    public static Object get(String data, String key) {
        return JSON.parseObject(data).get(key);
    }

}

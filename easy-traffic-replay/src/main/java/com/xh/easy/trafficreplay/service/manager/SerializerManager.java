package com.xh.easy.trafficreplay.service.manager;

import com.xh.easy.trafficreplay.service.util.serialze.JsonSerializer;
import com.xh.easy.trafficreplay.service.util.serialze.Serializer;
import com.xh.easy.trafficreplay.service.util.serialze.Serializer.SerializeType;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器管理器
 *
 * @author yixinhai
 */
public class SerializerManager {

    private static final Map<SerializeType, Serializer> serializers = new HashMap<>();

    static {
        serializers.put(SerializeType.JSON, new JsonSerializer());
    }


    /**
     * 获取JSON序列化器
     *
     * @return json序列化器
     */
    public static Serializer jsonSerializer() {
        return serializers.get(SerializeType.JSON);
    }
}

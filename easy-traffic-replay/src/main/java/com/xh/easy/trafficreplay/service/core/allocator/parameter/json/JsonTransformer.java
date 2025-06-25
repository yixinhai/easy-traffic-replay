package com.xh.easy.trafficreplay.service.core.allocator.parameter.json;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xh.easy.trafficreplay.service.manager.SerializerManager;
import com.xh.easy.trafficreplay.service.util.serialze.JsonUtil;
import com.xh.easy.trafficreplay.service.util.serialze.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;

import static com.xh.easy.trafficreplay.service.constant.LogStrConstant.LOG_STR;

/**
 * json参数转换器
 *
 * @author yixinhai
 */
@Slf4j
public class JsonTransformer {

    private final Serializer serializer;
    private final ParameterNameDiscoverer discoverer;

    public JsonTransformer() {
        this.serializer = SerializerManager.jsonSerializer();
        this.discoverer = new StandardReflectionParameterNameDiscoverer();
    }

    /**
     * 转换参数
     *
     * @param method 目标方法
     * @param data json格式参数内容
     * @return 参数实例列表
     */
    public List<Object> transform(Method method, String data) {
        Type[] parameterTypes = method.getGenericParameterTypes();
        String[] parameterNames = discoverer.getParameterNames(method);
        assert parameterNames != null;


        List<Object> result = new ArrayList<>();

        for (int i = 0; i < parameterTypes.length; i++) {
            try {

                // json中不存在该参数赋值null
                if (!JsonUtil.contains(data, parameterNames[i])) {
                    result.add(null);
                    continue;
                }

                Object arg = null;
                Object jsonValue = JsonUtil.get(data, parameterNames[i]);

                if (jsonValue instanceof JSONObject) {
                    arg = serializer.deserialize(jsonValue.toString(), parameterTypes[i]);
                } else {
                    arg = jsonValue;
                }

                result.add(arg);
            } catch (Exception e) {
                log.warn("{} Failed to transform json to parameter", LOG_STR, e);
                return null;
            }
        }

        return result;
    }


}

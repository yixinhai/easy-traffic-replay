package com.xh.easy.trafficreplay.service.core.allocator.parameter.json;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xh.easy.trafficreplay.service.manager.SerializerManager;
import com.xh.easy.trafficreplay.service.util.PrimitiveUtil;
import com.xh.easy.trafficreplay.service.util.serialze.JsonWrapper;
import com.xh.easy.trafficreplay.service.util.serialze.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

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
        this.discoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    /**
     * 转换参数
     *
     * @param method 目标方法
     * @param data json格式参数内容
     * @return 参数实例列表
     */
    public List<Object> transform(Method method, String data) {
        return transform(method.getGenericParameterTypes(), discoverer.getParameterNames(method), JsonWrapper.of(data));
    }

    /**
     * 获取参数列表
     *
     * @param parameterTypes 参数类型列表
     * @param parameterNames 参数名称列表
     * @param json json包装类
     * @return json转换后的参数列表
     */
    private List<Object> transform(Type[] parameterTypes, String[] parameterNames, JsonWrapper json) {
        List<Object> result = new ArrayList<>();

        for (int i = 0; i < parameterTypes.length; i++) {
            try {
                result.add(transformParam(parameterTypes[i], parameterNames[i], json));
            } catch (Exception e) {
                log.warn("{} Failed to transform json to parameter", LOG_STR, e);
                return new ArrayList<>(0);
            }
        }

        return result;
    }

    /**
     * 获取参数列表
     *
     * @param paramType 参数类型列表
     * @param paramName 参数名称列表
     * @param json json包装类
     * @return json转换后的参数
     */
    private Object transformParam(Type paramType, String paramName, JsonWrapper json) {
        return json.contains(paramName) ? transformExistParam(paramType, json.get(paramName))
            : transformNotExistParam(paramType);
    }

    /**
     * 获取未在json出现的参数默认值
     * 【规则】基本类型：false 或 0；引用类型：null
     *
     * @param paramType 参数类型
     * @return 参数默认值
     */
    private Object transformNotExistParam(Type paramType) {
        return PrimitiveUtil.getDefaultValue(paramType.getTypeName());
    }

    /**
     * 获取json中存在的参数值，反序列化为目标类型对象
     *
     * @param paramType 参数类型
     * @param jsonValue json参数值
     * @return 反序列化后的对象
     */
    private Object transformExistParam(Type paramType, Object jsonValue) {

        if (jsonValue instanceof JSONObject) {
            return serializer.deserialize(((JSONObject)jsonValue).toJSONString(), paramType);
        }

        return jsonValue;
    }
}

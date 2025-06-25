package com.xh.easy.trafficreplay.plugin.idea.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 方法数据服务
 * 用于管理选择的方法和对应的JSON数据
 * 
 * @author yixinhai
 */
public class MethodDataService {

    private static final MethodDataService INSTANCE = new MethodDataService();

    // 存储方法签名和对应的JSON数据
    private final Map<String, String> methodJsonMap = new ConcurrentHashMap<>();

    // 存储最近选择的方法
    private String lastSelectedMethod;

    private MethodDataService() {
    }

    public static MethodDataService getInstance() {
        return INSTANCE;
    }

    /**
     * 保存方法和JSON数据
     * 
     * @param methodSignature
     *     方法签名
     * @param jsonContent
     *     JSON内容
     */
    public void saveMethodData(String methodSignature, String jsonContent) {
        methodJsonMap.put(methodSignature, jsonContent);
        lastSelectedMethod = methodSignature;
        System.out.println("保存方法数据 - 方法: " + methodSignature + ", JSON长度: " + jsonContent.length());
    }

    /**
     * 获取指定方法的JSON数据
     * 
     * @param methodSignature
     *     方法签名
     * @return JSON内容，如果不存在则返回null
     */
    public String getJsonContent(String methodSignature) {
        return methodJsonMap.get(methodSignature);
    }

    /**
     * 获取最近选择的方法
     * 
     * @return 最近选择的方法签名
     */
    public String getLastSelectedMethod() {
        return lastSelectedMethod;
    }

    /**
     * 获取最近选择方法的JSON内容
     * 
     * @return JSON内容，如果不存在则返回null
     */
    public String getLastSelectedJson() {
        if (lastSelectedMethod != null) {
            return methodJsonMap.get(lastSelectedMethod);
        }
        return null;
    }

    /**
     * 获取所有保存的方法签名
     * 
     * @return 所有方法签名的集合
     */
    public Set<String> getAllMethods() {
        return methodJsonMap.keySet();
    }

    /**
     * 获取所有保存的数据
     * 
     * @return 方法签名和JSON内容的映射
     */
    public Map<String, String> getAllData() {
        return new ConcurrentHashMap<>(methodJsonMap);
    }

    /**
     * 删除指定方法的数据
     * 
     * @param methodSignature
     *     方法签名
     * @return 如果删除成功返回true，否则返回false
     */
    public boolean removeMethodData(String methodSignature) {
        String removed = methodJsonMap.remove(methodSignature);
        if (methodSignature.equals(lastSelectedMethod)) {
            lastSelectedMethod = null;
        }
        return removed != null;
    }

    /**
     * 清空所有数据
     */
    public void clearAll() {
        methodJsonMap.clear();
        lastSelectedMethod = null;
    }

    /**
     * 检查是否存在指定方法的数据
     * 
     * @param methodSignature
     *     方法签名
     * @return 如果存在返回true，否则返回false
     */
    public boolean hasMethodData(String methodSignature) {
        return methodJsonMap.containsKey(methodSignature);
    }

    /**
     * 获取保存的方法数量
     * 
     * @return 方法数量
     */
    public int getMethodCount() {
        return methodJsonMap.size();
    }
}
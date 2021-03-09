package com.tester.testercommon.util.cache;

import java.util.Map;
import java.util.Set;

/**
 * 缓存操作接口.
 * @Date 16:16 2021/3/9
 * @Author 温昌营
 **/
public interface ICacheManager {

    /**
     * 存入缓存
     */
    void putCache(String key, EntityCache cache);

    /**
     * 设置缓存
     */
    void putCache(String key, Object datas);


    /**
     * 获取对应缓存
     */
    EntityCache getCacheByKey(String key);

    /**
     * 获取对应缓存
     */
    Object getCacheDataByKey(String key);

    /**
     * 获取所有缓存
     */
    Map<String, EntityCache> getCacheAll();

    /**
     * 判断是否在缓存中
     */
    boolean isContains(String key);

    /**
     * 清除所有缓存
     */
    void clearAll();

    /**
     * 清除对应缓存
     */
    void clearByKey(String key);

    /**
     * 缓存是否超时失效
     */
    boolean isTimeOut(String key);

    /**
     * 获取所有key
     */
    Set<String> getAllKeys();

}

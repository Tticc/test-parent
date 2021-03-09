package com.tester.testercommon.util.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap实现自定义缓存.
 * @Date 16:18 2021/3/9
 * @Author 温昌营
 **/
@Slf4j
//@Service("cacheManagerImpl")
public class CacheManagerImpl implements ICacheManager {

    public static ICacheManager getInstance(){
        return SingletonCreator.INSTANCE;
    }
    /**
     * 缓存池大小,默认200个
     */
    @Value("${local.cache.pool.size:200}")
    private Integer cachePoolSize;

    /**
     * 缓存有效时间，默认有效时间为1天
     */
    @Value("${local.cache.expiration.time:86400}")
    private Long cacheExpirationTime;

    /**
     * 缓冲池实现
     */
    private static Map<String, EntityCache> caches = new ConcurrentHashMap<String, EntityCache>();

    @Override
    public void putCache(String key, EntityCache cache) {
        int cacheSize = caches.size();
        if (cacheSize < cachePoolSize) {
            caches.put(key, cache);
        } else {
            log.info("缓存池已经达到阈值，不会再新增缓存" + key);
        }
    }

    @Override
    public void putCache(String key, Object datas) {
        int cacheSize = caches.size();
        if (cacheSize < cachePoolSize) {
            putCache(key, new EntityCache(datas));
        } else {
            log.info("缓存池已经达到阈值，不会再新增缓存" + key);
        }
    }

    @Override
    public EntityCache getCacheByKey(String key) {
        if (this.isContains(key)) {
            EntityCache entityCache = caches.get(key);
            //新增被动清楚缓存机制，获取缓存的时候做判断
            long nowTime = System.currentTimeMillis() / 1000;
            if ((nowTime - entityCache.getCacheTime()) > cacheExpirationTime) {
                log.info("缓存超出有效期，数据不从缓存中取" + key);
                caches.remove(key);
                return null;
            }
            return caches.get(key);
        }
        return null;
    }

    @Override
    public Object getCacheDataByKey(String key) {
        if (this.isContains(key)) {

            EntityCache entityCache = caches.get(key);
            //新增被动清楚缓存机制，获取缓存的时候做判断
            long nowTime = System.currentTimeMillis() / 1000;
            if ((nowTime - entityCache.getCacheTime()) > cacheExpirationTime) {
                log.info("缓存超出有效期，数据不从缓存中取" + key);
                caches.remove(key);
                return null;
            }
            return entityCache.getDatas();
        }
        return null;
    }

    @Override
    public Map<String, EntityCache> getCacheAll() {
        return caches;
    }

    @Override
    public boolean isContains(String key) {
        return caches.containsKey(key);
    }

    @Override
    public void clearAll() {
        caches.clear();
    }

    @Override
    public void clearByKey(String key) {
        if (this.isContains(key)) {
            caches.remove(key);
        }
    }

    @Override
    public boolean isTimeOut(String key) {
        return false;
    }

    @Override
    public Set<String> getAllKeys() {
        return caches.keySet();
    }

    private static class SingletonCreator{
        private static final ICacheManager INSTANCE = new CacheManagerImpl();
    }
}

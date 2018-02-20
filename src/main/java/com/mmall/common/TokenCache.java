package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Eliza Liu on 2018/2/21
 * 此类用于本地缓存
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger((TokenCache.class));

    //设置缓存的初始化容量,当缓存内容超过了这个最大容量，guava会启用LRU算法，来移除缓存项，有效期是12h
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(
            12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
        @Override
        public String load(String s) throws Exception {
            return null;
        }
    });

    public static void setKey(String key, String value){
        localCache.put(key,value);

    }
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value))
                return null;
            return value;
        }catch (Exception e){
            logger.error("localCache get error", e);
        }
        return null;
    }
}

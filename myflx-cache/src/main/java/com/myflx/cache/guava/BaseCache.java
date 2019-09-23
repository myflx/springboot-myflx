package com.myflx.cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import java.util.concurrent.TimeUnit;

public abstract class BaseCache<K, V> {

    private LoadingCache<K, V> cache;

    public BaseCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .build(new CacheLoader<K, V>() {
                    public V load(K k) throws Exception {
                        return loadData(k);
                    }
                });
    }

    /**
     * 超时缓存：数据写入缓存超过一定时间自动刷新
     *
     * @param duration 时长
     * @param timeUtil 时间单位
     */
    public BaseCache(long duration, TimeUnit timeUtil, Long maximumSize) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(duration, timeUtil)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) throws Exception {
                        return loadData(k);
                    }
                });
    }

    /**
     * 限容缓存：缓存数据个数不能超过maxSize
     *
     * @param maxSize 最大数量
     */
    public BaseCache(long maxSize) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) throws Exception {
                        return loadData(k);
                    }
                });
    }

    /**
     * 权重缓存：缓存数据权重和不能超过maxWeight
     *
     * @param maxWeight 最大权重
     * @param weigher   权重函数类，需要实现计算元素权重的函数
     */
    public BaseCache(long maxWeight, Weigher<K, V> weigher) {
        cache = CacheBuilder.newBuilder()
                .maximumWeight(maxWeight)
                .weigher(weigher)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) throws Exception {
                        return loadData(k);
                    }
                });
    }

    /**
     * 缓存数据加载方法
     *
     * @param k 加载key
     * @return 返回数据
     */
    protected abstract V loadData(K k);

    /**
     * 从缓存获取数据
     *
     * @param param 获取的参数
     * @return 返回的value
     */
    public V getCache(K param) {
        return cache.getUnchecked(param);
    }

    /**
     * 清除缓存数据，缓存清除后，数据会重新调用load方法获取
     *
     * @param k 刷新的ekey
     */
    public void refresh(K k) {
        cache.refresh(k);
    }

    /**
     * 主动设置缓存数据
     *
     * @param k key
     * @param v value
     */
    public void put(K k, V v) {
        cache.put(k, v);
    }
}

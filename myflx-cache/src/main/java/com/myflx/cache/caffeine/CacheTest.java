package com.myflx.cache.caffeine;


import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheTest {

    /**
     * 手动加载
     */
    @Test
    public void testManual() {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();
        final String s = cache.get("key", k -> createExpensiveGraph("key"));
        System.out.println(s);
    }
    /**
     * 同步加载
     */
    @Test
    public void testLoading() {
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(this::createExpensiveGraph);
        System.out.println(cache.get("key"));
    }
    /**
     * 异步加载
     */
    @Test
    public void testAsyn() throws ExecutionException, InterruptedException {
        AsyncCache<String,String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .buildAsync();
        CompletableFuture<String> graph = cache.get("test", k -> createExpensiveGraph("test"));
        System.out.println(graph.get());
    }

    private String createExpensiveGraph(String test) {
        return null;
    }
}

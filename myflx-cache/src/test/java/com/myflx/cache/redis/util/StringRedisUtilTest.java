package com.myflx.cache.redis.util;

import com.myflx.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StringRedisUtilTest extends BaseTest {
    @Resource
    private StringRedisUtil redisUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;


    @Test
    public void testSetGet(){
        redisUtil.set("name","abc");
        Object name = redisUtil.get("name");
        Assert.assertNotNull(name);
        redisUtil.del("name");
        List<RedisClientInfo> clientList = stringRedisTemplate.getClientList();
        System.out.println(clientList);
    }
}

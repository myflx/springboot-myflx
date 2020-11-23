package com.myflx.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author LuoShangLin
 */
@EnableCaching
@EnableRedisRepositories
@SpringBootApplication
public class CacheBootstrap {

    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(CacheBootstrap.class, args);
        System.out.println(CacheBootstrap.class);
        System.out.println(run.getBean(CacheBootstrap.class));
    }
}

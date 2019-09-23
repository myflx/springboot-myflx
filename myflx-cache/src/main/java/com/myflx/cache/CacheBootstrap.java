package com.myflx.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author LuoShangLin
 */
@EnableCaching
@SpringBootApplication
public class CacheBootstrap {

    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(CacheBootstrap.class, args);
        System.out.println(run);
    }
}

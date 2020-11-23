package com.myflx.cache.redis.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
/*@ConditionalOnClass({RedisConnectionFactory.class})
@ConditionalOnBean({RedisConnectionFactory.class})
@AutoConfigureBefore({RedisAutoConfiguration.class})*/
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class RedisConnectionCheckerConfiguration {

    @Bean
    public RedisConnectionChecker redisConnectionChecker(@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new RedisConnectionChecker(redisConnectionFactory);
    }
}

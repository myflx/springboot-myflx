package com.myflx.cache.redis.config;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisConnectionChecker implements Runnable, InitializingBean, DisposableBean, ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RedisConnectionFactory redisConnectionFactory;
    private final ScheduledExecutorService scheduler;
    private AtomicInteger connectGeneration = new AtomicInteger(0);
    private AtomicInteger outConnectionTimes = new AtomicInteger(0);
    private ApplicationContext applicationContext;

    public RedisConnectionChecker(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.scheduler = Executors.newScheduledThreadPool(1, (new ThreadFactoryBuilder()).setNameFormat("RedisConnectionChecker-%d").setDaemon(true).build());
    }

    @Override
    public void run() {
        if (!redisConnected()) {
            outConnectionTimes.addAndGet(1);
            logger.error("can not get redis connection,trying to connect {} times, please take emergency measures!", outConnectionTimes.get());
        } else {
            connectGeneration.addAndGet(1);
        }
        if (connectGeneration.get() == 1) {
            logger.info("redis connected success");
        }
    }

    private Boolean redisConnected() {
        try {
            RedisConnection redisConnection = redisConnectionFactory.getConnection();
            boolean closed = redisConnection.isClosed();
            if (closed) {
                logger.info("{} Redis Connection is Closed : {}", new Date(), closed);
                applicationContext.publishEvent(new RedisConnectFailedEvent(this));
            }
            return !closed;
        } catch (Exception e) {
            logger.error("redis connect status exception:{}", e.getMessage());
        } finally {
            if (!scheduler.isShutdown()) {
                scheduler.schedule(this, 10, TimeUnit.SECONDS);
            }
        }
        return false;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        scheduler.schedule(this, 10, TimeUnit.SECONDS);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        if (!scheduler.isShutdown()) {
            logger.info("redis listen executor shutdown...");
            scheduler.shutdown();
        }
    }
}

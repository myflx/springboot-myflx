package com.myflx.cache.redis.config;

import org.springframework.context.ApplicationEvent;

public class RedisConnectFailedEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RedisConnectFailedEvent(Object source) {
        super(source);
    }
}

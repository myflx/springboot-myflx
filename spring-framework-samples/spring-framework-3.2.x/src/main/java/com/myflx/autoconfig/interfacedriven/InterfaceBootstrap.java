package com.myflx.autoconfig.interfacedriven;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class InterfaceBootstrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(InterfaceBootstrap.class);
        applicationContext.refresh();
    }
}

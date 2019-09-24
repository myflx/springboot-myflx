package com.myflx.autoconfig.annotationconfig.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SayHelloConfiguration {
    @Bean
    public String sayHello(){
        return "hello";
    }
}

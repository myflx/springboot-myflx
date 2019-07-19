package com.myflx.server;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

//mvn spring-boot:run 引导启动
@SpringBootApplication
public class EmbeddedServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmbeddedServerApplication.class,args);
    }


    @Bean
    public RouterFunction<ServerResponse> hello(){
        return route(GET("/hello"),request -> ok().body(Mono.just("Hello world"),String.class));
    }

    @Bean
    public ApplicationRunner runner(WebServerApplicationContext webServerApplicationContext){
        return args -> {
            System.out.println("当前webServer实现类是："+webServerApplicationContext.getWebServer().getClass().getName());;
        };
    }

    @EventListener
    public void onWebReady(WebServerInitializedEvent event){
        System.out.println("当前webServer实现类是====="+event.getWebServer().getClass().getName());
    }
}

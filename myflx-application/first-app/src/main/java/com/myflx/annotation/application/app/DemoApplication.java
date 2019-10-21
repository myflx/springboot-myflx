package com.myflx.annotation.application.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
public class DemoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplicationConfiguration.class, args);
        DemoApplication bean = run.getBean(DemoApplication.class);
        System.out.println(Objects.nonNull(bean));
    }

    @RequestMapping("/")
    public String index() {
        return "Welcome , first app default port is 8080!";
    }
}

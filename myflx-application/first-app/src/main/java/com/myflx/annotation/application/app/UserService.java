package com.myflx.annotation.application.app;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@Service
public class UserService {
    public String index() {
        return "Welcome , first app default port is 8080!";
    }
}

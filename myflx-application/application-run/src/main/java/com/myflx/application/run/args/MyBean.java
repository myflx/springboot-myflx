package com.myflx.application.run.args;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author LuoShangLin
 * @date 2019/10/24 11:04
 **/
@Component
public class MyBean {
    public String getDebug() {
        return debug;
    }

    @Value("${info}")
    private String debug;

    @Autowired
    public MyBean(ApplicationArguments args) {
        boolean debug = args.containsOption("debug");
        List<String> files = args.getNonOptionArgs();
        System.out.printf("debug=%s,files=%s",debug,files);
    }
}
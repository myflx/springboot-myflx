package com.myflx.autoconfig.annotationconfig;

import com.myflx.autoconfig.annotationconfig.annotation.EnableSayHello;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableSayHello
public class AnnotationBootstrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationBootstrap.class);
        applicationContext.refresh();
        String sayHello = applicationContext.getBean("sayHello", String.class);
        System.out.println(sayHello);
        applicationContext.close();
    }
}

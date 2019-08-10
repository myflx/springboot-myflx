package com.myflx.bootstrap;

import com.myflx.repository.NameRepository;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DrivenAnnotationBootstrap {
    static {
        // 解决 Spring 2.5.x 不兼容 Java 8 的问题
        // 同时，请注意 Java Security 策略，必须具备 PropertyPermission
        System.setProperty("java.version","1.7.0");
    }
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:/META-INFO/spring/context.xml");
        context.refresh();
        NameRepository nameRepository = (NameRepository) context.getBean("nameRepository");
        System.out.printf("nameRepository.getAll() = %s\n",nameRepository.getAll());
    }
}

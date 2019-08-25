package com.myflx;


import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.serviceloader.AbstractServiceLoaderBasedFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.validation.ValidatorFactory;

/**
 * @author LuoShangLin
 */
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MyApplication.class, args);
        ValidatorFactory bean = run.getBean(ValidatorFactory.class);
        System.out.println(bean);///org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
    }
}

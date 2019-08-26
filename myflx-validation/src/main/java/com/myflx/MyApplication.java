package com.myflx;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import javax.validation.Validator;
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
        System.out.println(run.getBean(Validator.class) instanceof OptionalValidatorFactoryBean);
        System.out.println(run.getBean(Validator.class));
    }
}

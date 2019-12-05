package com.myflx;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Collection;
import java.util.List;

/**
 * @author LuoShangLin
 */
@SpringBootApplication
public class MyApplication {

    @Autowired
    private Collection<ValidatorFactory> validatorFactories;
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    public Collection<ValidatorFactory> getValidatorFactories() {
        return validatorFactories;
    }

    public LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
        return localValidatorFactoryBean;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MyApplication.class, args);
        final MyApplication bean1 = run.getBean(MyApplication.class);
        System.out.println(bean1.getValidatorFactories());
        System.out.println(bean1.getLocalValidatorFactoryBean());
        ValidatorFactory bean = run.getBean(ValidatorFactory.class);
        System.out.println(bean);///org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
        System.out.println(run.getBean(Validator.class) instanceof OptionalValidatorFactoryBean);
        System.out.println(run.getBean(Validator.class));

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        System.out.println(runtimeMXBean.getStartTime());
    }
}

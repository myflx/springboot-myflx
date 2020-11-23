package com.myflx;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Collection;

/**
 * @author LuoShangLin
 */
@SpringBootApplication
//@EnableDiscoveryClient
@EnableEurekaClient
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

    @EventListener
    public void onHandle(ServletRequestHandledEvent event) {
        // 如果处理失败会重新调度一次 接口 /error 返回错误页面
        System.out.println(event);
    }
}

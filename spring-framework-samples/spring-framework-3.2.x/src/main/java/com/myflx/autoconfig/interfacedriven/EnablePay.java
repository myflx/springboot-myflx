package com.myflx.autoconfig.interfacedriven;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
//@Import(ImportServerSelector.class)
@Import(ImportServerBeanDefinitionRegistrar.class)
public @interface EnablePay {
    Pay.Type type();
}

package com.myflx.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
@Service(value = "transactionalService")
public @interface TransactionalService {

    // version1.0
    /*@AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";*/


    //version2.0

    /**
     * 服务bean的名称
     * @return
     */
    /*String name() default "";

    String transactionManager() default "txManager";*/


    //version 3.0
    String name() default "";

    String value() default "txManager";

}

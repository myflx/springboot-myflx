package com.myflx.annotation;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Component
@Repository
public @interface StringRepository {
    String value() default "";
}

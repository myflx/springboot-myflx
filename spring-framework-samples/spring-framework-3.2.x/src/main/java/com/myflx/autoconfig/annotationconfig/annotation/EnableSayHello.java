package com.myflx.autoconfig.annotationconfig.annotation;

import com.myflx.autoconfig.annotationconfig.configure.SayHelloConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SayHelloConfiguration.class)
public @interface EnableSayHello {
}

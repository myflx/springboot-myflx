package com.myflx.aop.ltw;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

import static org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving.ENABLED;

@Configuration
@ComponentScan("com.myflx.aop.ltw")
@EnableLoadTimeWeaving(aspectjWeaving = ENABLED)
//@EnableAspectJAutoProxy
public class CustomLtwConfig {
}
